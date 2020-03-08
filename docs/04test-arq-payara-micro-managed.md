# Payara Micro Managed Container Adapter

Payara server provides a **micro** version of Payara server, which allow you start a  Payara server with a single jar file.

Payara also includes a Micro Managed Adapter to run tests against a Payara Micro dist.

Open  the *pom.xml* file, check the **arq-payara-micro** profile.

```xml
<profile>
    <id>arq-payara-micro</id>
    <properties>
        <skipTests>false</skipTests>
    </properties>
    <dependencies>

        <!-- https://mvnrepository.com/artifact/fish.payara.arquillian/payara-client-ee8 -->
        <dependency>
            <groupId>fish.payara.arquillian</groupId>
            <artifactId>payara-client-ee8</artifactId>
            <version>${arquillian-payara-server.version}</version>
            <scope>test</scope>
        </dependency>

        <!-- Payara Server Container adaptor -->
        <dependency>
            <groupId>fish.payara.arquillian</groupId>
            <artifactId>arquillian-payara-micro-managed</artifactId>
            <version>${arquillian-payara-server.version}</version>
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
                            <goal>copy</goal>
                        </goals>
                        <configuration>
                            <artifactItems>
                                <artifactItem>
                                    <groupId>fish.payara.extras</groupId>
                                    <artifactId>payara-micro</artifactId>
                                    <version>${payara.version}</version>
                                    <type>jar</type>
                                    <overWrite>false</overWrite>
                                    <outputDirectory>${project.build.directory}</outputDirectory>
                                    <destFileName>payara-micro.jar</destFileName>
                                </artifactItem>
                            </artifactItems>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-failsafe-plugin</artifactId>
                <version>${maven-failsafe-plugin.version}</version>
                <configuration>
                    <environmentVariables>
                        <MICRO_JAR>${project.build.directory}/payara-micro.jar</MICRO_JAR>
                    </environmentVariables>
                    <systemPropertyVariables>
                        <!--<payara.microJar>${project.build.directory}/payara-micro.jar</payara.microJar>-->
                    </systemPropertyVariables>
                </configuration>
            </plugin>
        </plugins>
    </build>
</profile>
```

Use `maven-dependency-plugin` to download a copy of Payara Micro to the *target* folder, set a `MICRO_JAR` environment variable or a `payara.microJar` system property in the configuration of `maven-failsafe-plugin`.  This adapter does not includes the jersey client libraries, add `payara-client-ee8` as dependencies.

Run the tests with **arq-payara-micro** profile.

```bash
mvn clean verify -Parq-payara-micro
```

You will the following info in the console.

```bash
...
[2020-02-23T16:50:30.161+0800] [] [INFO] [] [PayaraMicro] [tid: _ThreadID=1 _ThreadName=main] [timeMillis: 1582447830161] [levelValue: 800] Payara Micro  5.194 #badassmicrofish (build 327) ready in 19,746 (ms)

Feb 23, 2020 4:50:30 PM fish.payara.arquillian.container.payara.managed.PayaraMicroDeployableContainer deploy
INFO: Payara Micro running on host: 192.168.10.2 port: 8403
Feb 23, 2020 4:50:30 PM fish.payara.arquillian.container.payara.managed.PayaraMicroDeployableContainer printApplicationFound
INFO: Deployed application detected. Name: "test".
Feb 23, 2020 4:50:30 PM fish.payara.arquillian.container.payara.managed.PayaraMicroDeployableContainer printServletFound
INFO:           Servlet found. Name: "jsp".
Feb 23, 2020 4:50:30 PM fish.payara.arquillian.container.payara.managed.PayaraMicroDeployableContainer printServletFound
INFO:           Servlet found. Name: "ArquillianServletRunner".
Feb 23, 2020 4:50:30 PM fish.payara.arquillian.container.payara.managed.PayaraMicroDeployableContainer printServletFound
INFO:           Servlet found. Name: "jsp".
Feb 23, 2020 4:50:30 PM fish.payara.arquillian.container.payara.managed.PayaraMicroDeployableContainer printServletFound
INFO:           Servlet found. Name: "default".
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 29.906 s - in com.example.it.GreetingServiceTest
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
[INFO] Total time:  01:13 min
[INFO] Finished at: 2020-02-23T16:50:32+08:00
[INFO] ------------------------------------------------------------------------
```

