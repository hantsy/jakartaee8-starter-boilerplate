#  Jakarta EE 8 Starter Boilerplate 

![compile and build](https://github.com/hantsy/jakartaee8-starter/workflows/build/badge.svg)

![Dockerize applications](https://github.com/hantsy/jakartaee8-starter/workflows/dockerize/badge.svg)

![Integration Test with Arquillian Glassfish Embedded Container](https://github.com/hantsy/jakartaee8-starter/workflows/it-with-arq-glassfish-embedded/badge.svg)
![Integration Test with Arquillian Glassfish Embedded Container](https://github.com/hantsy/jakartaee8-starter/workflows/it-with-arq-glassfish-managed/badge.svg)

![Integration Test with Arquillian OpenLiberty Managed Container](https://github.com/hantsy/jakartaee8-starter/workflows/it-with-arq-liberty-managed/badge.svg)

![Integration Test with Arquillian Payara Embedded Container](https://github.com/hantsy/jakartaee8-starter/workflows/it-with-arq-payara-embedded/badge.svg)
![Integration Test with Arquillian Payara Managed Container](https://github.com/hantsy/jakartaee8-starter/workflows/it-with-arq-payara-managed/badge.svg)
![Integration Test with Arquillian Payara Micro Container](https://github.com/hantsy/jakartaee8-starter/workflows/it-with-arq-payara-micro/badge.svg)

![Integration Test with Arquillian WildFly Embedded Container](https://github.com/hantsy/jakartaee8-starter/workflows/it-with-arq-wildfly-embedded/badge.svg)
![Integration Test with Arquillian WildFly Managed Container](https://github.com/hantsy/jakartaee8-starter/workflows/it-with-arq-wildfly-managed/badge.svg)


## What is it?

 [Jakarta EE 8 starter](https://github.com/hantsy/jakartaee8-starter) is a boilerplate project to help you to bootstrap a new  Java EE 8/Jakarta EE 8 application in seconds.

If you want to explore the real world Jakarta EE 8 applications based on JSF, JAX-RS and MVC, check the following projects which are derived from this project.

* [Jakarta Faces Sample](https://github.com/hantsy/jakartaee-faces-sample)
* [Jakarta MVC  Sample](https://github.com/hantsy/jakartaee-mvc-sample)
* [Jakarta Jaxrs  Sample](https://github.com/hantsy/jakartaee-jaxrs-sample)


## Build

Make sure you have installed the latest JDK 8 and Apache Maven 3.6.

Execute the following command to build a clean package locally.

```bash
mvn clean package
```
More details of testing and further deployments on application severs, check the [docs](https://hantsy.github.io/jakartaee8-starter-boilerplate/).

## Docs

There is  [a comprehensive setup guide](https://hantsy.github.io/jakartaee8-starter-boilerplate/) available for the Jakarta EE newbies, including:

1. Setup local development environment with the popular IDEs, such as:
   * Apache NetBeans IDE
   * Eclipse IDE
   * IntelliJ IDEA.
2. Deploy and Run application on the popular application servers, such as:
   * Glassfish
   * WildFly
   * Open Liberty
3. Testing Jakarta components using Junit and JBoss Arquillian.
4. Publish application into production environment.


## Resources 

* [WildFly Maven Plugin (wildfly-maven-plugin)](https://docs.jboss.org/wildfly/plugins/maven/latest/index.html)
* [Deploying to Payara Server Using the Maven Cargo Plugin](https://blog.payara.fish/deploying-to-payara-server-using-the-maven-cargo-plugin)  by Payara Blog
* [Cargo Maven2 Plugin for Glassfish v5](https://codehaus-cargo.github.io/cargo/GlassFish+5.x.html)
* [Video course on efficient enterprise testing](https://blog.sebastian-daschner.com/entries/efficient-testing-video-course)
* [Testing Java EE 7 Applications in Docker with Arquillian-Cube](https://blogs.oracle.com/developers/testing-java-ee-7-applications-in-docker-with-arquillian-cube)
* [Testing HTML and JSF-Based UIs with Arquillian](https://blogs.oracle.com/javamagazine/testing-html-and-jsf-based-uis-with-arquillian)
* [Jakarta EE & Wildfly Running on Kubernetes](https://dzone.com/articles/jakarta-ee-amp-wildfly-running-on-kubernetes#)
* [Building Self-Contained and Configurable Java EE Applications](http://dplatz.de/blog/2018/self-contained-jee-app.html)
* [Switching between data sources when using @DataSourceDefinition](http://jdevelopment.nl/switching-data-sources-datasourcedefinition/)
* [The state of @DataSourceDefinition in Java EE](https://henk53.wordpress.com/2012/06/30/the-state-of-datasourcedefinition-in-java-ee/)
