# Getting started with JBoss Arquillian



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