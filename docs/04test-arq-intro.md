# Getting started with JBoss Arquillian

As described in the Mission page of [JBoss Arquillian](https://arquillian.org) website:

> Arquillian is an innovative and highly extensible testing platform for  the JVM that enables developers to easily create automated integration,  functional and acceptance tests for Java middleware.

Especially,  Arquillian is responsible of all tedious work when testing applications in a container including container management, deployment and framework  initialization so you can focus on writing your tests. 

If you are new to Arquillian ,  the official [guides](http://arquillian.org/guides/) is a good start point to go through JBoss Arquillian.

## Adding Arquillian Dependencies

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

The `arquillian-bom` is a Maven  BOM(*Bill of Materials*)  which manages a collection of arquillian core dependencies.

We also add another dependency `junit` here. Arquillian supports TestNG as well.  But we use JUnit as an example in our [jakartaee8-starter](https://github.com/hantsy/jakartaee8-starter).

> Note: JUnit 5 had been released for a while, but Arquillian does not support JUnit 5 at the moment, see issue [arquillian/arquillian-core#137](https://github.com/arquillian/arquillian-core/issues/137).

In the `dependencies` section, declare the following two dependencies.

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

##  Creating your first Arquillian Test

Let's have a look at `GreetingServiceTest`, which is a simple Arquillian test to verify the functionality of a CDI bean ( `GreetingService`). 

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

As you see, an Arquillian test consists of the following components.

* A test class is annotated with `@RunWith(Arquillian.class)` which indicates to use an Arquillian specific test runner to run this test.
* Inside the test class, a *public static* method annotated with `@Deployment` annotation is available to  create a deployment unit for this test.
* A `@Test` annotated method (*should_create_greeting()*) is to verify the functionality of your JakaraEE components(`GreetingService` ) . Arquillian enriched tests and it allows you to  inject CDI beans in tests like in general Jakarta EE managed beans. In the above example, `GreetingService`  bean can be injected in test class like a regular CDI bean.

When running the test, it will perform a series of tasks in sequence to ensure the tests will be run on successfully application servers .

1.  First of all, it will create a deployable archive as described in the `@Deployment` annotated method. In this sample test, it just need to package `GreetingMessage`, `GreetingService`, and a CDI  `beans.xml` descriptor(which is optional since Java EE 7) into a `JavaArchive`. 
2. Then it will try to deploy the archive into a target container. If it runs against a managed adapter or an embedded adapter and the server is not running, it will start it firstly.
3. Then run the tests in the container,  and capture the test report and send back to the test runner.
4. Finally undeploy the test archive, and optionally stop the server if the tests runs against a managed adapter.

Let's move to another sample test in the [Jakarta EE 8 starter repository](https://github.com/hantsy/jakartaee8-starter).

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

* We create a `WebArchive` instead of `JavaArchive`, because this is for testing the functionality of JAX-RS resources.
* A `testable = false` property is added to `@Deployment` annotation, thus the test case will run as *client mode*.  That means the test method `should_create_greeting()` will run on a different JVM process, and resources in the archive, such as `GreetingService` can not be injected in this test.
* You can get the the test application context path from a ` @ArquillianResource` annotated URI after it is deployed.

To run Arquillian tests on a certain application server you have to configure an *Aquillian Container Adapter*.

In the Maven [*pom.xml*](https://github.com/hantsy/jakartaee8-starter/blob/master/pom.xml), a couple of Maven profiles are configured for applying different container adapters.