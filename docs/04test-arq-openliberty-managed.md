# Open Liberty Managed Container Adapter 

Open the *pom.xml* file, check the **arq-liberty-managed** profile.

```xml
<profile>
    <!-- Run with: mvn clean test -Parq-liberty-managed -->
    <id>arq-liberty-managed</id>
    <properties>
        <skipTests>false</skipTests>
    </properties>
    <dependencies>
        <dependency>
            <groupId>io.openliberty.arquillian</groupId>
            <artifactId>arquillian-liberty-managed</artifactId>
            <version>${arquillian-liberty.version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.cxf</groupId>
            <artifactId>cxf-rt-rs-client</artifactId>
            <version>${cxf.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.jaxrs</groupId>
            <artifactId>jackson-jaxrs-json-provider</artifactId>
            <version>2.10.1</version>
        </dependency>
        <dependency>
            <groupId>org.apache.cxf</groupId>
            <artifactId>cxf-rt-transports-http-hc</artifactId>
            <version>${cxf.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <build>
        <testResources>
            <testResource>
                <directory>src/test/arq-liberty-managed</directory>
                <includes>
                    <include>*</include>
                </includes>
                <excludes>
                    <exclude>server.xml</exclude>
                </excludes>
            </testResource>
            <testResource>
                <directory>src/test/arq-liberty-managed</directory>
                <includes>
                    <include>server.xml</include>
                </includes>
                <targetPath>
                    ${project.build.directory}/wlp/usr/servers/defaultServer
                </targetPath>
            </testResource>
        </testResources>
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
                                    <groupId>io.openliberty</groupId>
                                    <artifactId>openliberty-runtime</artifactId>
                                    <version>${liberty.runtime.version}</version>
                                    <type>zip</type>
                                    <overWrite>false</overWrite>
                                    <outputDirectory>${project.build.directory}</outputDirectory>
                                </artifactItem>
                                <artifactItem>
                                    <groupId>io.openliberty.arquillian</groupId>
                                    <artifactId>arquillian-liberty-support</artifactId>
                                    <version>${arquillian-liberty.version}</version>
                                    <type>zip</type>
                                    <classifier>feature</classifier>
                                    <overWrite>false</overWrite>
                                    <outputDirectory>${project.build.directory}/wlp/usr</outputDirectory>
                                </artifactItem>
                            </artifactItems>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <artifactId>maven-failsafe-plugin</artifactId>
                <version>${maven-failsafe-plugin.version}</version>
                <configuration>
                    <environmentVariables>
                        <WLP_HOME>${project.build.directory}/wlp</WLP_HOME>
                    </environmentVariables>
                    <systemPropertyVariables>
                        <arquillian.launch>liberty-managed</arquillian.launch>
                        <java.util.logging.config.file>${project.build.testOutputDirectory}/logging.properties
                        </java.util.logging.config.file>
                    </systemPropertyVariables>
                </configuration>
            </plugin>
        </plugins>
    </build>
</profile>
```

In the above codes, 

* The `arquillian-liberty-managed` is required to activate an Arquillian Managed Container Adapter.

* The Apache CXF related libraries are required for JAX-RS tests.

* The  `maven-dependency-plugin` prepare a copy of Open Liberty dist and `arquillian-liberty-support`(which provides `usr:arquillian-support-1.0` feature).

* Open Liberty requires a *server.xml* profile to run the server with necessary features. In the */src/test/arq-liberty-managed* folder, there is *server.xml* , use Maven resources plugin to copy it the *Open Liberty server's  usr/servers/defaultServer/*  folder.

Firstly, let's have a look at the `GreetingResourceTest`. Apache CXF does not register JAX-RS provider resources automatically, add `JacksonJsonProvider` manually, else you will get failure when the tests.

```java
@Before
public void setup()  {
    ...
        try {
            Class<?> clazz = Class.forName("com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider");
            this.client.register(clazz);
        } catch (ClassNotFoundException e) {
            LOGGER.warning("Only use for Open Liberty/CXF which does not register a json provider automatically.");
        }
}    
```

Let's have a look at the */src/test/arq-liberty-managed/arquillian.xml*.  

```xml
<container qualifier="liberty-managed">
    <configuration>
        <property name="wlpHome">target/wlp/</property>
        <property name="serverName">defaultServer</property>
        <property name="serverStartTimeout">300</property>
    </configuration>
</container>
```

The **wlpHome** property is required.

> Unlike Payara and WildFly, you have to configure the **wplHome** property  in *arquillian.xml* file, if set in `maven-failsafe-plugin`, it does not work.

Now let's move to  */src/test/arq-liberty-managed/server.xml*.

```xml
<server description="new server">

    <!-- Enable features -->
    <featureManager>
        <feature>javaee-8.0</feature>
        <feature>usr:arquillian-support-1.0</feature>
        <feature>localConnector-1.0</feature>
    </featureManager>
    
    <basicRegistry id="basic" realm="BasicRealm"> 
        <!-- <user name="yourUserName" password="" />  --> 
    </basicRegistry>
    
    <!-- To access this server from a remote client add a host attribute to the following element, e.g. host="*" -->
    <httpEndpoint id="defaultHttpEndpoint"
                  httpPort="9080"
                  httpsPort="9443" />
                  
    <!-- Automatically expand WAR files and EAR files -->
    <applicationManager autoExpand="true"/>

</server>
```

The **localConnector-1.0**  is required by Arquillian Liberty Managed Container Adapter to control the managed Open Liberty server.

Run the tests.

```bash
[INFO] --- maven-failsafe-plugin:3.0.0-M4:integration-test (integration-test) @ jakartaee8-starter ---
[INFO]
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.example.it.GreetingResourceTest
...
[AUDIT   ] CWWKE0001I: The server defaultServer has been launched.
...
[INFO    ] CWWKE0002I: The kernel started after 5.481 seconds
[INFO    ] CWWKF0007I: Feature update started.
...
[AUDIT   ] CWWKZ0058I: Monitoring dropins for applications.
...
[AUDIT   ] CWWKF0012I: The server installed the following features: [appClientSupport-1.0, appSecurity-2.0, appSecurity-3.0, batch-1.0, beanValidation-2.0, cdi-2.0, concurrent-1.0, distributedMap-1.0, ejb-3.2, ejbHome-3.2, ejbLite-3.2, ejbPersistentTimer-3.2, ejbRemote-3.2, el-3.0, j2eeManagement-1.1, jacc-1.5, jaspic-1.1, javaMail-1.6, javaee-8.0, jaxb-2.2, jaxrs-2.1, jaxrsClient-2.1, jaxws-2.2, jca-1.7, jcaInboundSecurity-1.0, jdbc-4.2, jms-2.0, jndi-1.0, jpa-2.2, jpaContainer-2.2, jsf-2.3, jsonb-1.0, jsonp-1.1, jsp-2.3, localConnector-1.0, managedBeans-1.0, mdb-3.2, servlet-4.0, ssl-1.0, usr:arquillian-support-1.0, wasJmsClient-2.0, wasJmsSecurity-1.0, wasJmsServer-1.0, webProfile-8.0, websocket-1.1].
[INFO    ] CWWKF0008I: Feature update completed in 13.419 seconds.
[AUDIT   ] CWWKF0011I: The defaultServer server is ready to run a smarter planet. The defaultServer server started in 18.904 seconds.
[INFO    ] SRVE0169I: Loading Web Module: arquillian-liberty-support.
[INFO    ] SRVE0250I: Web Module arquillian-liberty-support has been bound to default_host.
[AUDIT   ] CWWKT0016I: Web application available (default_host): http://localhost:9080/arquillian-support/
[INFO    ] SESN8501I: The session manager did not find a persistent storage location; HttpSession objects will be stored in the local application server's memory.
[INFO    ] SESN0176I: A new session context will be created for application key default_host/arquillian-support
[INFO    ] SESN0172I: The session manager is using the Java default SecureRandom implementation for session ID generation.
...
Feb 23, 2020 9:38:33 PM io.openliberty.arquillian.managed.WLPManagedContainer deploy
FINER: Archive provided to deploy method: f426e2fa-d747-4810-96ca-ad7c05dbda8b.war:
...
[INFO    ] SRVE0169I: Loading Web Module: f426e2fa-d747-4810-96ca-ad7c05dbda8b.
[INFO    ] SRVE0250I: Web Module f426e2fa-d747-4810-96ca-ad7c05dbda8b has been bound to default_host.
[AUDIT   ] CWWKT0016I: Web application available (default_host): http://localhost:9080/f426e2fa-d747-4810-96ca-ad7c05dbda8b/
[AUDIT   ] CWWKZ0001I: Application f426e2fa-d747-4810-96ca-ad7c05dbda8b started in 2.947 seconds.
...
[INFO    ] SRVE0242I: [f426e2fa-d747-4810-96ca-ad7c05dbda8b] [/f426e2fa-d747-4810-96ca-ad7c05dbda8b] [com.example.JaxrsActivator]: Initialization successful.
Feb 23, 2020 9:38:39 PM io.openliberty.arquillian.managed.WLPManagedContainer undeploy
...
[AUDIT   ] CWWKT0017I: Web application removed (default_host): http://localhost:9080/f426e2fa-d747-4810-96ca-ad7c05dbda8b/
[INFO    ] SRVE0253I: [f426e2fa-d747-4810-96ca-ad7c05dbda8b] [/f426e2fa-d747-4810-96ca-ad7c05dbda8b] [com.example.JaxrsActivator]: Destroy successful.
[AUDIT   ] CWWKZ0009I: The application f426e2fa-d747-4810-96ca-ad7c05dbda8b has stopped successfully.
...
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 28.824 s - in com.example.it.GreetingResourceTest
[INFO] Running com.example.it.GreetingServiceTest
...
Feb 23, 2020 9:38:41 PM io.openliberty.arquillian.managed.WLPManagedContainer deploy
FINER: Archive provided to deploy method: test.war:
...
Feb 23, 2020 9:38:41 PM io.openliberty.arquillian.managed.WLPManagedContainer deploy
FINER: Deployment done

...
Feb 23, 2020 9:38:45 PM io.openliberty.arquillian.managed.WLPManagedContainer undeploy
FINER: RETURN
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 5.862 s - in com.example.it.GreetingServiceTest
Feb 23, 2020 9:38:45 PM io.openliberty.arquillian.managed.WLPManagedContainer stop
FINER: ENTRY
Stopping server defaultServer.
...
[AUDIT   ] CWWKE0036I: The server defaultServer stopped after 35.5 seconds.
...
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
[INFO] Total time:  01:09 min
[INFO] Finished at: 2020-02-23T21:38:49+08:00
[INFO] ------------------------------------------------------------------------
```

From the logs, it works similar with the former WildFly Managed Container Adapter.

* Starting up Open Liberty Kernel

* Enabling required features in configured in server.xml

* Deploying the test archive

* Running tests

* Undeploy the test archive

* Reporting the test results, repeating the above steps if there are more tests to run

* Finally stopping Open Liberty.