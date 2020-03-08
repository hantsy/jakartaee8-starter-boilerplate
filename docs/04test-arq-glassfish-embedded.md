# Glassfish Embedded Container Adapter

Open the *pom.xml* file, check the **arq-glassfish-embedded** profile.

```xml
<profile>
    <!-- JAXRS tests `GreetingResourceTest` failed with aruqillian glassfish embedded container -->
    <!-- See: https://github.com/arquillian/arquillian-container-glassfish/issues/62 -->
    <id>arq-glassfish-embedded</id>
    <properties>
        <skipTests>false</skipTests>
    </properties>
    <dependencies>
        <!-- Jersey Client -->
        <!--<dependency>
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
                    <groupId>org.glassfish</groupId>
                    <artifactId>javax.json</artifactId>
                    <version>1.0.4</version>
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
                </dependency>-->

        <dependency>
            <groupId>org.glassfish.main.extras</groupId>
            <artifactId>glassfish-embedded-all</artifactId>
            <version>${glassfish.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.jboss.arquillian.container</groupId>
            <artifactId>arquillian-glassfish-embedded-3.1</artifactId>
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
                    <excludes>
                        <exclude>**/it/GreetingResourceTest*</exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</profile>
```

Besides  adding `arquillian-glassfish-embedded-3.1` as dependency, add an extra `glassfish-embedded-all` as dependencies.  The controller(Glassfish Embedded Container Adapter) is fully responsible for the lifecycle of the embedded Glassfish , and will run with the embedded Glassfish in the same JVM process. 

Run the tests using the following command.

```bash
mvn clean verify -Parq-glassfish-embedded
```

The result info is like :

```bash
...
[INFO] Results:
[INFO]
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO]
[INFO] --- maven-failsafe-plugin:3.0.0-M4:verify (integration-test) @ jakartaee8-starter ---
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  32.238 s
[INFO] Finished at: 2020-02-22T20:07:21+08:00
[INFO] ------------------------------------------------------------------------
```

> Unfortunately, I can not get the JAX-RS test run successfully on Glassfish Embedded Container Adapter, see issue [arquillian/arquillian-container-glassfish#62](https://github.com/arquillian/arquillian-container-glassfish/issues/62).

