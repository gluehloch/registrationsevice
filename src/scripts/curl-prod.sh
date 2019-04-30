#!/bin/bash
echo "Ping:"

curl -H "Content-Type: application/json" \
 http://cookie.gluehloch.de/registrationservice/api/registration/ping

echo ""
echo "Validate: "
curl -d '{"nickname": "nickname", "name": "name", "firstname": "firstname", "email": "email@mail.de", "password": "password"}' \
   -H "Content-Type: application/json" \
  http://cookie.gluehloch.de/registrationservice/api/registration/validate

echo ""
echo "Register: "

curl -d '{"nickname": "nickname", "name": "name", "firstname": "firstname", "email": "email@mail.de", "password": "password"}' \
 -H "Content-Type: application/json" \
 http://cookie.gluehloch.de/registrationservice/api/registration/register
