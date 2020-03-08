# Payara Managed Container Adapter

Open the *pom.xml* , check the **arq-payara-managed** profile.

```xml
<profile>
    <id>arq-payara-managed</id>
    <properties>
        <skipTests>false</skipTests>
    </properties>
    <dependencies>
        <!-- Payara Server Container adaptor -->
        <dependency>
            <groupId>fish.payara.arquillian</groupId>
            <artifactId>arquillian-payara-server-managed</artifactId>
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
                            <goal>unpack</goal>
                        </goals>
                        <configuration>
                            <artifactItems>
                                <artifactItem>
                                    <groupId>fish.payara.distributions</groupId>
                                    <artifactId>payara</artifactId>
                                    <version>${payara.version}</version>
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
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-failsafe-plugin</artifactId>
                <version>${maven-failsafe-plugin.version}</version>
                <configuration>
                    <environmentVariables>
                        <GLASSFISH_HOME>${project.build.directory}/payara5</GLASSFISH_HOME>
                    </environmentVariables>
                </configuration>
            </plugin>
        </plugins>
    </build>
</profile>
```

Very similar with **arq-glassfish-managed** profile, but there are some difference,  including:

* Add `arquillian-payara-server-managed` as dependency instead.
* The `maven-dependency-plugin` prepares a copy of Payara server to *target* folder. Set a `GLASSFISH_HOME` (or  `PAYARA_HOME`) environment variable in the configuration of `maven-failsafe-plugin`.
* For JAX-RS tests, no need to add the Jersey client libraries, `arquillian-payara-server-managed` resolves the tedious dependency configuration.

Run the following command to run tests against Payara Managed Container Adapter.

```bash
mvn clean verify -Parq-payara-managed
```

