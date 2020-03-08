# Payara Remote Container Adapter

Open the *pom.xml* file, locate to **arq-payara-remote** profile.

```xml
<profile>
    <id>arq-payara-remote</id>
    <properties>
        <skipTests>false</skipTests>
    </properties>
    <dependencies>
        <dependency>
            <groupId>fish.payara.arquillian</groupId>
            <artifactId>arquillian-payara-server-remote</artifactId>
            <version>${arquillian-payara-server.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-failsafe-plugin</artifactId>
                <version>${maven-failsafe-plugin.version}</version>
                <configuration>
                    <!-- This needs tuning -->
                    <systemPropertyVariables>
                        <arquillian.launch>glassfish-remote</arquillian.launch>
                    </systemPropertyVariables>
                </configuration>
            </plugin>
        </plugins>
    </build>
</profile>
```

It reuses `glassfish-remote`  qualifier in *src/resources/arquillian.xml*.

```xml
<container qualifier="glassfish-remote">
    <configuration>
        <property name="adminUser">admin</property>
        <property name="adminPassword">adminadmin</property>
    </configuration>
</container>
```

Start up a local Payara server for test purpose, make sure the `adminPassword` is set correctly.

Run the tests using the following command.

```bash
mvn clean verify -Parq-payara-remote
```


