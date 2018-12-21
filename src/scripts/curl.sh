echo "Ping:"

curl -H "Content-Type: application/json" \
 localhost:8080/registrationservice/api/registration/ping

echo ""
echo "Register: "

curl -d '{"nickname": "nickname", "name": "name", "firstname": "firstname", "email": "email@mail.de", "password": "password"}' \
 -H "Content-Type: application/json" \
 localhost:8080/registrationservice/api/registration/register
