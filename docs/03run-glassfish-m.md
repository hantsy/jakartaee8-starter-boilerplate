# Deploying applications to Glassfish Server - The Hard Way

Download a copy of  [Glassfish v5.1](https://projects.eclipse.org/projects/ee4j.glassfish/downloads), and extract files into your local system.

## Start and Stop Glassfish Server

In the *&lt;Glassfish  dir>/glassfish/bin* folder, there are a few command tools used to control Glassfish server.

Open your terminal, enter  *&lt;Glassfish dir>/glassfish/bin*.

Execute the following command to start Glassfish Server.

```bash
# Linux
./startserv.sh
# windows
startserv
```

By default, it will start with the default domain *doman1*, if you want to start the server with a different one, add the domain name to the command line.

```bash
startserv domain2
```

To stop a running Glassfish Server, jus terminate the process by *Ctrl+C* , or execute the following command in another terminal window.

```bash
# Linux
./stopserv.sh
# windows
stopserv
```

Alternatively, you can use the *asadmin* utility in *&lt;Glassfish dir>/bin* to start the server with a specific domain *in background*.

```bash
# asadmin start-domain domain1
Waiting for domain1 to start ................................................
Successfully started the domain : domain1
domain  Location: D:\appsvr\glassfish5\glassfish\domains\domain1
Log File: D:\appsvr\glassfish5\glassfish\domains\domain1\logs\server.log
Admin Port: 4848
Command start-domain executed successfully.
```

Similarly, use `asadmin stop-domain domain1` to stop a domain.

```bash
# asadmin stop-domain domain1
Waiting for the domain to stop .
Command stop-domain executed successfully.
```



## Deploy applications

Firstly package the application into a **war** package.
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

Copy this file to *&lt;Glassfish dir>/glassfish/domains/domain1/autodeploy/*.

In the Glassfish console, you will see the following deployment info.

```bash
Selecting file D:\appsvr\glassfish5\glassfish\domains\domain1\autodeploy\jakartaee8-starter.war for autodeployment
...
Clustered CDI Event bus initialized|
...
Initializing Soteria 1.1-b01 for context '/jakartaee8-starter'
...
Loading application [jakartaee8-starter] at [/jakartaee8-starter]
...
jakartaee8-starter was successfully deployed in 1,956 milliseconds
...
[AutoDeploy] Successfully autodeployed : D:\appsvr\glassfish5\glassfish\domains\domain1\autodeploy\jakartaee8-starter.war.
```

To undeploy an applicaiton, just delete *jakartaee8-starter.war* in the  *&lt;Glassfish dir>/glassfish/domains/domain1/autodeploy/*.

In the Glassfish console, it displays the undeployment progress.

```bash
Autoundeploying application:  jakartaee8-starter
...
[AutoDeploy] Successfully autoundeployed : D:\appsvr\glassfish5\glassfish\domains\domain1\autodeploy\jakartaee8-starter.war.
```

## Add an administration user

An administrator user is required to access [Admin Console](http://localhost:4848) or remote deployment from Maven plugin, etc.

By default, Glassfish shipped an **admin** user with an empty password. You can set the password by `asadmin` command.

Enter *&lt;Glassfish dir>/bin* , execute the following command to start changing the password of the admin user.

```bash
# asadmin change-admin-password
Enter admin user name [default: admin]>
Enter the admin password>
Enter the new admin password>
Enter the new admin password again>
Command change-admin-password executed successfully.
```
> Before you execute this command, make sure your Glassfish server is running.

