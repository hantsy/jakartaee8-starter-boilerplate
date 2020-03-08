#  Glassfish Managed Container Adapter

Open *pom.xml*, check the **arq-glassfish-managed** profile.

```xml
<profile>
    <id>arq-glassfish-managed</id>
    <properties>
        <skipTests>false</skipTests>
    </properties>
    <dependencies>
        <!-- Jersey Client -->
        <dependency>
            <groupId>org.glassfish.jersey.media</groupId>
            <artifactId>jersey-media-sse</artifactId>
            <version>${jersey.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.glassfish.jersey.media</groupId>
            <artifactId>jersey-media-json-binding</artifactId>
            <version>${jersey.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.glassfish.jersey.inject</groupId>
            <artifactId>jersey-hk2</artifactId>
            <version>${jersey.version}</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.glassfish.jersey.core</groupId>
            <artifactId>jersey-client</artifactId>
            <version>${jersey.version}</version>
            <scope>test</scope>
        </dependency>

        <!-- WebSocket client dependencies-->
        <!--<dependency>
                    <groupId>org.glassfish.tyrus</groupId>
                    <artifactId>tyrus-client</artifactId>
                    <version>1.13.1</version>
                    <scope>test</scope>
                </dependency>
                <dependency>
                    <groupId>org.glassfish.tyrus</groupId>
                    <artifactId>tyrus-container-grizzly-client</artifactId>
                    <version>1.13.1</version>
                    <scope>test</scope>
                </dependency>-->

        <dependency>
            <groupId>org.jboss.arquillian.container</groupId>
            <artifactId>arquillian-glassfish-managed-3.1</artifactId>
            <version>${arquillian-glassfish.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-dependency-plugin</artifactId>
                <version>${maven-dependency-plugin.version}</version>
                <executions>
                    <execution>
                        <id>unpack</id>
                        <phase>pre-integration-test</phase>
                        <goals>
                            <goal>unpack</goal>
                        </goals>
                        <configuration>
                            <artifactItems>
                                <artifactItem>
                                    <groupId>org.glassfish.main.distributions</groupId>
                                    <artifactId>glassfish</artifactId>
                                    <version>${glassfish.version}</version>
                                    <type>zip</type>
                                    <overWrite>false</overWrite>
                                    <outputDirectory>${project.build.directory}</outputDirectory>
                                </artifactItem>
                            </artifactItems>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-failsafe-plugin</artifactId>
                <version>${maven-failsafe-plugin.version}</version>
                <configuration>
                    <!-- This needs tuning -->
                    <environmentVariables>
                        <GLASSFISH_HOME>${project.build.directory}/glassfish5</GLASSFISH_HOME>
                    </environmentVariables>
                </configuration>
            </plugin>
        </plugins>
    </build>
</profile>
```

In the above codes,  it adds  `arquillian-glassfish-managed-3.1` as a dependency of *test* scope, which is required to serve *Glassfish Managed Container Adapter*.

The Jersey Client related dependencies are required for JAX-RS testing, eg. the `GreetingResourceTest`.  

Here we use `maven-dependency-plugin` to prepare a refresh copy of  Glassfish dist for the test, and unzip it to the path `${project.build.directory}`.  In the configuration of `maven-failsafe-plugin`,  set a `GLASSFISH_HOME` environment variable or configure a `glassfishHome` system property in the Arquillian configuration file - *src/test/resources/arquillian.xml*, specify its location to the `${project.build.directory}/glassfish5`.

The *Glassfish Managed Container Adapter* controls the lifecycle of the managed Glassfish server, eg. 

* Start the Glassfish server
* Deploy the test archive into the Glassfish server
* Run tests in container
* Undeploy the test archive
* Stop the Glassfish server

Execute the following command to run the tests against the **arq-glassfish-managed** profile.

```bash
mvn clean verify -Parq-glassfish-managed
```

You will see the following info in the console.

```bash
[INFO] --- maven-failsafe-plugin:3.0.0-M4:integration-test (integration-test) @ jakartaee8-starter ---
[INFO]
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.example.it.GreetingResourceTest
Starting container using command: [java, -jar, D:\hantsylabs\jakartaee8-starter\target\glassfish5\glassfish\modules\admin-cli.jar, start-domain, -t]
Attempting to start domain1.... Please look at the server log for more details.....
Feb 22, 2020 6:17:24 PM com.example.it.GreetingResourceTest setup
WARNING: Only use for Open Liberty/CXF which does not register a json provider automatically.
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 26.895 s - in com.example.it.GreetingResourceTest
[INFO] Running com.example.it.GreetingServiceTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 50.739 s - in com.example.it.GreetingServiceTest
Stopping container using command: [java, -jar, D:\hantsylabs\jakartaee8-starter\target\glassfish5\glassfish\modules\admin-cli.jar, stop-domain, -t]
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO]
[INFO] --- maven-failsafe-plugin:3.0.0-M4:verify (integration-test) @ jakartaee8-starter ---
[INFO] -----------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] -----------------------------------------------------------------
[INFO] Total time:  01:51 min
[INFO] Finished at: 2020-02-22T18:18:18+08:00
[INFO] -----------------------------------------------------------------
```
