# Deploying applications to Glassfish Server using Cargo Maven Plugin

The legacy official *maven-glassfish-plugin* is discontinued, but there is a better alternative existed. [Cargo](https://codehaus-cargo.github.io) project provide a common way to deploy the applications into Jakarta EE application servers by  maven plugin, Ant tasks, and pure Java APIs. Most of the popular application servers are got supported. Generally, it provides 3 approaches to deploy applications.

* Remote- Deploying to a running application server via  client APIs 
* Local - Controlling the lifecycle of application servers such as start and stop, etc. 
* Embedded - Package the application and the application server into a single package and run it.

Most of the application servers support remote and local deployment.

## Deploying to a running Glassfish server

The following is using a maven profile to configure the remote deployment.

```xml
<profile>
	<id>glassfish-remote</id>
	<build>
		<plugins>
			<plugin>
				<groupId>org.codehaus.cargo</groupId>
				<artifactId>cargo-maven2-plugin</artifactId>
				<configuration>
					<container>
						<containerId>glassfish5x</containerId>
						<type>remote</type>
					</container>
					<configuration>
						<type>runtime</type>
						<properties>
							<!--   <cargo.remote.username>admin</cargo.remote.username>
							   <cargo.remote.password>adminadmin</cargo.remote.password>
							   <cargo.glassfish.admin.port>4848</cargo.glassfish.admin.port>
							   <cargo.hostname>localhost</cargo.hostname>-->
						</properties>
					</configuration>
				</configuration>
				<!-- provides JSR88 client API to deploy on Glassfish/Payara Server -->
				<dependencies>
					<dependency>
						<groupId>org.glassfish.main.deployment</groupId>
						<artifactId>deployment-client</artifactId>
						<version>${glassfish.version}</version>
					</dependency>
				</dependencies>
			</plugin>
		</plugins>
	</build>
</profile>
```

It depends on the  Glassfish deployment client, and if you have set the admin password in the target Glassfish server, try to set the related properties.  

Before deploying, make sure the Glassfish is running, then execute the following command to start deployment.

```bash
mvn package cargo:deploy -Pglassfish-remote
```

After it is deployed successfully,  try to access the sample  API endpoint.

## Deploying to a local Glassfish server

Similarly, I use another maven profile to configure the local Glassfish server. You can use an existing Glassfish or download a fresh copy for deployment purpose as the following.

```xml
<profile>
	<id>glassfish-local</id>
	<properties>
		<glassfish.home>${project.build.directory}/glassfish5</glassfish.home>
		<glassfish.domainDir>${glassfish.home}/glassfish/domains</glassfish.domainDir>
		<glassfish.domainName>domain1</glassfish.domainName>
	</properties>
	<build>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-dependency-plugin</artifactId>
				<version>${maven-dependency-plugin.version}</version>
				<executions>
					<execution>
						<id>unpack</id>
						<phase>process-resources</phase>
						<goals>
							<goal>unpack</goal>
						</goals>
						<configuration>
							<artifactItems>
								<artifactItem>
									<!--
									 <groupId>fish.payara.distributions</groupId>
									<artifactId>payara</artifactId>
									<version>${payara.version}</version>
									<type>zip</type>
									-->
									<groupId>org.glassfish.main.distributions</groupId>
									<artifactId>glassfish</artifactId>
									<version>${glassfish.version}</version>
									<type>zip</type>
									<overWrite>false</overWrite>
									<outputDirectory>${project.build.directory}</outputDirectory>
								</artifactItem>
							</artifactItems>
						</configuration>
					</execution>
				</executions>
			</plugin>
			<plugin>
				<groupId>org.codehaus.cargo</groupId>
				<artifactId>cargo-maven2-plugin</artifactId>
				<version>${cargo-maven2-plugin.version}</version>
				<configuration>
					<container>
						<containerId>glassfish5x</containerId>
						<type>installed</type>
						<home>${glassfish.home}</home>
					</container>
					<configuration>
						<type>existing</type>
						<home>${glassfish.domainDir}</home>
						<properties>
							<cargo.glassfish.domain.name>${glassfish.domainName}</cargo.glassfish.domain.name>
							<cargo.remote.timeout>600000</cargo.remote.timeout>
							<cargo.remote.password></cargo.remote.password>
						</properties>
					</configuration>
				</configuration>
				<!-- provides JSR88 client API to deploy on Glassfish/Payara Server -->
				<dependencies>
					<dependency>
						<groupId>org.glassfish.main.deployment</groupId>
						<artifactId>deployment-client</artifactId>
						<version>${glassfish.version}</version>
					</dependency>
				</dependencies>
			</plugin>
		</plugins>

	</build>
</profile>
```

Run the following command to start the application server and deploy the application to the server.

```bash
mvn clean package cargo:run -Pglassfish-local
```

The `maven-dependency-plugin` will download a copy of Glassfish distribution and extract the files, then `cargo:run` goal  will start the server, and deploy the application finally.

More info about Cargo maven plugin for Glassfish, see [here](https://codehaus-cargo.github.io/cargo/GlassFish+5.x.html).





