# Kickstart a Jakarta EE 8 Application
 [Jakarta EE 8 starter](https://github.com/hantsy/jakartaee8-starter) is a boilerplate project which allow you to start up a new  Java EE 8/Jakarta EE 8 application in seconds.

Jakarta EE 8 is the first version released by Eclipse Foundation. 

## Prerequisites

Before start the project , I assume the following software have been installed into your local machine.

* The latest JDK 8, you can choose [Oracle JDK 8](https://java.oracle.com) or community-driven [AdoptOpenJDK 8](https://adoptopenjdk.net/releases.html) .

* One of the following Jakarta EE 8 compatible products to serve your application:

  * [Glassfish v5.1](Prerequisites)
  * [Payara Server 193 Full](https://www.payara.fish/software/downloads/)
  * [Wildfly 17.0.1](https://wildfly.org/downloads)
  * [OpenLiberty 19.0.0.9]( https://openliberty.io/downloads/)

* Your favorite IDEs, not limited to:
  * [Eclipse IDE](https://eclipse.org)
  * [Apache NetBeans IDE](http://netbeans.apache.org)
  * [Intellij IDEA ](https://www.jetbrains.net)

## Create a new project

You can check out the source codes into your local system directly via `git clone`.

```bash
git clone https://github.com/hantsy/jakartaee8-starter
```

And add your codes.

Or  click the [**Use this template**](https://github.com/hantsy/jakartaee8-starter/generate) button to use this project as template  and generate a new repository under  your Github account, then you can start your new project freely.

## Explore the project codes

The project file structure looks like.

```bash
├── .github
│   ├── ISSUE_TEMPLATE
│   │   └── bug_report.md
│   └── workflows
│       └── maven.yml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           ├── GreetingMessage.java
│   │   │           ├── GreetingResource.java
│   │   │           ├── GreetingService.java
│   │   │           └── JaxrsActivator.java
│   │   └── resources
│   │       └── META-INF
│   │           └── beans.xml
│   └── test
│       ├── java
│       │   └── com
│       │       └── example
│       │           ├── it
│       │           │   ├── GreetingResourceTest.java
│       │           │   └── GreetingServiceTest.java
│       │           └── GreetingMessageTest.java
│       └── resources
│           └── arquillian.xml
├── .gitignore
├── LICENSE
├── pom.xml
└── README.md

```

The **.github** folder holds the Github specific configurations, eg, issue templates, Github Actions workflows. 

The *src/main/java* includes some sample codes,  `GreetingMessage` is a simple POJO presenting a greeting message.  `GreetingService` is a simple CDI managed bean used to building a greeting message.  `GreetingResource` is a simple JAX-RS resource to expose RESTful APIs.  `JaxrsActivator` is the JAX-RS application class which is used to activate JAX-RS in Jakarta EE  applications.

The *src/main/java/resources/META-INF/beans.xml* is a CDI configuration file.

The *src/test/java* includes some testing sample codes.

The *src/test/java/resources/arquillian.xml*  is a sample [Arquillian](http://arquillian.org/) configuration file.



## Resources 

* [WildFly Maven Plugin (wildfly-maven-plugin)](https://docs.jboss.org/wildfly/plugins/maven/latest/index.html)
* [Deploying to Payara Server Using the Maven Cargo Plugin](https://blog.payara.fish/deploying-to-payara-server-using-the-maven-cargo-plugin)  by Payara Blog
* [Cargo Maven2 Plugin for Glassfish v5](https://codehaus-cargo.github.io/cargo/GlassFish+5.x.html)