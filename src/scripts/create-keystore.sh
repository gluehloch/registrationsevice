#!/bin/bash

keytool -genkeypair \
    -alias testkey \
    -keyalg RSA \
    -keysize 2048 \
    -dname "CN=Andre Winkler, OU=awtools, O=awtools, L=Essen, ST=Unknown, C=BRD" \
    -validity 100 \
    -storetype PKCS12 \
    -keystore keystore.jks \
    -storepass abcdef