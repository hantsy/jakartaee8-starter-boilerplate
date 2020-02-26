# Testing Jakarta EE applications

Generally, in a Jakarta EE applications, there are some tools use for testing Jakarta EE components.

* Use [JUnit](https://junit.org/) or [TestNG](https://testng.org/) to test simple POJOs.
* Use [Mockito](https://site.mockito.org/) like mock framework to isolate a dependent part of a component in tests.
* Test Jakarta EE components in a real world environment with [JBoss Arquillian](https://arquillian.org).

Besides, there are some extensions existed for improving testing productivity.

* [AssertJ](https://assertj.github.io) includes a collection of fluent assertions.
* [RestAssured](http://rest-assured.io/) provides BDD like behaviors for testing RESTful APIs.
* [JsonPath](https://github.com/json-path/JsonPath) provides XPath like query for JSON.
* [awaitility](https://github.com/awaitility/awaitility) provides assertions for async invocation.

In this post, we will focus on testing Jakarta EE components with [JBoss Arquillian](https://arquillian.org).

[toc]

## Add Arquillian Dependencies

Add the `arquillian-bom` as an dependency into `dependencyManagement` section.

```xml
<dependencyManagement>
    <dependencies>
		...
        <dependency>
            <groupId>org.jboss.arquillian</groupId>
            <artifactId>arquillian-bom</artifactId>
            <version>${arquillian-bom.version}</version>
            <scope>import</scope>
            <type>pom</type>
        </dependency>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>
     </dependencies>
</dependencyManagement>
```

The `arquillian-bom` is a Maven *Bill of Materials*  which manages a collection of arquillian core dependencies.

We also add another dependency `junit` here. Arquillian also supports for TestNG,  we use JUnit as a test runner here.

> Note: JUnit 5 had been released for a while, but Arquillian does not support JUnit 5 at the moment, see issue [arquillian/arquillian-core#137](https://github.com/arquillian/arquillian-core/issues/137).

In the `dependencies` section, add the following two dependencies.

```xml
<dependencies>
    ...
	<dependency>
        <groupId>org.jboss.arquillian.junit</groupId>
        <artifactId>arquillian-junit-container</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
    </dependency>
</dependencies>    
```

## Create your first Arquillian Test

A simple Arquillian test looks like the following.

```java
@RunWith(Arquillian.class)
public class GreetingServiceTest {
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(GreetingMessage.class)
                .addClass(GreetingService.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    GreetingService service;

    @Test
    public void should_create_greeting() {
        GreetingMessage message = service.buildGreetingMessage("Jakarta EE");
        assertTrue("message should start with \"Say Hello to Jakarta EE at \"",
                message.getMessage().startsWith("Say Hello to Jakarta EE at "));
    }
}
```

This test is use for testing the functionality of `GreetingService`.

* `@RunWith(Arquillian.class)` indicates it use an Arquillian specific test runner to run this test.
* `@Deployment` creates a deployment unit for this test.
* The CDI bean `GreetingService`  can be injected in test codes like general Jakarta EE managed beans.
* `should_create_greeting()` is a simple test case to verify the functionality of `GreetingService`.

When running the test, first of all, it will create a deployable archive as described in the `@Deployment` annotated static method. In this sample test, it just need to package `GreetingMessage`, `GreetingService`, and a CDI  `beans.xml` descriptor(which is optional since Java EE 7) into a jar archive. Then it will be deployed into an application server,  and run the tests in container, the test report will be captured by Arquillian controller and sent back to the test runner.

Let's look at another sample test in the [Jakarta EE 8 starter repository](https://github.com/hantsy/jakartaee8-starter).

```java
@RunWith(Arquillian.class)
public class GreetingResourceTest {
    private final static Logger LOGGER = Logger.getLogger(GreetingResourceTest.class.getName());

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClass(GreetingMessage.class)
                .addClass(GreetingService.class)
                .addClasses(GreetingResource.class, JaxrsActivator.class)
                // Enable CDI
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @ArquillianResource
    private URL base;

    private Client client;

    @Before
    public void setup()  {
        this.client = ClientBuilder.newClient();
        try {
            Class<?> clazz = Class.forName("com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider");
            this.client.register(clazz);
        } catch (ClassNotFoundException e) {
            LOGGER.warning("Only use for Open Liberty/CXF which does not register a json provider automatically.");
        }

    }

    @After
    public void teardown() {
        if(this.client != null) {
            this.client.close();
        }
    }

    @Test
    public void should_create_greeting() throws MalformedURLException {
        final WebTarget greetingTarget = client.target(URI.create(new URL(base, "api/greeting/JakartaEE").toExternalForm()));
        try (final Response greetingGetResponse = greetingTarget.request()
                .accept(MediaType.APPLICATION_JSON)
                .get()) {
            assertEquals("response status is ok", 200, greetingGetResponse.getStatus());
            assertTrue("message should start with \"Say Hello to JakartaEE at \"",
                    greetingGetResponse.readEntity(GreetingMessage.class).getMessage().startsWith("Say Hello to JakartaEE"));

        }
    }
}
```

This `GreetingResourceTest` is use for testing the endpoints exposed by the `GreetingResource`.

As you see , this test is slightly different from the former `GreetingServiceTest`.

* We create a `WebArchive` instead of `JavaArchive`, because this is for testing the functionality of Jaxrs resources.
* A `testable = false` property is added to `@Deployment`, thus the test case will run as *client mode*.  That means the test method `should_create_greeting()` will run on a different JVM process, and resources in the archive, such as `GreetingService` can not be injected in this test.
* You can get the the context path from a ` @ArquillianResource` annotated URI after it is deployed.

To run the test on a certain application server and gather the test result, you have to configure an *Aquillian Container Adapter*.

In the Maven *pom.xml*, a couple of Maven profiles are configured for applying different container adapters.

## Glassfish Server

Arquillian provides 3 adapters to Glassfish server, check the project [arquillian/arquillian-container-glassfish](https://github.com/arquillian/arquillian-container-glassfish).

* [GlassFish Managed 3.1 Container Adapter – 1.0.2 ](http://arquillian.org/modules/arquillian-glassfish-managed-3.1-container-adapter/)         
* [GlassFish Remote 3.1 Container Adapter – 1.0.2](http://arquillian.org/modules/arquillian-glassfish-remote-3.1-container-adapter/)              
* [GlassFish Embedded 3.1 Container Adapter   – 1.0.2 ](http://arquillian.org/modules/arquillian-glassfish-embedded-3.1-container-adapter/)       

### Add Glassfish Managed Container Adapter

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

In the above codes,  it adds  `arquillian-glassfish-managed-3.1` as a dependency, which is required to serve *Glassfish Managed Container Adapter*.

The Jersey related dependencies are required for Jaxrs testing, eg. the `GreetingResourceTest`.  

Here we use `maven-dependency-plugin` to prepare a refresh copy of  Glassfish dist for the test, and unzip it to the path `${project.build.directory}`.  In the configuration of `maven-failsafe-plugin`,  set a `GLASSFISH_HOME` environment variable or configure a `glassfishHome` property in the Arquillian configuration file - *src/test/resources/arquillian.xml*, specify its location to the `${project.build.directory}/glassfish5`.

The *Glassfish Managed Container Adapter* controls the lifecycle of the Glassfish server, eg. 

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

### Add Glassfish Remote Container Adapter

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
Here we assume it connects to *localhost:4848*. 

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

### Add Glassfish Embedded Container Adapter

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

An embedded Glassfish is added into the dependencies, the controller(Glassfish Embedded Container Adapter) is fully responsible for the lifecycle of the embedded Glassfish , and will run with the embedded Glassfish in the same JVM process. 

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

## Payara Server

Payara Server is a fork of Glassfish, you can use the Glassfish container adapter to test on Payara server directly. But Payara maintain [its own container adapters](https://github.com/payara/ecosystem-arquillian-connectors) for Payara Server. Compared to the Glassfish ones, they are updated more frequently, and easier to use.

### Add Payara Managed Container Adapter

Open the *pom.xml* , check the **arq-payara-managed** profile.

```xml
<profile>
    <id>arq-payara-managed</id>
    <properties>
        <skipTests>false</skipTests>
    </properties>
    <dependencies>
        <!-- Payara Server Container adaptor -->
        <dependency>
            <groupId>fish.payara.arquillian</groupId>
            <artifactId>arquillian-payara-server-managed</artifactId>
            <version>${arquillian-payara-server.version}</version>
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
                                    <groupId>fish.payara.distributions</groupId>
                                    <artifactId>payara</artifactId>
                                    <version>${payara.version}</version>
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
                    <environmentVariables>
                        <GLASSFISH_HOME>${project.build.directory}/payara5</GLASSFISH_HOME>
                    </environmentVariables>
                </configuration>
            </plugin>
        </plugins>
    </build>
</profile>
```

Very similar with **arq-glassfish-managed** profile, but there are some difference,  including:

* Add `arquillian-payara-server-managed` as dependency instead.
* The `maven-dependency-plugin` prepares a copy of Payara server to *target* folder. Set a `GLASSFISH_HOME` (or  `PAYARA_HOME`) environment variable in the configuration of `maven-failsafe-plugin`.
* For JAX-RS tests, no need to add the Jersey client libraries, `arquillian-payara-server-managed` resolves the tedious dependency configuration.

Run the following command to run tests against Payara Managed Container Adapter.

```bash
mvn clean verify -Parq-payara-managed
```



### Add Payara Remote Container Adapter

Open the *pom.xml* file, locate to **arq-payara-remote** profile.

```xml
<profile>
    <id>arq-payara-remote</id>
    <properties>
        <skipTests>false</skipTests>
    </properties>
    <dependencies>
        <dependency>
            <groupId>fish.payara.arquillian</groupId>
            <artifactId>arquillian-payara-server-remote</artifactId>
            <version>${arquillian-payara-server.version}</version>
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

It reuses `glassfish-remote`  qualifier in *src/resources/arquillian.xml*.

```xml
<container qualifier="glassfish-remote">
    <configuration>
        <property name="adminUser">admin</property>
        <property name="adminPassword">adminadmin</property>
    </configuration>
</container>
```

Start up a local Payara server for test purpose, make sure the `adminPassword` is set correctly.

Run the tests using the following command.

```bash
mvn clean verify -Parq-payara-remote
```



### Add Payara Embedded Container Adapter

Open the *pom.xml* file, check the **arq-payara-embedded** profile.

```xml
<profile>
    <id>arq-payara-embedded</id>
    <properties>
        <skipTests>false</skipTests>
    </properties>
    <dependencies>
        <dependency>
            <groupId>fish.payara.extras</groupId>
            <artifactId>payara-embedded-all</artifactId>
            <version>${payara.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>fish.payara.arquillian</groupId>
            <artifactId>arquillian-payara-server-embedded</artifactId>
            <version>${arquillian-payara-server.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
</profile>
```

Similar with the Glassfish's **arq-glassfish-embedded** profile, but add the Payara specific dependencies instead.

Run the tests using the following command.

```bash
mvn clean verify -Parq-payara-remote
```

### Add Payara Micro Managed Container Adapter

Payara server provides a **micro** version of Payara server, which allow you start a  Payara server with a single jar file.

Payara also includes an Micro Managed Adapter to run tests against a Payara Micro dist.

Open  the *pom.xml* file, check the **arq-payara-micro** profile.

```xml
<profile>
    <id>arq-payara-micro</id>
    <properties>
        <skipTests>false</skipTests>
    </properties>
    <dependencies>

        <!-- https://mvnrepository.com/artifact/fish.payara.arquillian/payara-client-ee8 -->
        <dependency>
            <groupId>fish.payara.arquillian</groupId>
            <artifactId>payara-client-ee8</artifactId>
            <version>${arquillian-payara-server.version}</version>
            <scope>test</scope>
        </dependency>

        <!-- Payara Server Container adaptor -->
        <dependency>
            <groupId>fish.payara.arquillian</groupId>
            <artifactId>arquillian-payara-micro-managed</artifactId>
            <version>${arquillian-payara-server.version}</version>
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
                            <goal>copy</goal>
                        </goals>
                        <configuration>
                            <artifactItems>
                                <artifactItem>
                                    <groupId>fish.payara.extras</groupId>
                                    <artifactId>payara-micro</artifactId>
                                    <version>${payara.version}</version>
                                    <type>jar</type>
                                    <overWrite>false</overWrite>
                                    <outputDirectory>${project.build.directory}</outputDirectory>
                                    <destFileName>payara-micro.jar</destFileName>
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
                    <environmentVariables>
                        <MICRO_JAR>${project.build.directory}/payara-micro.jar</MICRO_JAR>
                    </environmentVariables>
                    <systemPropertyVariables>
                        <!--<payara.microJar>${project.build.directory}/payara-micro.jar</payara.microJar>-->
                    </systemPropertyVariables>
                </configuration>
            </plugin>
        </plugins>
    </build>
</profile>
```

Use `maven-dependency-plugin` to download a copy of Payara Micro to the *target* folder, set a `MICRO_JAR` environment variable or a `payara.microJar` system property in the configuration of `maven-failsafe-plugin`.  This adapter does not includes the jersey client libraries, add `payara-client-ee8` as dependencies.

Run the tests with **arq-payara-micro** profile.

```bash
mvn clean verify -Parq-payara-micro
```

You will the following info in the console.

```bash
...
[2020-02-23T16:50:30.161+0800] [] [INFO] [] [PayaraMicro] [tid: _ThreadID=1 _ThreadName=main] [timeMillis: 1582447830161] [levelValue: 800] Payara Micro  5.194 #badassmicrofish (build 327) ready in 19,746 (ms)

Feb 23, 2020 4:50:30 PM fish.payara.arquillian.container.payara.managed.PayaraMicroDeployableContainer deploy
INFO: Payara Micro running on host: 192.168.10.2 port: 8403
Feb 23, 2020 4:50:30 PM fish.payara.arquillian.container.payara.managed.PayaraMicroDeployableContainer printApplicationFound
INFO: Deployed application detected. Name: "test".
Feb 23, 2020 4:50:30 PM fish.payara.arquillian.container.payara.managed.PayaraMicroDeployableContainer printServletFound
INFO:           Servlet found. Name: "jsp".
Feb 23, 2020 4:50:30 PM fish.payara.arquillian.container.payara.managed.PayaraMicroDeployableContainer printServletFound
INFO:           Servlet found. Name: "ArquillianServletRunner".
Feb 23, 2020 4:50:30 PM fish.payara.arquillian.container.payara.managed.PayaraMicroDeployableContainer printServletFound
INFO:           Servlet found. Name: "jsp".
Feb 23, 2020 4:50:30 PM fish.payara.arquillian.container.payara.managed.PayaraMicroDeployableContainer printServletFound
INFO:           Servlet found. Name: "default".
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 29.906 s - in com.example.it.GreetingServiceTest
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
[INFO] Total time:  01:13 min
[INFO] Finished at: 2020-02-23T16:50:32+08:00
[INFO] ------------------------------------------------------------------------
```



## WildFly Server

Like Glassfish and Payara, [WildFly Arquillian](https://github.com/wildfly/wildfly-arquillian) also provide three basic container adapters. Additionally, it contains two adapters for managed domain and remote domain for that running WildFly servers in domain mode.



### Add WildFly Managed Container Adapter 

Open the *pom.xml* file, check the **arq-wildfly-managed** profile.

```xml
 <profile>
     <id>arq-wildfly-managed</id>
     <properties>
         <skipTests>false</skipTests>
         <serverProfile>standalone-full.xml</serverProfile>
         <serverRoot>${project.build.directory}/wildfly-${wildfly.version}</serverRoot>
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
             <artifactId>wildfly-arquillian-container-managed</artifactId>
             <version>${wildfly-arquillian.version}</version>
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
                                     <groupId>org.wildfly</groupId>
                                     <artifactId>wildfly-dist</artifactId>
                                     <version>${wildfly.version}</version>
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
                 <artifactId>maven-failsafe-plugin</artifactId>
                 <version>${maven-failsafe-plugin.version}</version>
                 <configuration>
                     <environmentVariables>
                         <JBOSS_HOME>${project.build.directory}/wildfly-${wildfly.version}</JBOSS_HOME>
                     </environmentVariables>
                     <systemPropertyVariables>
                         <java.util.logging.manager>org.jboss.logmanager.LogManager</java.util.logging.manager>
                     </systemPropertyVariables>
                 </configuration>
             </plugin>
         </plugins>
     </build>
</profile>
```

In the above codes,

* The `wildfly-arquillian-container-managed` is required for Arquillian WildFly Managed Container adapter.
* The Resteay related dependencies are required for JAX-RS tests.
* Use `maven-dependency-plugin` to download a copy of WildFly server to the *target* folder, and set `JBOSS_HOME` environment variable to use this downloaded WildFly in the `maven-failsafe-plugin`.

Run the tests via the following command.

```bash
mvn clean verify -Parq-wildfly-managed
```

You will see the following info in the console.

```bash
[INFO] --- maven-failsafe-plugin:3.0.0-M4:integration-test (integration-test) @ jakartaee8-starter ---
[INFO]
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.example.it.GreetingResourceTest
17:46:24,519 INFO  [org.jboss.modules] (main) JBoss Modules version 1.9.2.Final
17:46:26,697 INFO  [org.jboss.msc] (main) JBoss MSC version 1.4.11.Final
17:46:26,727 INFO  [org.jboss.threads] (main) JBoss Threads version 2.3.3.Final
17:46:26,954 INFO  [org.jboss.as] (MSC service thread 1-2) WFLYSRV0049: WildFly Full 19.0.0.Beta2 (WildFly Core 11.0.0.Beta8) starting
...
17:46:33,763 INFO  [org.jboss.as] (Controller Boot Thread) WFLYSRV0025: WildFly Full 19.0.0.Beta2 (WildFly Core 11.0.0.Beta8) started in 10021ms - Started 316 of 582 services (374 services are lazy, passive or on-demand)
...
17:46:34,207 INFO  [org.jboss.as.server.deployment] (MSC service thread 1-7) WFLYSRV0027: Starting deployment of "39fd4362-55da-49b0-8164-7a7a3d92f19f.war" (runtime-name: "39fd4362-55da-49b0-8164-7a7a3d92f19f.war")
...
17:46:37,713 INFO  [org.jboss.as.server] (management-handler-thread - 1) WFLYSRV0010: Deployed "39fd4362-55da-49b0-8164-7a7a3d92f19f.war" (runtime-name : "39fd4362-55da-49b0-8164-7a7a3d92f19f.war")
...
17:46:39,559 INFO  [org.jboss.as.server] (management-handler-thread - 1) WFLYSRV0009: Undeployed "39fd4362-55da-49b0-8164-7a7a3d92f19f.war" (runtime-name: "39fd4362-55da-49b0-8164-7a7a3d92f19f.war")
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 19.854 s - in com.example.it.GreetingResourceTest
[INFO] Running com.example.it.GreetingServiceTest
...
17:46:40,900 INFO  [org.jboss.as.server.deployment] (MSC service thread 1-7) WFLYSRV0027: Starting deployment of "test.war" (runtime-name: "test.war")
...
17:46:41,819 INFO  [org.jboss.as.server] (management-handler-thread - 1) WFLYSRV0010: Deployed "test.war" (runtime-name : "test.war")
...
17:46:42,195 INFO  [org.jboss.as.server] (management-handler-thread - 1) WFLYSRV0009: Undeployed "test.war" (runtime-name: "test.war")
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.576 s - in com.example.it.GreetingServiceTest
...
17:46:42,436 INFO  [org.jboss.as] (MSC service thread 1-8) WFLYSRV0050: WildFly Full 19.0.0.Beta2 (WildFly Core 11.0.0.Beta8) stopped in 132ms
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
[INFO] Total time:  51.375 s
[INFO] Finished at: 2020-02-23T17:46:43+08:00
[INFO] ------------------------------------------------------------------------
```

From the logging, Aquillian controller have done the following work when running the tests.

* Starting the WildFly Server.
* Packaging and deploying the archive
* Running the tests
* Undeploying the test archive
* Reporting the result,  and repeating the above steps if there are multi tests to run.
* Finally Stopping the WildFly Server



### Add WildFly Remote Container Adapter 

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



### Add WildFly Embedded Container Adapter 

Open the *pom.xml* file, check the **arq-wildfly-embedded** profile.

```xml
<profile>
    <!-- An optional Arquillian testing profile that executes tests in your
                WildFly instance -->
    <!-- Run with: mvn clean test -Parq-wildfly-embedded -->
    <id>arq-wildfly-embedded</id>
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
            <artifactId>wildfly-arquillian-container-embedded</artifactId>
            <version>${wildfly-arquillian.version}</version>
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
                                    <groupId>org.wildfly</groupId>
                                    <artifactId>wildfly-dist</artifactId>
                                    <version>${wildfly.version}</version>
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
                <artifactId>maven-failsafe-plugin</artifactId>
                <version>${maven-failsafe-plugin.version}</version>
                <configuration>
                    <environmentVariables>
                        <JBOSS_HOME>${project.build.directory}/wildfly-${wildfly.version}</JBOSS_HOME>
                    </environmentVariables>
                    <systemPropertyVariables>
                        <java.util.logging.manager>org.jboss.logmanager.LogManager</java.util.logging.manager>
                    </systemPropertyVariables>
                </configuration>
            </plugin>
        </plugins>
    </build>
</profile>
```

It adds a `wildfly-arquillian-container-embedded` dependency instead, other config is similar with the **arq-wildfly-managed** profile.

Run the tests.

```bash
mvn clean verify -Parq-wildfly-embedded
```



## Open Liberty Server

 The [Liberty Arquillian](https://github.com/Open Liberty/liberty-arquillian) project provides [Managed Container Adapter and Remote Container Adapter](https://github.com/Open Liberty/liberty-arquillian).



### Add Open Liberty Managed Container Adapter 

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

  
### Add Open Liberty Remote Container Adapter

Open the *pom.xml* file, check the **arq-liberty-remote** profile.

```xml
<profile>
    <!-- Run with: mvn clean test -Parq-liberty-remote -->
    <id>arq-liberty-remote</id>
    <properties>
        <skipTests>false</skipTests>
    </properties>
    <dependencies>
        <dependency>
            <groupId>io.openliberty.arquillian</groupId>
            <artifactId>arquillian-liberty-remote</artifactId>
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
                <directory>src/test/arq-liberty-remote</directory>
                <includes>
                    <include>*</include>
                </includes>
                <excludes>
                    <exclude>server.xml</exclude>
                </excludes>
            </testResource>
        </testResources>
        <plugins>
            <plugin>
                <artifactId>maven-failsafe-plugin</artifactId>
                <version>${maven-failsafe-plugin.version}</version>
                <configuration>
                    <systemPropertyVariables>
                        <arquillian.launch>liberty-remote</arquillian.launch>
                        <java.util.logging.config.file>${project.build.testOutputDirectory}/logging.properties
                        </java.util.logging.config.file>
                    </systemPropertyVariables>
                </configuration>
            </plugin>
        </plugins>
    </build>
</profile>
```

The following is the content of the *arquillian.xml*.

```xml
<container qualifier="liberty-remote">
    <configuration>
        <property name="hostName">localhost</property>
        <property name="serverName">defaultServer</property>
        <property name="username">admin</property>
        <property name="password">admin</property>
        <property name="httpPort">9080</property>
        <property name="httpsPort">9443</property>
    </configuration>
</container>
```

To run the tests, you need a running Open Liberty server, and allow our Arquillian controller to connect it.

Create a server profile by `server` command in the Open Liberty dist, or copy *server.xml* from *src/test/arq-liberty-remote*  to *Open Liberty_dir/usr/servers/defaultServer*.

The content of our sample server.xml.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<server description="new server">

    <!-- Enable features -->
    <featureManager>
        <feature>javaee-8.0</feature>
        <feature>restConnector-2.0</feature>
    </featureManager>
    <quickStartSecurity userName="admin" userPassword="admin" />

    <ssl id="defaultSSLConfig" trustDefaultCerts="true"/>
    <keyStore id="defaultKeyStore" password="password" />

    <applicationMonitor updateTrigger="mbean" />
    <logging consoleLogLevel="INFO" />
    <remoteFileAccess>
        <writeDir>${server.config.dir}/dropins</writeDir>
    </remoteFileAccess>
    <httpEndpoint id="defaultHttpEndpoint"
                  httpPort="9080"
                  httpsPort="9443" />
                  
    <!-- Automatically expand WAR files and EAR files -->
    <applicationManager autoExpand="true"/>

</server>
```



* The `restConnector-2.0` is required to support remote connection.
* The `quickStartSecurity` is used to setup an administrative account quickly.
* The `remoteFileAccess` allows `${server.config.dir}/dropins` is writable, and  to receive deployments.
* Set a `keyStore` and ensure the client to connect to this server securely.

Start up the Open Liberty server, it will generate the resources for the new server profile, including the keystore files in *${server.config.dir}/resources/security*.

If you run the tests, you will got a failure about authorization.

Follow these steps to import the server certification and make it trust by your client's JDK.

* Export the certification from the `jks` file in the *${server.config.dir}/resources/security* folder of Open Liberty server.
  ```bash
  keytool -export -alias default -file testwlp.crt -keystore key.jks 
  ```
* Import `.crt` into the JDK you are running the tests.
  ```
  keytool -import -trustcacerts -keystore $JAVA_HOME/jre/lib/security/cacerts -storepass changeit -alias testwlp -file testwlp.crt 
  ```

More details about the usage of `keytool` command, see [keytool documentation](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/keytool.html).

Now let's run the tests.

```bash
mvn clean verify -Parq-liberty-remote
```

