# Testing Jakarta EE applications

Generally, in a Jakarta EE application, there are some tools can be used for testing Jakarta EE components.

* Use [JUnit](https://junit.org/) or [TestNG](https://testng.org/) to test simple POJOs.
* Use [Mockito](https://site.mockito.org/) like mock framework to isolate the dependencies  of a component in tests.
* Test Jakarta EE components in a real world environment with [JBoss Arquillian](https://arquillian.org).

Besides, there are some existing extensions to help for improving testing productivity.

* [AssertJ](https://assertj.github.io) includes a collection of fluent assertions.
* [RestAssured](http://rest-assured.io/) provides BDD like behaviors for testing RESTful APIs.
* [JsonPath](https://github.com/json-path/JsonPath) provides XPath like query for JSON.
* [awaitility](https://github.com/awaitility/awaitility) provides assertions for async invocation.

In this post, we will focus on testing Jakarta EE components on the following Jakarta EE 8 compatible application servers with [JBoss Arquillian](https://arquillian.org).

* [Glassfish Server](./04test-arq-glassfish.md)
* [Payara  Server](./04test-arq-payara.md)
* [WildFly  Server](./04test-arq-wildfly.md)
* [Open Liberty  Server](./04test-arq-openliberty.md)

