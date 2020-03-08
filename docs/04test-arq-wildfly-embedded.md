# WildFly Embedded Container Adapter 

Open the *pom.xml* file, check the **arq-wildfly-embedded** profile.

```xml
<profile>
    <!-- An optional Arquillian testing profile that executes tests in your
                WildFly instance -->
    <!-- Run with: mvn clean test -Parq-wildfly-embedded -->
    <id>arq-wildfly-embedded</id>
    <properties>
        <skipTests>false</skipTests>
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
            <artifactId>wildfly-arquillian-container-embedded</artifactId>
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

It adds a `wildfly-arquillian-container-embedded` dependency instead, other config is similar with the **arq-wildfly-managed** profile.

Run the tests.

```bash
mvn clean verify -Parq-wildfly-embedded
```

