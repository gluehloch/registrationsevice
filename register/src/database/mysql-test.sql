CREATE DATABASE `registertest` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

REVOKE ALL PRIVILEGES ON * . * FROM 'register'@'localhost';
REVOKE ALL PRIVILEGES ON * . * FROM 'register'@'%';

REVOKE ALL PRIVILEGES ON * . * FROM 'registersu'@'localhost';
REVOKE ALL PRIVILEGES ON * . * FROM 'registersu'@'%';

GRANT SELECT, INSERT, UPDATE, DELETE ON registertest.* TO 'register'@'localhost'
  WITH GRANT OPTION
  MAX_QUERIES_PER_HOUR 0
  MAX_CONNECTIONS_PER_HOUR 0
  MAX_UPDATES_PER_HOUR 0
  MAX_USER_CONNECTIONS 0;

GRANT SELECT, INSERT, UPDATE, DELETE ON registertest.* TO 'register'@'%'
  WITH GRANT OPTION
  MAX_QUERIES_PER_HOUR 0
  MAX_CONNECTIONS_PER_HOUR 0
  MAX_UPDATES_PER_HOUR 0
  MAX_USER_CONNECTIONS 0;

GRANT SELECT, INSERT, UPDATE, DELETE, CREATE,
  ALTER, INDEX, DROP, CREATE TEMPORARY TABLES, SHOW VIEW,
  CREATE ROUTINE, ALTER ROUTINE, EXECUTE, CREATE VIEW, EVENT, TRIGGER,
  LOCK TABLES
  ON registertest.* TO 'registersu'@'localhost'
  WITH GRANT OPTION
  MAX_QUERIES_PER_HOUR 0
  MAX_CONNECTIONS_PER_HOUR 0
  MAX_UPDATES_PER_HOUR 0
  MAX_USER_CONNECTIONS 0;

GRANT SELECT, INSERT, UPDATE, DELETE, CREATE,
  ALTER, INDEX, DROP, CREATE TEMPORARY TABLES, SHOW VIEW,
  CREATE ROUTINE, ALTER ROUTINE, EXECUTE, CREATE VIEW, EVENT, TRIGGER,
  LOCK TABLES
  ON registertest.* TO 'registersu'@'%'
  WITH GRANT OPTION
  MAX_QUERIES_PER_HOUR 0
  MAX_CONNECTIONS_PER_HOUR 0
  MAX_UPDATES_PER_HOUR 0
  MAX_USER_CONNECTIONS 0;