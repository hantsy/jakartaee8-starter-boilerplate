# Deploying applications to WildFly Server using WildFly Maven plugin

Redhat WildFly has official maven plugin support for application deployment and application server management.

Declare a WildFly maven plugin in the pom.xml file.

```xml
<!-- The WildFly plugin deploys your war to a local WildFly container -->
<!-- To use, run: mvn package wildfly:deploy -->
<plugin>
	<groupId>org.wildfly.plugins</groupId>
	<artifactId>wildfly-maven-plugin</artifactId>
	<version>2.0.1.Final</version>
</plugin> 
```

The `wildfly:start` goal  will start an server instance, if there is no server instance configured, it will download the newest WildFly redistribution or define a `wilfly.version` property to specify a version , and extract the files, and start it in an embedded mode. 

If you want to reuse your local WildFly server you have downloaded before, configure a  `jbossHome` property in the `configuration` of WildFly maven plugin or an external `wildfly.home`  property.

```xml
<plugin>
    <groupId>org.wildfly.plugins</groupId>
    <artifactId>wildfly-maven-plugin</artifactId>
    <version>${wildfly-maven-plugin.version}</version>
    <configuration>
        
        <!-- also fetch wildfly.home from jbossHome -->
        <!-- <jossHome>${env.WILDFLY_HOME}</jbossHome>-->
        ...
    </configuration>
</plugin>
...
<!-- if a wildfly.home property is not set, firstly it will download a copy of wildfly distribution automatically -->
<properties>
	<wildfly.home>...</wildfly.home>
</properties>

```

If the WildFly server instance is running, and run the following command to start deployment.

```bash
mvn package wildfly:deploy
```

More simply, execute `mvn deploy:run` to start a WildFly server instance and start the deployment immediately.

WildFly maven plugin also support of deploying to remote  WildFly server instance. Configure the host properties.

```xml
<plugin>
    <groupId>org.wildfly.plugins</groupId>
    <artifactId>wildfly-maven-plugin</artifactId>
    <version>${wildfly-maven-plugin.version}</version>
    <configuration>

        <!-- control remote wildfly server -->
        <hostname>localhost</hostname>
        <port>9990</port>
        <username>admin</username>
        <password>admin</password>
    </configuration>
</plugin>
```

Username and password are sensitive info, you can follow the Maven rule, and configure them in the Maven global settings.xml file(*~/.m2/settings.xml*).

```xml
<settings>
    ...
	<servers>
       ... 
       <server>
            <id>wildfly-svr</id>
           	<hostname>localhost</hostname>
            <port>9990</port>
            <username>admin</username>
            <password>admin</password>
        </server>
    </servers>
</settings>    
```
Then configure the server id in the maven plugin configuration.

```xml
<plugin>
    <groupId>org.wildfly.plugins</groupId>
    <artifactId>wildfly-maven-plugin</artifactId>
    <version>${wildfly-maven-plugin.version}</version>
    <configuration>
		...
		<id>wildfly-svr</id>
    </configuration>
</plugin>
```

Make sure it it running, run `mvn wildfly:deploy` to start deployment.

Use `wildfly:undeploy` goal to undeploy an application from a running WildFly server.

You can also  add some configuration to WildFly by maven plugin, such as registering a JDDC driver module, configuring a DataSource, etc, more information please read the [Wildfy maven plugin documentation](https://docs.jboss.org/wildfly/plugins/maven/latest/index.html). 