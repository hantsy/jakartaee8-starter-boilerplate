# Understanding Application Architecture

[toc]

## Project File Structure

After the source codes is imported into IDEs, the project file structure looks like the following:

```bash
├── .github
│   ├── ISSUE_TEMPLATE
│   │   └── bug_report.md
│   └── workflows
│       └── maven.yml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           ├── GreetingMessage.java
│   │   │           ├── GreetingResource.java
│   │   │           ├── GreetingService.java
│   │   │           └── JaxrsActivator.java
│   │   └── resources
│   │       └── META-INF
│   │           └── beans.xml
│   └── test
│       ├── java
│       │   └── com
│       │       └── example
│       │           ├── it
│       │           │   ├── GreetingResourceTest.java
│       │           │   └── GreetingServiceTest.java
│       │           └── GreetingMessageTest.java
│       └── resources
│           └── arquillian.xml
├── .gitignore
├── LICENSE
├── pom.xml
└── README.md

```

The **.github** folder holds the Github specific configurations, eg, issue templates, Github Actions workflows. 

The *src/main/java* includes some sample codes:

*  `GreetingMessage` is a simple POJO to present a greeting message.  
*  `GreetingService` is a simple CDI managed bean used to building a greeting message.  
*  `GreetingResource` is a simple JAX-RS resource to produce RESTful APIs. 
*  `JaxrsActivator` is the JAX-RS application class which is used to activate JAX-RS in Jakarta EE  applications.

The *src/main/java/resources/META-INF/beans.xml* is a CDI configuration file. CDI is activated by default since Java EE 7, so beans.xml file is optional if you do not have any extra CDI configuration.

The *src/test/java* includes some ample codes for testing purpose.  Especially, the tests under `com.example.it` package is [JBoss Arquillian](http://arquillian.org/)  *Integration Tests* , which means it will deploy into application servers and run the tests.

The *src/test/java/resources/arquillian.xml*  is a [JBoss Arquillian](http://arquillian.org/)  sample configuration file.

## Application Architecture

A traditional **3**-**tier application architecture** is a modular client-server architecture that consists of a **presentation tier**, an **application tier** and a **data tier**. The three tiers are logical, not physical, and may or may not run on the same physical server.

In our sample application, the presentation tier is not the web pages in traditional web applications, but consists of a collection of RESTful APIs. 

