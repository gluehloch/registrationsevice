# registrationservice
User registration service

## Development environment

Clone the GIT repository and 
```
git clone git@github.com:gluehloch/registrationservice.git

cd ./src/database
chmod u+x prepare-docker.sh
dos2unix prepare-docker.sh

./prepare-docker.sh
```

## Configuration
Beispiel:

```
register.persistence.classname = org.mariadb.jdbc.Driver
register.persistence.url = jdbc:mariadb://127.0.0.1:3306/registertest
register.persistence.username = register
register.persistence.password = register

register.mail.smtp.auth =
register.mail.smtp.starttls.enable =
register.mail.smtp.host =
register.mail.smtp.port =
register.mail.smtp.ssl.trust =
register.mail.authentication.user =
register.mail.authentication.password =
```

Als Datei in `${user-home}/.register.properties` oder als Classpath File `/register.properties`.

Siehe auch Klasse package `de.awtools.registration.config.PersistenceJPAConfig`:

```
@PropertySource(ignoreResourceNotFound = true, value = {
        "file:${AWTOOLS_CONFDIR}/register/register.properties",
        "file:${user.home}/.register.properties",
        "classpath:/register.properties"})
```