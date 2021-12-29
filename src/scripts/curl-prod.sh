#!/bin/bash
echo "Ping:"

#
# In einer aelteren Version fand ich hier:
# * http://cookie.gluehloch.de/registrationservice/api/registration/ping
# Die Idee mit /api wurde aber nicht weiterverfolgt. Aktuell ist der Endpunkt ueber die folgende Adresse erreichbar:
# * https://cookie.gluehloch.de/registrationservice/ping bzw.
# * https://gluehloch.de/registrationservice/ping oder
# * https://tippdiekistebier.de/registrationservice/ping
#


curl -H "Content-Type: application/json" \
 https://cookie.gluehloch.de/registrationservice/ping

echo ""
echo "Validate: "
curl -d '{"nickname": "nickname", "name": "name", "firstname": "firstname", "email": "email@mail.de", "password": "password"}' \
   -H "Content-Type: application/json" \
  https://cookie.gluehloch.de/registrationservice/registration/validate

echo ""
echo "Register: "

curl -d '{"nickname": "nickname", "name": "name", "firstname": "firstname", "email": "email@mail.de", "password": "password"}' \
 -H "Content-Type: application/json" \
 http://cookie.gluehloch.de/registrationservice/api/registration/register
