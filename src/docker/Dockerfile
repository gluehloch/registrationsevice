# docker build -t register:0.2.0-SNAP .
# Namensvorschlaege: register
#                    
# docker run --expose 8080 -p 9090:8080 --name register-0.2.0-SNAP -d register:0.2.0-SNAP
#                             ^    ^- Port im Container
#                             \- Port im Host
# docker run --network betoffice --expose 8080 -p 9090:8080 --name register -d register:0.0.1
#
# Im laufenden Betrieb ein WAR installieren:
# docker cp registrationservice-0.0.1-SNAPSHOT.war registerservice:/usr/local/tomcat/webapps/registrationservice2.war
#

#
# docker network create dev
# docker network rm dev
# docker network connect dev mariadb
# docker network connect dev register
# Im JDBC connection string kann nun der Container Name, z.B. 'mariadb', als Host Name verwendet werden. 
#

# FROM tomcat:9.0.35-jdk14-openjdk-oracle
FROM tomcat:9.0.37-jdk14-openjdk-buster

LABEL maintainer="Andre Winkler"
LABEL mail="andre.winkler@web.de"

COPY registrationservice-0.2.0-SNAPSHOT.war /usr/local/tomcat/webapps/registrationservice.war
COPY .register.properties /root/.register.properties
# COPY mariadb-java-client-2.2.6.jar /usr/local/tomcat/lib

EXPOSE 8080
