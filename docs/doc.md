# Kickstart a Jakarta EE 8 Application
[Jakarta EE 8](https://jakarta.ee/) was released  at the first [Jakarta EE One](https://jakartaone.org/) online conference which was driven by Eclipse Foundation. 

Jakarta EE 8 is the first version released by Eclipse Foundation.  Compared to the previous Java EE 8, the new Jakarta EE 8 neither introduced new specifications nor updated the existing ones , the main work was moving all specifications to  Eclipse foundation, and cleaning up the issue of the licenses.  Now Jakarta EE is completely a community-driven  project, more info about Jakarta EE 8, please navigate the official [Jakarta EE website](https://jakarta.ee/).  



[TOC]

## Prerequisites

Before starting a new Jakarta EE 8 project, make sure the following software have been installed in your local system.  

### Java 8 

There are a few options available.  

* [Oracle  JDK 8](https://java.oracle.com)
* [AdoptOpenJDK](https://adoptopenjdk.net/)
* [Redhat OpenJDK ](https://developers.redhat.com)

Additionally, [Azul](https://www.azul.com/downloads/zulu-community/),  [Amazon](https://aws.amazon.com/corretto/),  [Alibaba](https://github.com/alibaba/dragonwell8),  and [Microsoft](https://docs.microsoft.com/en-us/xamarin/android/get-started/installation/openjdk)  have maintained their own OpenJDK redistribution for their products. 

Personally I prefer AdoptOpenJDK,  because it is maintained by Java community.

Optionally,  set a **JAVA\_HOME** environment variable and add *&lt;JDK installation dir>/bin* in the **PATH** environment variable.

Open your terminal, execute the following command to verify your Java environment installed successfully.

```sh
#java -version
openjdk version "1.8.0_232"
OpenJDK Runtime Environment (AdoptOpenJDK)(build 1.8.0_232-b09)
OpenJDK 64-Bit Server VM (AdoptOpenJDK)(build 25.232-b09, mixed mode)
```

> At the moment, Java 13 was released for a while. But for building a Jakarta EE 8 application, Java 8 is highly recommended.  Some Jakarta EE application servers,  such as  Glassfish is not  compatible with the latest Java 13. 



### Apache Maven 

Download a copy of  the latest of [Apache Maven 3](https://maven.apache.org) , and extract the files into your local system. Optionally, set up a **M2\_HOME** environment variable, and also do not forget to append *&lt;Maven Installation dir>/bin* your **PATH** environment variable.  

Type the following command to verify Apache Maven is working.

```shell
#mvn -v
Apache Maven 3.6.2 (40f52333136460af0dc0d7232c0dc0bcf0d9e117; 2019-08-27T23:06:16+08:00)
Maven home: D:\build\maven\bin\..
Java version: 1.8.0_232, vendor: AdoptOpenJDK, runtime: d:\JDK8\jre
Default locale: en_US, platform encoding: Cp1252
OS name: "windows 10", version: "10.0", arch: "amd64", family: "windows"
```

### Jakarta EE 8 Application Servers

In the Jakarta EE [Compatible Products](https://jakarta.ee/compatibility/) , there is a full list of application servers that are compatible with the  newest Jakarta EE 8 specification, including: 

* [Glassfish v5.1](https://projects.eclipse.org/projects/ee4j.glassfish/downloads) is an open-source Java EE/Jakarta EE application server. In the past years, it was the official Java EE reference implementation for a long time, now it is donated to Eclipse Foundation as part of Eclipse EE4J project.
* [Payara Server 193](https://www.payara.fish/software/downloads/) is a fork  of Glassfish, and provides more quickly patch fixes for  commercial support customers. 
* [Wildfly 18.0.0](https://wildfly.org/downloads) is the rebranded open-source JBoss application server  from Redhat.
* [OpenLiberty 19.0.0.9]( https://openliberty.io/downloads/) is  an open-source application server sponsored by IBM.  OpenLiberty follows a monthly-cycle release and the development is very active in the past years. 

###  Development Tools

There are several rich IDEs  or simple code editors available for coding Java.

#### [Eclipse IDE](https://eclipse.org)
If Eclipse is your preferred IDE,  to get better experience of Java EE development,  [Eclipse IDE for Enterprise Java Developers](https://www.eclipse.org/downloads/packages/release/2019-09/r/eclipse-ide-enterprise-java-developers) is highly recommended. 

Alternatively, you can select a  commercial  version, such as [Red Hat CodeReady Studio](https://www.redhat.com/en/technologies/jboss-middleware/codeready-studio) which is free for developers.

#### [Apache NetBeans IDE](http://netbeans.apache.org)
Apache NetBeans IDE is the easiest tools for building Java and Java EE/Jakarta EE applications. For those new to Java/Java EE, it is highly recommended.

#### [Intellij IDEA ](https://www.jetbrains.com/idea/)

Intellij IDEA has two versions, the free open-sourced community edition and the full-featured  commercial ultimate edition.  The community edition just contains basic features to develop Java application,  and also includes essential supports of other languages, such as Groovy, Kotlin, etc.,   the later has richer support of building enterprise applications.  

If you are a big fan of IDEA,  to work more productive, you should consider to buy a commercial  license.

#### [VisualStudio Code](https://code.visualstudio.com)

if you are stick on simple code editor for coding, VS Code is really a good choice. 

Benefit from the effort of Microsoft and Redhat, VS Code also has great Java support via [Java extension pack](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack) , you can simply install it  from [VS Code marketpalce](https://marketplace.visualstudio.com/VSCode). 



## Bootstrap a new Jakarta EE project



Firstly you can use this project as template and prepare the initial project skeleton.

### Prepare Project Skeleton

Open your browser, and navigate to [Jakarta EE 8 Starter](https://github.com/hantsy/jakartaee8-starter) page, click  the [**Use this template**](https://github.com/hantsy/jakartaee8-starter/generate) button, it will guide your to create a new repository under  your Github account, and use this project as template to initialize the repository.

After it is done, you can check out the source codes from your Github account.

```bash
git clone https://github.com/<your account>/<your jakartaee project>
```

Or check out the source codes of this project into your local system directly,  and push it back to your Github account later.

```bash
git clone https://github.com/hantsy/jakartaee8-starter
```

The project skeleton is ready, then import the source codes into your favorite IDE.

### Import the project codes into IDEs

Apache NetBeans IDE, Eclipse IDE and IntelliJ IDEA have great IDE support, and VS Code also has basic Maven support if you have instaled the Java extension pack.. 

#### Apache NetBeans IDE

 NetBeans  can recognize Maven  project directly.

1. Open  *File->Open* Project, or click the *Open Project* icon button in the toolbar,  or use *Ctrl+Shift+O* shortcuts to start up the *Open Project* dialog.

2. Select the folder of the source codes, it should be displayed as a NetBeans  Maven project icon.

   <img src="./import-nb.png" alt="Open project in NetBeans" style="zoom:80%;" />

#### Eclipse IDE

1. Click *File-> Import...* from the main menu to open the *Import* dialog.
2. Select *Maven/Existing Maven Projects* in tree nodes, and click *Next* button to continue.
3. In the  *Import Maven projects*, select root folder of the source codes.

   ![Import Maven projects](./import-eclipse.png)
   
4. Click *Finish* button to import the project into the current Eclipse workspace.


#### Intellij IDEA

1. Click *File->New->Project from Existing Sources...*. from main menu.
2. In the *Select File or Directory  to Import...* dialog, select the folder node of the source codes, click *Ok* button.
3. In the *Import project...* dialog, choose the *Import from external model* option, and then select *Maven* in list, click *Finish* button.

If it is the first time to create a Jakarta EE 8 project, it will take some time to resolve the Maven dependencies,  please be patient and wait for seconds.

### Explore the Sample Codes

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
* `GreetingService` is a simple CDI managed bean used to building a greeting message.  
*  `GreetingResource` is a simple JAX-RS resource to produce RESTful APIs. 
*  `JaxrsActivator` is the JAX-RS application class which is used to activate JAX-RS in Jakarta EE  applications.

The *src/main/java/resources/META-INF/beans.xml* is a CDI configuration file.

The *src/test/java* includes some ample codes for testing purpose.

The *src/test/java/resources/arquillian.xml*  is a [ JBoss Arquillian](http://arquillian.org/)  sample configuration file.

## Run on Application Servers

Apache NetBeans, Eclipse and Intellij IDEA have great Jakarta EE support, you can run the Jakarta EE applications in IDEs directly.

### Using Apache NetBeans IDE

NetBeans has built-in support for Glassfish and Payara server.  Let's start with Glassfish.

#### Glassfish Server

Firstly, you should add a Glassfish server instance in NetBeans.

1. Click *Windows->Services* or use *Ctrl+5* shortcuts to open *Services* view .
2. Right click  the *Servers* node, select *Add Server...* in the context menu.
3. In the *Add Server Instance* dialog, there are three steps:
   * *Choose Server* :select *Glassfish* in the server list, click *Next* button.
   * *Server Location*: select the Glassfish server location, click *Next* button.
   * *Domain name/Location*: use the default *domain1*  as domain name, click *Finish* button.

After it is done, there is a new node *Glassfish server* added under the *Server*s nodes.

<img src="./glassfish-node-nb.png" alt="Glassfish server node in Netbeans" style="zoom:80%;" />

Right click the Glassfish server node, there is a few actions available for you to control the server instance, such as Start, Stop, Debug etc. 

Let's start the Glassfish server by click *Start* in the context menu.  Wait for seconds, you will see the *Output* screen similar to the following.

<img src="./glassfish-start-output-nb.png" style="zoom:80%;" />

Switch to  *Project* view, right click the project node, and select *Run* in the context menu.

In the  *Select deployment server*, select *Glassfish server* we have created in the dropdown menu.

<img src="./run-nb.png" alt=" Select deployment server" style="zoom:80%;" />

It will try to build the project and deploy the application into the  NetBeans managed Glassfish server.   After it is deployed successfully, there is success message in the *Output* windows.

```bash
------------------------------------------------------------------------
Deploying on GlassFish Server
    profile mode: false
    debug mode: false
    force redeploy: true
In-place deployment at D:\hantsylabs\jakartaee8-starter\target\jakartaee8-starter

```

Let's switch to *Server* view, there several nodes are displayed under Glassfish servers. Expand the *Application* node, you will see there is a node *jakartaee8-starter* there.

<img src="./glassfish-node-deploy-nb.png" alt="Glassfish server node after the application is deployed" style="zoom:80%;" />

Currently the application just serves a RESTful APIs at */api/greeting* endpoints. Open a terminal and use `curl`  or Postman to test the APIs.

```bash
curl http://localhost:8080/jakartaee8-starter/api/greeting/hantsy
{"message":"Say Hello to hantsy at 2019-11-04T16:16:13.509"}
```



#### Payara Server

Payara server is derived from Glassfish project, the steps of using Payara server in NetBeans is very similar with Glassfish server. Play it yourself.

Unfortunately at the moment of writing this post, the original Wildfly plugin is not aligned with Jakarta EE 8, and it is missing in NetBeans Plugin portal,  and there is also no OpenLiberty support via NetBeans plugin. 



### Using Eclipse IDE 

Through Eclipse Marketplace, it is easy to get Glassfish, Wildfly,  OpenLiberty supports in Eclipse IDE.

#### Glassfish Server

Follow the following steps to  install [Eclipse Glassfish Tools](https://projects.eclipse.org/projects/webtools.glassfish-tools/) plugin into Eclipse IDE.

1. Open Eclipse Marketplace from *Help*-> *Eclipse Marketplace* menu. 
2. Type *Glassfish* in the search box to filter Glassfish plugins.
3. In the search result, find *Glassfish tools* ,  click the *Install* button to install it.
4. After it is installed, restart Eclipse IDE to apply the plugin.

Next let's create a Glassfish Server instance.

If the *Servers* view is not opened, try to open it from *Windows*->*Show Views*->*Servers* menu, or open the Java EE *Perspective*->*Open Perspective*-> *Other...* to find the *Java EE* perspective, it will include an open Servers for you.

Right click on the blank space in the *Servers* view, select *New*->*Server* in the context menu.

<img src="./eclipse-new-server.png" alt="Eclipse new Glassfish server" style="zoom:80%;" />

 

In the *New Server* wizard,  select *Glassfish*  in the *Define a New Server* step, then click *Next* button.

<img src="./eclipse-new-server2.png" alt="Eclipse new Glassfish Server" style="zoom:80%;" />

In the *Glassfish runtime properties* dialog, select the Glassfish location, and choose a JDK 8 to run Glassfish, click *Next* button.

<img src="./eclipse-new-server3.png" alt="Eclipse new Glassfish server 3" style="zoom:80%;" />

In the *Glassfish Application Server properties* step, use the default values, click *Finish* button. 

There is a new node will be appeared in the *Servers* view.

<img src="./eclipse-new-server4.png" alt="Eclipse new Glassfish node" style="zoom:80%;" />

It is easy to control the applcation server in the Servers view, such as start, stop, restart, deploy and undeploy etc. 

Right click the *Glassfish* node, and select *Start* to start Glassfish server.  After it is  started successfully, under the Glassfish node, it will include the resources in the Glassfish server. 

<img src="./eclipse-new-server-running.png" alt="Eclipse new Glassfish node" style="zoom:80%;" />

Ok, let's try to run the application on Glassish server.

In the *Project* or *Packages* view, right click the project node, and click *Run As...*-> *Run on Server*.

<img src="./eclipse-run-on-server.png" alt="Eclipse run on Server-Glassfish" style="zoom:80%;" />

In the *Run on Server* dialog, select *Glassfish*, and click *Finish* button. Wait for seconds, it will build, package and deploy the application into Glassfish server. When it is done, you will see there is a *Deployed Applications* under the Glassfish node in the *Servers* view. Expand this node, there is a *jakartaee8-starter* node, it is our application.

<img src="./eclipse-new-server-running2.png" alt="Eclipes run on Servers-Glassfish deployed app" style="zoom:80%;" />

Open your terminal, try to access the sample endpoint `api/greeting/{name}` via `curl` command.

```bash 
curl http://localhost:8080/jakartaee8-starter/api/greeting/hantsy
{"message":"Say Hello to hantsy at 2019-12-03T14:05:28.437"}
```

To undeploy  the application, in *Servers* view, expand *Glassfish 5*->*Glassfish Management*->*Deployed Applications*,  right click *jakartaee8-starter*, click *Undeploy* in the context menu.

Or right click  *Glassfish 5*-> *jakartaee8-starter*,  click *Remove*  in the context menu.

#### Wildfly

If you are using  the official Eclipse IDE, you should install [Redhat CodeReady Studio](https://developers.redhat.com/products/codeready-studio/overview)  to get Wildfly  server support. 

Follow the following steps to  install Redhat CodeReady Studio plugin into Eclipse IDE.

1. Open Eclipse Marketplace from *Help*-> *Eclipse Marketplace* menu. 
2. Click the *Redhat* icon in the bottom *Marketplaces* area of *Eclipse Marketplace* dialog to switch to *Redhat* marketplace.
3.  Select *Redhat CodeReady Studio* ,  click the *Install* button to install it.
4. After it is installed, restart Eclipse IDE to apply the plugin.

Next let's create a Wildfly Server instance.

Right click on the blank area in the *Servers* view, select *New*->*Server* in the context menu.

<img src="./eclipse-new-wildfly1.png" alt="Eclipse  new Server Wildfly " style="zoom:80%;" />



Expand *JBoss Community* node in the tree list, select Wildfly 18, click *Next*  button.

<img src="./eclipse-new-wildfly2.png" alt="Eclipse  new Server Wildfly " style="zoom:80%;" />

In the  *Create a new Server Adapter* step, use the default selections, click *Next* button.

<img src="./eclipse-new-wildfly3.png" alt="Eclipse  new Server Wildfly " style="zoom:80%;" />

In the *JBoss Runtime*, select the Wildfly server location in your system, click *Finish* button. 

After it is done, there is a Wildfly instance in the  *Servers* view.

<img src="./eclipse-new-wildfly4.png" alt="Eclipse  new Server Wildfly " style="zoom:80%;" />

Ok, let's try to run the application on Wildfly server.

In the *Project* or *Packages* view, right click the project node, and click *Run As...*-> *Run on Server*.

![Eclipse run on Server-Wildfly](./eclipse-wildfly-run-on-server.png)

If the Wildlfy is not started, it will start the server firstly, then build, package and deploy the application into the Wildfly Server.

After it is started , open your terminal, try to access the sample endpoint `api/greeting/{name}` via `curl` command.

```bash 
curl http://localhost:8080/jakartaee8-starter/api/greeting/hantsy
{"message":"Say Hello to hantsy at 2019-12-03T16:34:59.095"}
```

To undeploy the application, just right click the *jakartaee8-starter* node under the *Wildfly* instance node in the *Servers* view, and click *Remove* in the context menu. It will start undeploying the application, you can see the progress in the *Console* view.

```bash
16:36:13,619 INFO  [org.wildfly.extension.undertow] (ServerService Thread Pool -- 8) WFLYUT0022: Unregistered web context: '/jakartaee8-starter' from server 'default-server'
16:36:13,670 INFO  [org.jboss.as.server.deployment] (MSC service thread 1-5) WFLYSRV0028: Stopped deployment jakartaee8-starter.war (runtime-name: jakartaee8-starter.war) in 80ms
16:36:13,758 INFO  [org.jboss.as.server] (DeploymentScanner-threads - 1) WFLYSRV0009: Undeployed "jakartaee8-starter.war" (runtime-name: "jakartaee8-starter.war")
```



#### Open Liberty

To manage Open Liberty server in Eclipse IDE, you should install Open Liberty Developer tools pluign.

Follow the following steps to  install Open Liberty Developer Tools  plugin into Eclipse IDE.

1. Open Eclipse Marketplace from *Help*-> *Eclipse Marketplace* menu. 
2. Type *Liberty* in the search box and hit *Enter* to search it.
3. In the result list, find  *IBM Liberty Developer Tools* ,  then click *Install* button to start the installation.
4. After it is installed, restart Eclipse IDE to apply the plugin.

Next let's create an  OpenLiberty Server instance.

Right click on the blank area in the *Servers* view, select *New*->*Server* in the context menu.

<img src="./eclipse-new-liberty1.png" alt="Eclipse  new Server Liberty " style="zoom:80%;" />


Expand *IBM* node in the tree list, select *Liberty*, and click *Next*  button.

<img src="./eclipse-new-liberty2.png" alt="Eclipse  new Server Liberty " style="zoom:80%;" />

In the  *Liberty Runtime Environmentr* step,  set the Liberty location,  click *Next* button.

<img src="./eclipse-new-liberty2.png" alt="Eclipse  new Server Liberty " style="zoom:80%;" />

In the *New Liberty Server*,  there is a ** template** field, make sure  *javaee-8.0* is selected, others use the default values, click *Finish* button. 

After it is done, there is a Liberty Server instance in the  *Servers* view.

<img src="./eclipse-new-liberty4.png" alt="Eclipse  new Server Liberty " style="zoom:80%;" />

Ok, let's try to run the application on Open Liberty server.

In the *Project* or *Packages* view, right click the project node, and click *Run As...*-> *Run on Server*.

![Eclipse run on Server-Liberty](./eclipse-liberty-run-on-server.png)

If the Liberty server is not started, it will start the server firstly, then build, package and deploy the application into the Liberty Server.

If it is first time to run Liberty server, it will popup a dialog for you to setup the keystore. Set up  it as you like.

After it is started successfully, you can see the following like info in the Console view.

```bash
[AUDIT   ] CWPKI0803A: SSL certificate created in 4.902 seconds. SSL key file: D:/appsvr/wlp/usr/servers/defaultServer/resources/security/key.p12
[AUDIT   ] CWWKI0001I: The CORBA name server is now available at corbaloc:iiop:localhost:2809/NameService.
[AUDIT   ] CWWKT0016I: Web application available (default_host): http://localhost:9080/jakartaee8-starter/
[AUDIT   ] CWWKZ0001I: Application jakartaee8-starter started in 2.888 seconds.
[AUDIT   ] CWWKF0012I: The server installed the following features: [appClientSupport-1.0, appSecurity-2.0, appSecurity-3.0, batch-1.0, beanValidation-2.0, cdi-2.0, concurrent-1.0, distributedMap-1.0, ejb-3.2, ejbHome-3.2, ejbLite-3.2, ejbPersistentTimer-3.2, ejbRemote-3.2, el-3.0, j2eeManagement-1.1, jacc-1.5, jaspic-1.1, javaMail-1.6, javaee-8.0, jaxb-2.2, jaxrs-2.1, jaxrsClient-2.1, jaxws-2.2, jca-1.7, jcaInboundSecurity-1.0, jdbc-4.2, jms-2.0, jndi-1.0, jpa-2.2, jpaContainer-2.2, jsf-2.3, jsonb-1.0, jsonp-1.1, jsp-2.3, localConnector-1.0, managedBeans-1.0, mdb-3.2, servlet-4.0, ssl-1.0, wasJmsClient-2.0, wasJmsSecurity-1.0, wasJmsServer-1.0, webProfile-8.0, websocket-1.1].
[AUDIT   ] CWWKF0011I: The defaultServer server is ready to run a smarter planet. The defaultServer server started in 54.255 seconds.
```

Now open your terminal, try to access the sample endpoint `api/greeting/{name}` via `curl` command.


```bash 
curl http://localhost:9080/jakartaee8-starter/api/greeting/hantsy
{"message":"Say Hello to hantsy at 2019-12-03T21:34:00.033"}
```
>Note, Liberty Server uses 9080 as port by default.

To undeploy the application, just right click the *jakartaee8-starter* node under the *Liberty Server* instance node in the *Servers* view, and click *Remove* in the context menu. It will start undeploying the application, you can see the progress in the *Console* view.

```bash
[AUDIT   ] CWWKG0016I: Starting server configuration update.
[AUDIT   ] CWWKG0017I: The server configuration was successfully updated in 0.800 seconds.
[AUDIT   ] CWWKT0017I: Web application removed (default_host): http://localhost:9080/jakartaee8-starter/
[AUDIT   ] CWWKZ0009I: The application jakartaee8-starter has stopped successfully.
```


### Using Maven CLI



## Testing Jakarta EE Applications



## Put Your Application to Production

## Resources 

* [WildFly Maven Plugin (wildfly-maven-plugin)](https://docs.jboss.org/wildfly/plugins/maven/latest/index.html)
* [Deploying to Payara Server Using the Maven Cargo Plugin](https://blog.payara.fish/deploying-to-payara-server-using-the-maven-cargo-plugin)  by Payara Blog
* [Cargo Maven2 Plugin for Glassfish v5](https://codehaus-cargo.github.io/cargo/GlassFish+5.x.html)