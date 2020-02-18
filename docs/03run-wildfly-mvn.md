

Add a user admin.

```bash
#>add-user.bat

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


## Wildfly Server

Redhat Wildfly has official maven plugin support for application deployment and application server management.

Declare a wildfly maven plugin in the pom.xml file.

```xml
<!-- The WildFly plugin deploys your war to a local WildFly container -->
<!-- To use, run: mvn package wildfly:deploy -->
<plugin>
	<groupId>org.wildfly.plugins</groupId>
	<artifactId>wildfly-maven-plugin</artifactId>
	<version>2.0.1.Final</version>
</plugin> 
```

Make sure  there is a running Wildfly server, and run the following command to start deploymaent.

```bash
mvn package wildfly:deploy
```

You can also  add some configuration to Wildfly by maven plugin, such as registering a JDDC driver module, configuring a DataSource, etc, more information please read the [Wildfy maven plugin documentation](https://docs.jboss.org/wildfly/plugins/maven/latest/index.html). 