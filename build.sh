mvn package -DskipTests && docker cp ./target/registrationservice-0.0.1-SNAPSHOT.war register-0.0.1:/usr/local/tomcat/webapps/registrationservice.war
