FROM open-liberty:kernel
COPY --chown=1001:0  ./target/jakartaee8-starter.war /config/dropins/
COPY --chown=1001:0  ./src/main/liberty/config/server.xml /config/
RUN configure.sh