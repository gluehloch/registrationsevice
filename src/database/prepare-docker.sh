#!/bin/bash
export DOCKER_NAME='registerdb'
export DOCKER_DB_HOST='192.168.99.100'

# Die Testdatenbank laueft unter Port 3308.
# docker run --expose=3306 -p 3308:3306 --name register -e MYSQL_ALLOW_EMPTY_PASSWORD=true -d mariadb:latest
docker run --expose=3306 -p 3308:3306 --name ${DOCKER_NAME} -e MYSQL_ALLOW_EMPTY_PASSWORD=true -d mariadb:latest

# mysql -u root -h 192.168.99.100 -P 3308
mysql -u root -h ${DOCKER_DB_HOST} -P 3308 < mysql-prod.sql
mysql -u registersu --password=register -h ${DOCKER_DB_HOST} -P 3308 -D register < mysql.sql
