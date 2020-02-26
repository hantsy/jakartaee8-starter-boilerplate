# Deploying applications to Open Liberty Server - The Hard Way

Download Open Liberty [from openliberty.io download page](https://openliberty.io/downloads/), and extract files into your local system.



## Create a Server profile

Unlike Glassfish or WildFly server, Open Liberty does not provide a default domain. 

Locate to *&lt;Open Liberty dif>/bin*, execute the following command to create a new server profile.

```bash
server create
Server defaultServer created.
```
Open  *&lt;Open Liberty dif>/usr/servers*, there is a new *defaultServer* folder created. In this folder, there are some files and folder are generated for your application deployment.

Open the *server.xml* file, it already included *javaee-8.0* feature. There are several templates allow you create a server profile quickly with essential features, more templates check  *&lt;Open Liberty dif>/templates/servers*.

Try to run the following command to create a **microProfile3** profile with the name *mp3* and the template *microProfile3*.

```bash
server create mp3 --template="microProfile3"
Server mp3 created.
```

## Start and Stop Open Liberty Server

In the *&lt;Open Liberty dir>/bin* folder, execute the following command to start Open Liberty Server.

```bash
# server start
Starting server defaultServer.
Server defaultServer started.
```

By default, it will start the **defaultServer**.  You can specify the server name to start a different server.

```bash
# server start mp3
Starting server mp3.
Server mp3 started.
```

To start a server with verbose mode, use the following command instead.

```bash
server run
Launching defaultServer (Open Liberty 20.0.0.1/wlp-1.0.36.cl200120200108-0300) on OpenJDK 64-Bit Server VM, version 11.0.6+10 (en_US)
[AUDIT   ] CWWKE0001I: The server defaultServer has been launched.
[WARNING ] CWWKS3103W: There are no users defined for the BasicRegistry configuration of ID com.ibm.ws.security.registry.basic.config[basic].
[AUDIT   ] CWWKZ0058I: Monitoring dropins for applications.
[AUDIT   ] CWPKI0820A: The default keystore has been created using the 'keystore_password' environment variable.
[AUDIT   ] CWWKI0001I: The CORBA name server is now available at corbaloc:iiop:localhost:2809/NameService.
[AUDIT   ] CWWKF0012I: The server installed the following features: [appClientSupport-1.0, appSecurity-2.0, appSecurity-3.0, batch-1.0, beanValidation-2.0, cdi-2.0, concurrent-1.0, distributedMap-1.0, ejb-3.2, ejbHome-3.2, ejbLite-3.2, ejbPersistentTimer-3.2, ejbRemote-3.2, el-3.0, j2eeManagement-1.1, jacc-1.5, jaspic-1.1, javaMail-1.6, javaee-8.0, jaxb-2.2, jaxrs-2.1, jaxrsClient-2.1, jaxws-2.2, jca-1.7, jcaInboundSecurity-1.0, jdbc-4.2, jms-2.0, jndi-1.0, jpa-2.2, jpaContainer-2.2, jsf-2.3, jsonb-1.0, jsonp-1.1, jsp-2.3, managedBeans-1.0, mdb-3.2, servlet-4.0, ssl-1.0, wasJmsClient-2.0, wasJmsSecurity-1.0, wasJmsServer-1.0, webProfile-8.0, websocket-1.1].
[AUDIT   ] CWWKF0011I: The defaultServer server is ready to run a smarter planet. The defaultServer server started in 9.205 seconds.
```



To stop a running Open Liberty Server,  execute the following command in another terminal window.

```bash
# server stop
Stopping server defaultServer.
Server defaultServer stopped.
```



## Deploy applications

Firstly build the project and package it into a war package.
```bash
# mvn clean package
...
[INFO] --- maven-war-plugin:2.2:war (default-war) @ jakartaee8-starter ---
...
[INFO] Building war: D:\hantsylabs\jakartaee8-starter\target\jakartaee8-starter.war
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  12.449 s
[INFO] Finished at: 2020-02-18T17:50:43+08:00
[INFO] ------------------------------------------------------------------------
```
After it is done there is a *jakartaee8-starter.war* is located in the *target* folder.

Copy this file to *&lt;Open Liberty dir>/usr/servers/defaultServer/dropins*.

In the Open Liberty console, you will see the following deployment info.

```bash
[AUDIT   ] CWWKT0016I: Web application available (default_host): http://localhost:9080/jakartaee8-starter/
[AUDIT   ] CWWKZ0001I: Application jakartaee8-starter started in 2.902 seconds.
```

To undeploy an applicaiton, just delete *jakartaee8-starter.war* in the  *&lt;Open Liberty dir>/usr/servers/defaultServer/dropins/*.

In the Open Liberty console, it displays the undeployment progress.

```bash
[AUDIT   ] CWWKT0017I: Web application removed (default_host): http://localhost:9080/jakartaee8-starter/
[AUDIT   ] CWWKZ0009I: The application jakartaee8-starter has stopped successfully.
```

