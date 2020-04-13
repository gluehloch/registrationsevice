#!/bin/bash
export DOCKER_NAME='registerdb'
export DOCKER_DB_HOST='192.168.99.101'

# Mit der nativen Windows Variante von Docker entfällt der Parameter --expose. Der Parameter -p fuer
# das Port-Binding ist dennoch wichtig:
# docker run --name mariadb -p 3308:3306 -e MYSQL_ALLOW_EMPTY_PASSWORD -d mariadb:latest

# Mit Docker Toolbox wird der --expose Parameter wichtig:
# docker run --expose=3306 -p 3308:3306 --name register -e MYSQL_ALLOW_EMPTY_PASSWORD=true -d mariadb:latest
# docker run --expose=3306 -p 3308:3306 --name ${DOCKER_NAME} -e MYSQL_ALLOW_EMPTY_PASSWORD=true -d mariadb:latest

# sleep 10s

# mysql -u root -h 192.168.99.100 -P 3308
mysql -u root --password=root -h ${DOCKER_DB_HOST} < mysql-prod.sql
mysql -u registersu --password=register -h ${DOCKER_DB_HOST} -D register < mysql.sql
