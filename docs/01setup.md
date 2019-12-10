

# Setup local development environment



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

   <img src="D:/hantsylabs/jakartaee8-starter/docs/import-nb.png" alt="Open project in NetBeans" style="zoom:80%;" />

#### Eclipse IDE

1. Click *File-> Import...* from the main menu to open the *Import* dialog.

2. Select *Maven/Existing Maven Projects* in tree nodes, and click *Next* button to continue.

3. In the  *Import Maven projects*, select root folder of the source codes.

   ![Import Maven projects](D:/hantsylabs/jakartaee8-starter/docs/import-eclipse.png)

4. Click *Finish* button to import the project into the current Eclipse workspace.


#### Intellij IDEA

1. Click *File->New->Project from Existing Sources...*. from main menu.
2. In the *Select File or Directory  to Import...* dialog, select the folder node of the source codes, click *Ok* button.
3. In the *Import project...* dialog, choose the *Import from external model* option, and then select *Maven* in list, click *Finish* button.

If it is the first time to create a Jakarta EE 8 project, it will take some time to resolve the Maven dependencies,  please be patient and wait for seconds.