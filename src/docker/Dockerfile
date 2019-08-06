# docker build -t register:0.0.1 .
# Namensvorschlaege: register
#                    
# docker run --expose 8080 -p 9090:8080 --name register-0.0.1 -d register:0.0.1
#                             ^    ^- Port im Container
#                             \- Port im Host
# docker run --expose 8080 -p 9090:8080 --name register -d register:0.0.1
#
# Im laufenden Betrieb ein WAR installieren:
# docker cp registrationservice-0.0.1-SNAPSHOT.war registerservice:/usr/local/tomcat/webapps/registrationservice2.war
#

FROM tomcat:9.0.21-jdk11-openjdk

LABEL maintainer="Andre Winkler"
LABEL mail="andre.winkler@web.de"

COPY registrationservice-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/registrationservice.war
COPY .register.properties /root/.register.properties
# COPY mariadb-java-client-2.2.6.jar /usr/local/tomcat/lib

EXPOSE 8080
