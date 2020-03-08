# Payara Embedded Container Adapter

Open the *pom.xml* file, check the **arq-payara-embedded** profile.

```xml
<profile>
    <id>arq-payara-embedded</id>
    <properties>
        <skipTests>false</skipTests>
    </properties>
    <dependencies>
        <dependency>
            <groupId>fish.payara.extras</groupId>
            <artifactId>payara-embedded-all</artifactId>
            <version>${payara.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>fish.payara.arquillian</groupId>
            <artifactId>arquillian-payara-server-embedded</artifactId>
            <version>${arquillian-payara-server.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
</profile>
```

Similar with the Glassfish's **arq-glassfish-embedded** profile, but add the Payara specific dependencies instead.

Run the tests using the following command.

```bash
mvn clean verify -Parq-payara-remote
```

> Payara arquillian includes all  Jersey Client dependencies , we do not need to add it explicitly.