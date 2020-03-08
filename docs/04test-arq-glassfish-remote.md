# Glassfish Remote Container Adapter

Assume there is a running Glassfish server.  In this case, the Glassfish Remote Container Adapter connects to the Glassfish server by deployment client APIs.

Start the Glassfish server manually if it is not started. 

```bash
asadmin start-domain domain1
```

> The Glassfish server can be a local server instance, or running on a remote server,  make sure the management port 4848 is accessible.

Open the *pom.xml*, and check the *arq-glassfish-remote* profile.

```xml
<profile>
    <id>arq-glassfish-remote</id>
    <properties>
        <skipTests>false</skipTests>
    </properties>
    <dependencies>

        <!-- Jersey -->
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

        <!-- WebSocket client dependencies -->
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
            <artifactId>arquillian-glassfish-remote-3.1</artifactId>
            <version>${arquillian-glassfish.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-failsafe-plugin</artifactId>
                <version>${maven-failsafe-plugin.version}</version>
                <configuration>
                    <!-- This needs tuning -->
                    <systemPropertyVariables>
                        <arquillian.launch>glassfish-remote</arquillian.launch>
                    </systemPropertyVariables>
                </configuration>
            </plugin>
        </plugins>
    </build>
</profile>
```

In the configuration of `maven-failsafe-plugin`, there is a  `arquillian.launch`  property to specify which arquillian qualifier will be selected to run the test.

Have a look at the `glassfish-remote`  qualifier in *src/resources/arquillian.xml*.

```xml
<container qualifier="glassfish-remote">
    <configuration>
        <property name="adminUser">admin</property>
        <property name="adminPassword">adminadmin</property>
    </configuration>
</container>
```

Here we assume it connects to *localhost:4848*, and the admin password is changed to *adminadmin*. 

Run the tests.

```bash
mvn clean verify -Parq-glassfish-remote
```

You will see the following info in the console.

```bash
[INFO] --- maven-failsafe-plugin:3.0.0-M4:integration-test (integration-test) @ jakartaee8-starter ---
[INFO]
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.example.it.GreetingResourceTest
Feb 22, 2020 7:25:32 PM org.jboss.arquillian.container.impl.MapObject populate
WARNING: Configuration contain properties not supported by the backing object org.jboss.arquillian.container.glassfish.remote_3_1.GlassFishRestConfiguration
Supported property names: [adminHttps, remoteServerHttpPort, libraries, type, remoteServerAddress, target, retries, remoteServerAdminPort, remoteServerAdminHttps, adminUser, authorisation, waitTimeMs, adminPort, properties, adminPassword, adminHost]
Feb 22, 2020 7:25:46 PM com.example.it.GreetingResourceTest setup
WARNING: Only use for Open Liberty/CXF which does not register a json provider automatically.
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 16.217 s - in com.example.it.GreetingResourceTest
[INFO] Running com.example.it.GreetingServiceTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 4.217 s - in com.example.it.GreetingServiceTest
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
[INFO] Total time:  31.404 s
[INFO] Finished at: 2020-02-22T19:25:53+08:00
[INFO] -----------------------------------------------------------------
```
