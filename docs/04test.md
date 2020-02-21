# Testing Jakarta EE applications

Generally, in a Jakarta EE applications, there are some approaches to test the components.

* Use [JUnit](https://junit.org/) or [TestNG](https://testng.org/) to test simple POJOs.
* Use [Mockito](https://site.mockito.org/) like mock framework to mock a dependent part and test components in an isolated environment with an existing test runner.
* Test Jakarta EE components in a real world environment with [JBoss Arquillian](https://arquillian.org).

In this post, we will focus on writing test with [JBoss Arquillian](https://arquillian.org).

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

We also add another dependency `junit4` here. Arquillian also supports for TestNG,  we use JUnit as a test runner here.

> Note: JUnit 5 had been released for a while, but Arquillian does not support JUnit 5 at the moment, see Arquillian [issue#137](https://github.com/arquillian/arquillian-core/issues/137).

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

