# Deploying applications to Glassfish Server using Apache NetBeans IDE

NetBeans has built-in support for Glassfish.  

[toc]

## Add a Glassfish Server instance

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

## Start and Stop Glassfish Server

Let's start the Glassfish server by click *Start* in the context menu.  Wait for seconds, you will see the *Output* screen similar to the following.

<img src="./glassfish-start-output-nb.png" style="zoom:80%;" />

To stop the running Glassfish server,  click *Stop* in the context menu of the existed Glassfish server node. Or jus click the *stop* button the *Notifications* windows.

## Deploy and undeploy applications

Switch to  *Project* view, right click the project node, and select *Run* in the context menu.

In the  *Select deployment server* dialog, select *Glassfish server* we have created in the dropdown menu.

<img src="./run-nb.png" alt=" Select deployment server" style="zoom:80%;" />

Click *OK* button. It will try to build the project and deploy the application package into the NetBeans managed Glassfish server.   

After it is deployed successfully, there is a success message similar with the following in the *Output* window.

```bash
------------------------------------------------------------------------
Deploying on GlassFish Server
    profile mode: false
    debug mode: false
    force redeploy: true
In-place deployment at D:\hantsylabs\jakartaee8-starter\target\jakartaee8-starter

```

Let's switch to *Server* view, there several nodes are displayed under Glassfish servers. Expand the *Application* node, you will see there is a new node *jakartaee8-starter* there.

<img src="./glassfish-node-deploy-nb.png" alt="Glassfish server node after the application is deployed" style="zoom:80%;" />

Currently the sample application just includes a simple RESTful APIs which serves at  the */api/greeting* endpoint. 

Open  your terminal and use `curl` to verify the APIs.

```bash
curl http://localhost:8080/jakartaee8-starter/api/greeting/hantsy
{"message":"Say Hello to hantsy at 2019-11-04T16:16:13.509"}
```

To  undeploy the application, in the *Service* view, expand Servers/Glassfish Server/Applications, right click the *jakartaee8-starter* node, click *Undeploy*  to undeploy it from Glassfish server. 

