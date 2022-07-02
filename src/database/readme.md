# Datenbank Installationsanleitung

Es wird ein Benutzer 'register' und ein 'register-su' angelegt. Jeweils mit dem
Passwort 'register'. Im Anschluss die Datenbanken 'register' und 'registertest'.

```
mysql -u root -h 127.0.0.1

source mysql-create-user.sql
source mysql-prod.sql
source mysql-test.sql
exit;
```

```
mysql -u registersu --password=register -D register -h 127.0.0.1
source mysql.sql
exit;
```


