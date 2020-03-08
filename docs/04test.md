# Testing Jakarta EE applications

Generally, in a Jakarta EE applications, there are some tools use for testing Jakarta EE components.

* Use [JUnit](https://junit.org/) or [TestNG](https://testng.org/) to test simple POJOs.
* Use [Mockito](https://site.mockito.org/) like mock framework to isolate the dependencies  of a component in tests.
* Test Jakarta EE components in a real world environment with [JBoss Arquillian](https://arquillian.org).

Besides, there are some extensions existed for improving testing productivity.

* [AssertJ](https://assertj.github.io) includes a collection of fluent assertions.
* [RestAssured](http://rest-assured.io/) provides BDD like behaviors for testing RESTful APIs.
* [JsonPath](https://github.com/json-path/JsonPath) provides XPath like query for JSON.
* [awaitility](https://github.com/awaitility/awaitility) provides assertions for async invocation.

In this post, we will focus on testing Jakarta EE components with [JBoss Arquillian](https://arquillian.org).
