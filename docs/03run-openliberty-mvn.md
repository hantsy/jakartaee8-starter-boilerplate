# Deploying applications to Open Liberty Server using Liberty Maven plugin

Open Liberty provides an official maven plugin for application deployment.  

```xml
<!-- Enable liberty-maven-plugin -->
<plugin>
	<groupId>io.openliberty.tools</groupId>
	<artifactId>liberty-maven-plugin</artifactId>
	<version>3.1</version>
</plugin> 
```

`liberty:dev` provides a dev mode for developers. 

`liberty:run` is easy to run the application on a local Open Liberty server.  If the server does not exist, it will download it automatically.

`liberty:deploy` will copy applications to the Liberty server's *dropins* or *apps* directory, If the server instance is running, it will also verify the applications started successfully.

Unluckily, liberty maven plugin does not support deployment to remote Liberty Server, see [issue  #245](https://github.com/OpenLiberty/ci.maven/issues/245). 

More info about  Liberty maven plugin, see [here](https://github.com/OpenLiberty/ci.maven).

