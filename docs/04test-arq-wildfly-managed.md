# WildFly Managed Container Adapter 

Open the *pom.xml* file, check the **arq-wildfly-managed** profile.

```xml
 <profile>
     <id>arq-wildfly-managed</id>
     <properties>
         <skipTests>false</skipTests>
         <serverProfile>standalone-full.xml</serverProfile>
         <serverRoot>${project.build.directory}/wildfly-${wildfly.version}</serverRoot>
     </properties>
     <dependencies>
         <dependency>
             <groupId>org.jboss.resteasy</groupId>
             <artifactId>resteasy-client</artifactId>
             <version>${resteasy.version}</version>
             <scope>test</scope>
         </dependency>
         <dependency>
             <groupId>org.jboss.resteasy</groupId>
             <artifactId>resteasy-jackson2-provider</artifactId>
             <version>${resteasy.version}</version>
             <scope>test</scope>
         </dependency>
         <dependency>
             <groupId>org.wildfly.arquillian</groupId>
             <artifactId>wildfly-arquillian-container-managed</artifactId>
             <version>${wildfly-arquillian.version}</version>
             <scope>test</scope>
         </dependency>
     </dependencies>

     <build>
         <plugins>
             <plugin>
                 <groupId>org.apache.maven.plugins</groupId>
                 <artifactId>maven-dependency-plugin</artifactId>
                 <version>${maven-dependency-plugin.version}</version>
                 <executions>
                     <execution>
                         <id>unpack</id>
                         <phase>pre-integration-test</phase>
                         <goals>
                             <goal>unpack</goal>
                         </goals>
                         <configuration>
                             <artifactItems>
                                 <artifactItem>
                                     <groupId>org.wildfly</groupId>
                                     <artifactId>wildfly-dist</artifactId>
                                     <version>${wildfly.version}</version>
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
                 <artifactId>maven-failsafe-plugin</artifactId>
                 <version>${maven-failsafe-plugin.version}</version>
                 <configuration>
                     <environmentVariables>
                         <JBOSS_HOME>${project.build.directory}/wildfly-${wildfly.version}</JBOSS_HOME>
                     </environmentVariables>
                     <systemPropertyVariables>
                         <java.util.logging.manager>org.jboss.logmanager.LogManager</java.util.logging.manager>
                     </systemPropertyVariables>
                 </configuration>
             </plugin>
         </plugins>
     </build>
</profile>
```

In the above codes,

* The `wildfly-arquillian-container-managed` is required for Arquillian WildFly Managed Container adapter.
* The Resteay related dependencies are required for JAX-RS tests.
* Use `maven-dependency-plugin` to download a copy of WildFly server to the *target* folder, and set `JBOSS_HOME` environment variable to use this downloaded WildFly in the `maven-failsafe-plugin`.

Run the tests via the following command.

```bash
mvn clean verify -Parq-wildfly-managed
```

You will see the following info in the console.

```bash
[INFO] --- maven-failsafe-plugin:3.0.0-M4:integration-test (integration-test) @ jakartaee8-starter ---
[INFO]
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.example.it.GreetingResourceTest
17:46:24,519 INFO  [org.jboss.modules] (main) JBoss Modules version 1.9.2.Final
17:46:26,697 INFO  [org.jboss.msc] (main) JBoss MSC version 1.4.11.Final
17:46:26,727 INFO  [org.jboss.threads] (main) JBoss Threads version 2.3.3.Final
17:46:26,954 INFO  [org.jboss.as] (MSC service thread 1-2) WFLYSRV0049: WildFly Full 19.0.0.Beta2 (WildFly Core 11.0.0.Beta8) starting
...
17:46:33,763 INFO  [org.jboss.as] (Controller Boot Thread) WFLYSRV0025: WildFly Full 19.0.0.Beta2 (WildFly Core 11.0.0.Beta8) started in 10021ms - Started 316 of 582 services (374 services are lazy, passive or on-demand)
...
17:46:34,207 INFO  [org.jboss.as.server.deployment] (MSC service thread 1-7) WFLYSRV0027: Starting deployment of "39fd4362-55da-49b0-8164-7a7a3d92f19f.war" (runtime-name: "39fd4362-55da-49b0-8164-7a7a3d92f19f.war")
...
17:46:37,713 INFO  [org.jboss.as.server] (management-handler-thread - 1) WFLYSRV0010: Deployed "39fd4362-55da-49b0-8164-7a7a3d92f19f.war" (runtime-name : "39fd4362-55da-49b0-8164-7a7a3d92f19f.war")
...
17:46:39,559 INFO  [org.jboss.as.server] (management-handler-thread - 1) WFLYSRV0009: Undeployed "39fd4362-55da-49b0-8164-7a7a3d92f19f.war" (runtime-name: "39fd4362-55da-49b0-8164-7a7a3d92f19f.war")
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 19.854 s - in com.example.it.GreetingResourceTest
[INFO] Running com.example.it.GreetingServiceTest
...
17:46:40,900 INFO  [org.jboss.as.server.deployment] (MSC service thread 1-7) WFLYSRV0027: Starting deployment of "test.war" (runtime-name: "test.war")
...
17:46:41,819 INFO  [org.jboss.as.server] (management-handler-thread - 1) WFLYSRV0010: Deployed "test.war" (runtime-name : "test.war")
...
17:46:42,195 INFO  [org.jboss.as.server] (management-handler-thread - 1) WFLYSRV0009: Undeployed "test.war" (runtime-name: "test.war")
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.576 s - in com.example.it.GreetingServiceTest
...
17:46:42,436 INFO  [org.jboss.as] (MSC service thread 1-8) WFLYSRV0050: WildFly Full 19.0.0.Beta2 (WildFly Core 11.0.0.Beta8) stopped in 132ms
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO]
[INFO] --- maven-failsafe-plugin:3.0.0-M4:verify (integration-test) @ jakartaee8-starter ---
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  51.375 s
[INFO] Finished at: 2020-02-23T17:46:43+08:00
[INFO] ------------------------------------------------------------------------
```

From the logging, Aquillian controller have done the following work when running the tests.

* Starting the WildFly Server.
* Packaging and deploying the archive
* Running the tests
* Undeploying the test archive
* Reporting the result,  and repeating the above steps if there are multi tests to run.
* Finally Stopping the WildFly Server