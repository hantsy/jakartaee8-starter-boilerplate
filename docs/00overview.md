# An introduction to Jakarta EE

Firstly let's review Java EE and Jakarta EE.

## History of Jakarta EE

Originally Java EE was designated  as an extension of Java SE and  targeted at  enterprise applications. 

The first version of  [J2EE(before Java EE 5, it was called J2EE) was released at December 12, 1999][1], aligned with J2SE 1.2, and   J2EE 1.3 was out at September 24, 2001, and J2EE 1.4 entered public at November 11, 2003.

Yeah the naming version is a little confused.  Till to 2006, Java EE 5 was released, the name was changed from J2EE to Java EE, and  the version was jumped from 1.4 to 5.  

We have to mention Spring here.  In 2002,  Rod Johnson, the author of Spring wrote down a book named **J2EE development without EJB**. In his book, he listed lots of problems when developing enterprise application with J2EE, and explained how to resolve them with simple POJOs.  Based on the source codes of this book, a new framework named Spring was born in 2003.

At the same time, Gavin King started his project Hibernate to replace the tedious Entity beans in  EJB. 

The Spring and Hibernate pair was considered as a competitor of Java EE, esp. EJB 2. A lot of developers started new projects with Spring instead of J2EE platform. I started my first Spring project in 2004.

Ok, let's back to Java EE 5 which was released at May 11, 2006. Benefited from the  promotion of  Java 5 programming  language, esp. the new features introduced in Java 5, such as generics, annotations, etc. the programming is simplified,  Java EE 5 was considered as a big milestone. In this version,  new specification- Java Persistence API was added to replace the  legacy EJB 2.x Entity Bean. As you know,  JPA 1.0 concept is heavily derived from Hibernate and other ORM providers, such as Oracle TopLinks(now EclipseLinks), .  

 Java EE 5 really simplified Java EE development. And EJB 3.0 appeared as a new programming model  and gained much praise from Java communities. JBoss released a  new framework - Seam which provided better development experience to  Java EE and made all specs work seamlessly, the most attractive features included an united managed bean model, bi-direction dependency injection mechanism , event driven mechanism, etc.

The evolutional concept in Seam became the new CDI spec (JSR299) in the later Java EE 6. Before Java EE 6 was  finalized, Google and SpringSource worked together and drafted a new specification -  Dependency Injection spec(JSR330) which is a common dependency injection spec and  worked well in Java SE. Spring framework embraced JSR330 soon in its new version. Java EE 6 was released at December 10, 2009.





[Jakarta EE 8](https://jakarta.ee/) was released  at the first [Jakarta EE One](https://jakartaone.org/) online conference which was driven by Eclipse Foundation. 

Jakarta EE 8 is the first version released by Eclipse Foundation.  Compared to the previous Java EE 8, the new Jakarta EE 8 neither introduced new specifications nor updated the existing ones , the main work was moving all specifications and related projects to  Eclipse foundation, and cleaning up the issue of their licenses.  

Now Jakarta EE is a completely a community-driven  project, more info about Jakarta EE 8, please navigate the official [Jakarta EE website](https://jakarta.ee/).  

### 

## Project file structure

Now let's take a glance at the sample codes, by default the project file structure looks like the following:

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

The *src/main/java/resources/META-INF/beans.xml* is a CDI configuration file.

The *src/test/java* includes some ample codes for testing purpose.

The *src/test/java/resources/arquillian.xml*  is a [ JBoss Arquillian](http://arquillian.org/)  sample configuration file.

[1]: https://en.wikipedia.org/wiki/Java_Platform,_Enterprise_Edition "Java EE wikipedia"