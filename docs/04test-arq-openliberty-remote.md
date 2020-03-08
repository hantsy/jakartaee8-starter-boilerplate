# Open Liberty Remote Container Adapter

Open the *pom.xml* file, check the **arq-liberty-remote** profile.

```xml
<profile>
    <!-- Run with: mvn clean test -Parq-liberty-remote -->
    <id>arq-liberty-remote</id>
    <properties>
        <skipTests>false</skipTests>
    </properties>
    <dependencies>
        <dependency>
            <groupId>io.openliberty.arquillian</groupId>
            <artifactId>arquillian-liberty-remote</artifactId>
            <version>${arquillian-liberty.version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.cxf</groupId>
            <artifactId>cxf-rt-rs-client</artifactId>
            <version>${cxf.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.jaxrs</groupId>
            <artifactId>jackson-jaxrs-json-provider</artifactId>
            <version>2.10.1</version>
        </dependency>
        <dependency>
            <groupId>org.apache.cxf</groupId>
            <artifactId>cxf-rt-transports-http-hc</artifactId>
            <version>${cxf.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <build>
        <testResources>
            <testResource>
                <directory>src/test/arq-liberty-remote</directory>
                <includes>
                    <include>*</include>
                </includes>
                <excludes>
                    <exclude>server.xml</exclude>
                </excludes>
            </testResource>
        </testResources>
        <plugins>
            <plugin>
                <artifactId>maven-failsafe-plugin</artifactId>
                <version>${maven-failsafe-plugin.version}</version>
                <configuration>
                    <systemPropertyVariables>
                        <arquillian.launch>liberty-remote</arquillian.launch>
                        <java.util.logging.config.file>${project.build.testOutputDirectory}/logging.properties
                        </java.util.logging.config.file>
                    </systemPropertyVariables>
                </configuration>
            </plugin>
        </plugins>
    </build>
</profile>
```

The following is the content of the *arquillian.xml*.

```xml
<container qualifier="liberty-remote">
    <configuration>
        <property name="hostName">localhost</property>
        <property name="serverName">defaultServer</property>
        <property name="username">admin</property>
        <property name="password">admin</property>
        <property name="httpPort">9080</property>
        <property name="httpsPort">9443</property>
    </configuration>
</container>
```

To run the tests, you need a running Open Liberty server, and allow our Arquillian controller to connect it.

Create a server profile by `server` command in the Open Liberty dist, or copy *server.xml* from *src/test/arq-liberty-remote*  to *Open Liberty_dir/usr/servers/defaultServer*.

The content of our sample server.xml.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<server description="new server">

    <!-- Enable features -->
    <featureManager>
        <feature>javaee-8.0</feature>
        <feature>restConnector-2.0</feature>
    </featureManager>
    <quickStartSecurity userName="admin" userPassword="admin" />

    <ssl id="defaultSSLConfig" trustDefaultCerts="true"/>
    <keyStore id="defaultKeyStore" password="password" />

    <applicationMonitor updateTrigger="mbean" />
    <logging consoleLogLevel="INFO" />
    <remoteFileAccess>
        <writeDir>${server.config.dir}/dropins</writeDir>
    </remoteFileAccess>
    <httpEndpoint id="defaultHttpEndpoint"
                  httpPort="9080"
                  httpsPort="9443" />
                  
    <!-- Automatically expand WAR files and EAR files -->
    <applicationManager autoExpand="true"/>

</server>
```

In the above *server.xml* file.

* The `restConnector-2.0` is required to support remote connection.
* The `quickStartSecurity` is used to setup an administrative account quickly.
* The `remoteFileAccess` allows `${server.config.dir}/dropins` is writable, and  to receive deployments.
* Set a `keyStore` and ensure the client to connect to this server securely.

Start up the Open Liberty server, it will generate the resources for the new server profile, including the keystore files in *${server.config.dir}/resources/security*.

If you run the tests, you will got a failure about authorization.

Follow these steps to import the server certification and make it trusted by your client's JDK.

* Export the certification from the `jks` file in the *${server.config.dir}/resources/security* folder of Open Liberty server.

  ```bash
  keytool -export -alias default -file testwlp.crt -keystore key.jks 
  ```

* Import `.crt` into the JDK you are running the tests.

  ```
  keytool -import -trustcacerts -keystore $JAVA_HOME/jre/lib/security/cacerts -storepass changeit -alias testwlp -file testwlp.crt 
  ```

More details about the usage of `keytool` command, see [keytool documentation](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/keytool.html).

Now let's run the tests.

```bash
mvn clean verify -Parq-liberty-remote
```

