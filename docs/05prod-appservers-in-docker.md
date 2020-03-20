# Running Application Servers in Docker

In the [Deploying applications to Application Servers](./03run.md), we explored how to utilize the deployments scan to deploy a Jakarta EE application on application servers, such as [Glassfish](./03run-glassfish-m.md), [Payara](./03run-payara-m.md), [WildFly](./03run-wildfly-m.md), [Open Liberty](./03run-openliberty-m.md). 

With the deployments scan feature, it is easy to deploy a Jakarta EE application to an application server that running in a Docker container.

Assume you have installed Docker in your local development environment. If not, check the official [Docker installation guide](https://docs.docker.com/install/) to install it firstly.

## Payara Server

Once Docker is installed, you can start a Payara server with the `docker`command  quickly.

```bash
docker run -p 8080:8080 -p 4848:4848 -v ./deployments:/opt/payara/deployments payara/server-full
```

If this is your first time to run this command, it will take some seconds or minutes to download  the *payara/server-full* docker image firstly, the create a docker using *payara/server-full* image. Please be patient and drink a cup of coffee.

After it is running, copy the packaged war archive to the location *./deployments*.  The Payara server will scan the */opt/payara/deployments* in docker to deploy it automatically. 

More simply, you can start a Payara server by `docker-compose`. There is a Docker compose file  [*docker-compose.yml*](https://github.com/hantsy/jakartaee8-starter/blob/master/docker-compose.yml) available in the root of  [jakartaee8-starter](https://github.com/hantsy/jakartaee8-starter) .

Run the following command to start a Payara server using `docker-compose` command.

```bash 
docker-compose up payara
```

In the [*docker-compose.yml*](https://github.com/hantsy/jakartaee8-starter/blob/master/docker-compose.yml) , it defines a service for payara server.

```yaml
version: '3.3' # specify docker-compose version

# Define the services/containers to be run
services:
  payara:
    image: payara/server-full
    ports:
      - "8080:8080"
      - "8181:8181" # HTTPS listener
      - "4848:4848" # HTTPS admin listener
      - "9009:9009" # Debug port
    restart: always
#    environment:
#    JVM_ARGS: ""
#     - "AS_ADMIN_MASTERPASSWORD=admin" #default password is `changeit`
    volumes:
      - ./deployments:/opt/payara/deployments
```



 ## WildFly Server

Similar with Payara server, start a WildFly server with `docker` command like this.

```bash
docker run -p 8080:8080 -p 9990:9990 -v ./deployments:/opt/jboss/wildfly/standalone/deployments/ -it jboss/wildfly /opt/jboss/wildfly/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0
```

When it is running, copy *jakartaee8-starter.war* to to the location *./deployments*.  It will scan it and deploy it automatically. 

Alternatively, start a WildFly server via `docker-compose` command.

```bash
docker-compose up wildfly
```

The *wildfly* services defined in the [*docker-compose.yml*](https://github.com/hantsy/jakartaee8-starter/blob/master/docker-compose.yml).

```yaml
version: '3.3' # specify docker-compose version

# Define the services/containers to be run
services:
...
  wildfly:
    image: jboss/wildfly
    ports:
      - "8080:8080"
      - "9990:9990" # admin listener
    restart: always
    command: /opt/jboss/wildfly/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0
#    environment:
    volumes:
      - ./deployments:/opt/jboss/wildfly/standalone/deployments/   
```



## Open Liberty Server

Start a Open Liberty Server  with `docker` command.

```bash
docker run -p 9080:9080 -p 9443:9443 -v ./deployments:/config/dropins open-liberty:full
```

Instead, start a Open Liberty Server by `docker-compose` command. Define a *service* in the *docker-compose.yml* file.

```yaml
version: '3.3' # specify docker-compose version

# Define the services/containers to be run
services:
...
  openliberty:
    image: open-liberty:full
    ports:
      - "9080:9080"
      - "9443:9443" # HTTPS listener
    restart: always
#    command: configure.sh # use for open-liberty:kernel image.
#    environment:
    volumes:
      - ./deployments:/config/dropins  
```

> Unfortunately, there is no official Glassfish v5.1 in docker hub. There are some third party Glassfish docker images available, such as [adam-bien's docklands](http://www.adam-bien.com/roller/abien/entry/jakarta_ee_eclipse_glassfish_5), [jelastic/glassfish](https://hub.docker.com/r/jelastic/glassfish).

