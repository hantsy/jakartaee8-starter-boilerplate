# An introduction to Jakarta EE

[toc]



## The history of Java EE/Jakarta EE

Firstly let's review Java EE and Jakarta EE.

### Leading enterprise application development

Originally Java EE was designated  as an extension of Java SE and  targeted at  enterprise applications.  Java EE was defined by a collection of specifications and managed by JCP, all Java EE compatible products have to pass the TCK tests provided by JCP.

The first version of  [J2EE was released at December 12, 1999][1], aligned with J2SE 1.2, and   J2EE 1.3 was out at September 24, 2001, and J2EE 1.4 entered public on November 11, 2003.

Yeah,  the naming convention is a little confused if you are new to Java world. Before Java EE 5, it was called J2EE.  In 2004, Java 5  was released and brought significant features. To highlighten the big changes, Sun micorsystem decided to rename its Java product lines.   In 2006, Java EE 5 was released, the name was changed from J2EE to Java EE, and  the version was jumped from 1.4 to 5. 

There were a few Java EE certificated products occurred in this period.

* Sun Application Server (it is the open-source Eclipse Glassfish now)
* IBM Websphere
* BEA Weblogic( BEA was acquired by Oracle)
* Oracle  Oc4j(it was abandoned By Oracle after acquired BEA)
* JBoss Application Server (JBoss company was acquired by Redhat, and JBoss community was live for years, and at last JBoss AS was renamed to WildFly. Now Redhat is a division of IBM)
* Kingdee(金蝶) Apusic
* SAP NetWeaver
* JOnAS(an open source application server, it is not active now)
* Borland Application Server

To get the full list of Java EE application servers and their current status, check [Wikipeda's Java EE page](https://en.wikipedia.org/wiki/Java_Platform,_Enterprise_Edition).

### Java EE and Java communities

We have to mention Spring here.  In 2002,  Rod Johnson, the author of Spring wrote down a book named **J2EE development without EJB**. In his book, he listed lots of problems when developing enterprise application with J2EE, and explained how to resolve them with simple POJOs.  Based on the source codes of this book, a new framework named Spring was born in 2003.

At the same time, Gavin King started his project Hibernate to replace the tedious work in  EJB development. 

The Spring and Hibernate pair was considered as a competitor of Java EE, esp. EJB 2. A lot of developers started new projects with Spring instead of J2EE platform. I started my first Spring project in 2004.

Ok, let's back to Java EE 5 which was released on  May 11, 2006. Benefited from the  promotion of  Java 5 programming  language, esp. the new features introduced in Java 5, such as generics, annotations, etc. the programming is simplified,  Java EE 5 was considered as a big milestone. In this version,  a new specification- Java Persistence API was added to replace the  legacy EJB 2.x Entity Bean. As you know,  JPA 1.0 concept is heavily derived from Hibernate and other ORM providers, such as Oracle TopLinks(now it is EclipseLinks),  BEA Kodo(now it is Apache OpenJPA).  

 Java EE 5 really simplified Java EE development. And EJB 3.0 appeared as a new programming model  and gained much praise from Java communities. JBoss released a  new framework - Seam which provided better development experience to  Java EE and made all specs work seamlessly, the most attractive features included an united managed bean model, bi-direction dependency injection mechanism , event driven mechanism, etc.

The evolutional concept in Seam became the new specification - Context and Dependency Injection for Java EE (JSR299) and finally joined the later Java EE 6. When Java EE 6 was close to be finalized, Google and SpringSource worked together and drafted a new specification -  Dependency Injection for Java (JSR330) which tried to provide a common dependency injection mechanism for Java SE. Spring framework and Google Guice embraced JSR330 soon in their new version. 

Java EE 6 was finally released on December 10, 2009.

### Java EE is dead?

Java EE  7 was finalized on May 28, 2013.  Java EE brought a lots of improvement to increase developer productivity. 

The are four and new specifications introduced in Java EE 7 :

- Java WebSocket API 1.0 (JSR 356)
- Java API for JSON Processing 1.0 (JSR 353)
- Concurrency Utilities 1.0 (JSR 236)
- Batch Applications for Java Platform 1.0 (JSR 352)

 Here is the revamped specification list:

- Java Message Service API 2.0 (major revamp for the first time in 10 years)
- JAX-RS 2.0 (RESTFul API)
- Enterprise JavaBeans 3.2
- Java Persistence API 2.1
- Contexts And Dependency Injection 1.1
- JavaServer Faces 2.2
- Java Servlet 3,1
- Interceptors 1.2
- Bean Validation 1.1

Since 2013, cloud service and microservice became more and more popular. Java EE had to embrace the changes, so a lot of perspectives were proposed to be brought into Java EE 8, including Configuration, Load Balance, Circuit  breaker, Service Registry and Discovery, programmatic Security API, MVC  etc.

But the road to Java EE 8 was not straight,  the development work of some specifications were paused for a long time, most of proposed specifications were moved out of Java EE 8 finally.

To save Java EE, the [Java Guardians community](https://javaee-guardians.io/) created a [petition](https://www.change.org/p/larry-ellison-tell-oracle-to-move-forward-java-ee-as-a-critical-part-of-the-global-it-industry) and wished Oracle move Java EE forward more quickly.

At the same time, IBM, Redhat and other Java communities launched a new [MicroProfile](http://microprofile.io) which targets lightweight Java EE and cloud native applications. Now it is a project under Eclipse Foundation.

Although the Java EE 8 way is a little hard, finally it is released to the public. 

Only two new specifications were introduced in Java EE 8.

* JSR 375 – Java EE Security API 1.0
* JSR 367 – The Java API for JSON Binding (JSON-B) 1.0

Some specifications have been updated to align with Java 8 and CDI or involved as a maintenance release.

* JSR 365 – Contexts and Dependency Injection (CDI) 2.0
* JSR 369 – Java Servlet 4.0
* JSR 370 – Java API for RESTful Web Services (JAX-RS) 2.1
* JSR 372 – JavaServer Faces (JSF) 2.3
* JSR 374 – Java API for JSON Processing (JSON-P)1.1
* JSR 380 – Bean Validation 2.0
* JSR 250 – Common Annotations 1.3
* JSR 338 – Java Persistence 2.2
* JSR 356 – Java API for WebSocket 1.1
* JSR 919 – JavaMail 1.6

The other specifications such as JMS, Batch have no updates in this version.

Unfortunately, MVC(JSR 371) is vetoed in the final stage, but it is still existed as a community based specification. And JCache(JSR 107) which had missed the last train of Java EE 7, and also lost its attractiveness in Java EE 8.

After Java EE 8 is released, surprisingly Oracle decided to [open up Java EE progress](https://blogs.oracle.com/theaquarium/opening-up-java-ee)  and move Java EE to Eclipse Foundation.

###  Eclipse takes over the baton

[Jakarta EE 8](https://jakarta.ee/) was released  at the first [Jakarta EE One](https://jakartaone.org/) online conference which was driven by Eclipse Foundation. 

Jakarta EE 8 is the first version released by Eclipse Foundation.  Compared to the previous Java EE 8, the new Jakarta EE 8 neither introduced new specifications nor updated the existing ones , the main work was moving all specifications and related projects to Eclipse foundation, and cleaning up the issue of their licenses.  

Now Jakarta EE is  completely a community-driven  project, more info about Jakarta EE 8, please navigate the official [Jakarta EE website](https://jakarta.ee/).  

## Jakarta EE is the future

Jakarta EE 9 is on the way, the main work is helping developers to migrate to the new `jakarta.*` namespace from `java.*` and `javax.*`.

More info about Jakarta EE 9, check the [Jakarta EE 9 Release Plan]( https://eclipse-ee4j.github.io/jakartaee-platform/jakartaee9/JakartaEE9ReleasePlan). 

 

[1]: https://en.wikipedia.org/wiki/Java_Platform,_Enterprise_Edition "Java EE wikipedia"