# WildFly Remote Container Adapter 

Open the *pom.xml* file, check the **arq-wildfly-remote** profile.

```xml
<profile>
    <id>arq-wildfly-remote</id>
    <properties>
        <skipTests>false</skipTests>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.jboss.resteasy</groupId>
            <artifactId>resteasy-client</artifactId>
            <version>${resteasy.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.jboss.resteasy</groupId>
            <artifactId>resteasy-jackson2-provider</artifactId>
            <version>${resteasy.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.wildfly.arquillian</groupId>
            <artifactId>wildfly-arquillian-container-remote</artifactId>
            <version>${wildfly-arquillian.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <artifactId>maven-failsafe-plugin</artifactId>
                <version>${maven-failsafe-plugin.version}</version>
                <configuration>
                    <systemPropertyVariables>
                        <arquillian.launch>wildfly-remote</arquillian.launch>
                        <java.util.logging.manager>org.jboss.logmanager.LogManager</java.util.logging.manager>
                    </systemPropertyVariables>
                </configuration>
            </plugin>
        </plugins>
    </build>
</profile>
```

The content of  `wildfly-remote` qualifier in the *src/test/resources/arquillian.xml*.

```xml
<!-- Configuration to be used when the WidlFly remote profile is active -->
<container qualifier="wildfly-remote">
    <configuration>
        <property name="managementAddress">127.0.0.1</property>
        <property name="managementPort">9990</property>
        <property name="protocol">http-remoting</property>
        <property name="username">admin</property>
        <property name="password">Admin@123</property>
    </configuration>
</container>
```

Run the  tests.

```bash
mvn clean verify -Parq-wildfly-remote
```

You will the following info in the console.

```bash
[INFO] --- maven-failsafe-plugin:3.0.0-M4:integration-test (integration-test) @ jakartaee8-starter ---
[INFO]
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.example.it.GreetingResourceTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 11.022 s - in com.example.it.GreetingResourceTest
[INFO] Running com.example.it.GreetingServiceTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 3.834 s - in com.example.it.GreetingServiceTest
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO]
[INFO] --- maven-failsafe-plugin:3.0.0-M4:verify (integration-test) @ jakartaee8-starter ---
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  27.782 s
[INFO] Finished at: 2020-02-23T18:22:09+08:00
[INFO] ------------------------------------------------------------------------
```

More logs of the WildFly server, check the content of the *server.log* in the *&lt; WildFly dir>/standalone/log* folder.

```bash
...
18:21:57,075 INFO  [org.jboss.as.server.deployment] (MSC service thread 1-5) WFLYSRV0027: Starting deployment of "34fea863-e144-445e-ae22-c2736ba8915b.war" (runtime-name: "34fea863-e144-445e-ae22-c2736ba8915b.war")
...
18:22:03,208 INFO  [org.jboss.as.server] (management-handler-thread - 1) WFLYSRV0010: Deployed "34fea863-e144-445e-ae22-c2736ba8915b.war" (runtime-name : "34fea863-e144-445e-ae22-c2736ba8915b.war")
...
18:22:04,587 INFO  [org.jboss.as.server] (management-handler-thread - 1) WFLYSRV0009: Undeployed "34fea863-e144-445e-ae22-c2736ba8915b.war" (runtime-name: "34fea863-e144-445e-ae22-c2736ba8915b.war")
...
18:22:05,929 INFO  [org.jboss.as.server.deployment] (MSC service thread 1-2) WFLYSRV0027: Starting deployment of "test.war" (runtime-name: "test.war")
...
18:22:08,322 INFO  [org.jboss.as.server] (management-handler-thread - 1) WFLYSRV0010: Deployed "test.war" (runtime-name : "test.war")
...
18:22:08,576 INFO  [org.jboss.as.server] (management-handler-thread - 1) WFLYSRV0009: Undeployed "test.war" (runtime-name: "test.war")
```

