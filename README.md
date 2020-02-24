# registrationservice
User registration service

## Development environment

```
git clone git@github.com:gluehloch/registrationservice.git

cd ./src/database
chmod u+x prepare-docker.sh
dos2unix prepare-docker.sh

./prepare-docker.sh
```

Meine Versuche mit openssl generierte Schlüssel. Ziel: Diese dann mit Java
einzulesen. Hat nicht so funktioniert bisher.

```
openssl genpkey -out rsakey.pem -algorithm RSA -pkeyopt rsa_keygen_bits:2048

openssl key file Generierung:
```

```
openssl pkcs8 -topk8 -v2 des3 \
    -in test_rsa_key.old -passin 'pass:super secret passphrase' \
    -out test_rsa_key -passout 'pass:super secret passphrase'
```

Ohne Password?

```    
openssl pkcs8 -topk8 -v2 des3 -in test_rsa_key.old -out test_rsa_key    
```