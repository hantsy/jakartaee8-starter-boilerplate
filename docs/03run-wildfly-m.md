# Deploying applications to WildFly Server - The Hard Way

Download a copy of [WildFly](https://wildfly.org/downloads), and extract files into your local system.

## Start and Stop WildFly Server

In the *&lt;WildFly dir>/bin* folder, there are a few command tools used to manage WildFly server.

Open your terminal, enter  *&lt;WildFly dir>/bin*.

Execute the following command to start WildFly Servers in **standalone** mode.

```bash
# Linux
./standalone.sh
# windows
standalone
```

Execute the following command to start WildFly Servers in **domain** mode.

```bash
# Linux
./domain.sh
# windows
domain
```

To stop a running WildFly Server, jus terminate the process by *Ctrl+C*.

## Deploy applications

Firstly package the application into a **war** package.
```bash
# mvn clean package
...
[INFO] --- maven-war-plugin:2.2:war (default-war) @ jakartaee8-starter ---

[INFO] Packaging webapp
[INFO] Assembling webapp [jakartaee8-starter] in [D:\hantsylabs\jakartaee8-starter\target\jakartaee8-starter]
[INFO] Processing war project
[INFO] Copying webapp resources [D:\hantsylabs\jakartaee8-starter\src\main\webapp]
[INFO] Webapp assembled in [65 msecs]
[INFO] Building war: D:\hantsylabs\jakartaee8-starter\target\jakartaee8-starter.war
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  12.449 s
[INFO] Finished at: 2020-02-18T17:50:43+08:00
[INFO] ------------------------------------------------------------------------
```
After it done there is a *jakartaee8-starter.war* is located in the *target* folder.

Copy this file to *&lt;WildFly dir>/standalone/deployments*.

In the WildFly console, you will see the following deployment info.

```bash
17:54:48,491 INFO  [org.jboss.as.repository] (DeploymentScanner-threads - 1) WFLYDR0001: Content added at location D:\appsvr\wildfly\standalone\data\content\ac\d8ebc6d4327b6e9b1ec92b10186d2fe7fdda89\content
17:54:48,515 INFO  [org.jboss.as.server.deployment] (MSC service thread 1-3) WFLYSRV0027: Starting deployment of "jakartaee8-starter.war" (runtime-name: "jakartaee8-starter.war")
17:54:52,510 INFO  [org.jboss.weld.deployer] (MSC service thread 1-4) WFLYWELD0003: Processing weld deployment jakartaee8-starter.war
17:54:53,474 INFO  [org.hibernate.validator.internal.util.Version] (MSC service thread 1-4) HV000001: Hibernate Validator 6.0.17.Final
17:54:53,932 INFO  [org.jboss.weld.Version] (MSC service thread 1-3) WELD-000900: 3.1.2 (Final)
17:54:53,987 INFO  [org.infinispan.factories.GlobalComponentRegistry] (MSC service thread 1-8) ISPN000128: Infinispan version: Infinispan 'Infinity Minus ONE +2' 9.4.16.Final
17:54:54,396 INFO  [io.smallrye.metrics] (MSC service thread 1-2) MicroProfile: Metrics activated
17:54:54,453 INFO  [org.jboss.as.clustering.infinispan] (ServerService Thread Pool -- 76) WFLYCLINF0002: Started client-mappings cache from ejb container
17:54:55,626 INFO  [org.jboss.resteasy.resteasy_jaxrs.i18n] (ServerService Thread Pool -- 76) RESTEASY002225: Deploying javax.ws.rs.core.Application: class com.example.JaxrsActivator
17:54:55,787 INFO  [org.wildfly.extension.undertow] (ServerService Thread Pool -- 76) WFLYUT0021: Registered web context: '/jakartaee8-starter' for server 'default-server'
17:54:55,883 INFO  [org.jboss.as.server] (DeploymentScanner-threads - 1) WFLYSRV0010: Deployed "jakartaee8-starter.war" (runtime-name : "jakartaee8-starter.war")
```

To undeploy an applicaiton, just delete *jakartaee8-starter.war.deployed* in the  *&lt;WildFly dir>/standalone/deployments*.

In the WildFly console, there is undeployment info displayed.

```bash
17:57:41,787 INFO  [org.jboss.as.server] (DeploymentScanner-threads - 1) WFLYSRV0009: Undeployed "jakartaee8-starter.war" (runtime-name: "jakartaee8-starter.war")
```

## Add an administration user

Admin user is required to access [management console](http://localhost:9990) or remote deployment from Maven plugin, etc.

Enter *&lt;WildFly dir>/bin* , execute `add-user` to add a user.

```bash
# add-user

What type of user do you wish to add?
 a) Management User (mgmt-users.properties)
 b) Application User (application-users.properties)
(a):

Enter the details of the new user to add.
Using realm 'ManagementRealm' as discovered from the existing property files.
Username : admin
User 'admin' already exists and is disabled, would you like to...
 a) Update the existing user password and roles
 b) Enable the existing user
 c) Type a new username
(a): b
Updated user 'admin' to file 'D:\appsvr\wildfly\standalone\configuration\mgmt-users.properties'
Updated user 'admin' to file 'D:\appsvr\wildfly\domain\configuration\mgmt-users.properties'
Updated user 'admin' with groups null to file 'D:\appsvr\wildfly\standalone\configuration\mgmt-groups.properties'
Updated user 'admin' with groups null to file 'D:\appsvr\wildfly\domain\configuration\mgmt-groups.properties'
Press any key to continue . . .
```
There is an existing *admin* user , just enable it. If you want to update the password and roles, run `add-user` again, the select *a) Update the existing user password and roles* in the second step, input your new password, add it to the required groups. There are seven groups available in WildFly.

* Monitor
* Operator
* Maintainer
* Deployer
* Auditor
* Administrator
* SuperUser

Assign **Administrator** role to *admin* user.

