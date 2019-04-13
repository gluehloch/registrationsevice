
select 'Start installation of registerservice 0.0.1 MySQL schema.' as INFO;
select version();

drop table if exists cookie;
drop table if exists registration;
drop table if exists useraccount_session;
drop table if exists useraccount_application;
drop table if exists useraccount;
drop table if exists application;

create table cookie(
    id bigint not null auto_increment,
    remoteaddress VARCHAR(100) not null comment 'the user remote address',
    browser VARCHAR(200) not null comment 'the user browser',
    created datetime not null comment 'entry creation time',
    acceptcookie bit comment 'user accepts cookies',
    primary key (id)
) ENGINE=InnoDB;

create table registration (
    id bigint not null auto_increment,
    nickname varchar(20) not null unique,
    name varchar(50) not null,
    firstname varchar(50) not null,
    password varchar(60) not null,
    email varchar(50) not null comment 'email address',
    created datetime not null comment 'cretion time',
    token varchar(60) not null unique comment 'token to confirm email address',
    application varchar(50) not null comment 'name of the appliction',
    confirmed bit comment 'registration completed successful',
    acceptmail bit comment 'user accepts emails',
    acceptcookie bit comment 'user accepts cookies',
    primary key(id)
) ENGINE=InnoDB;

create table useraccount (
    id bigint not null auto_increment,
    nickname varchar(20) not null unique,
    name varchar(50) not null,
    firstname varchar(50) not null,
    password varchar(60) not null,
    email varchar(50) not null,
    created datetime not null,
    last_change datetime comment 'Last password change',
    enabled bit comment 'account enabled',
    expired bit comment 'password expired',
    locked bit comment 'account locked',
    credential_expired bit comment 'credential expired?',
    acceptmail bit comment 'user accepts emails',
    acceptcookie bit comment 'user accepts cookies',
    primary key(id)
) ENGINE=InnoDB;

create table application (
    id bigint not null auto_increment,
    name varchar(50) not null unique,
    description varchar(500) not null,
    primary key(id)
) ENGINE=InnoDB;

create table useraccount_session (
    id bigint not null auto_increment,
    useraccount_ref bigint,
    token varchar(2048) not null comment 'Session Token',
    login datetime comment 'Login date and time',
    logout datetime comment 'Logout date and time',
    remoteaddress varchar(30) comment 'IP address',
    browser varchar(200) comment 'Browser',
    primary key(id)
) ENGINE=InnoDB;

create table useraccount_application (
    useraccount_ref bigint not null,
    application_ref bigint not null,
    primary key(useraccount_ref, application_ref)
) ENGINE=InnoDB;

alter table useraccount_session
    add constraint fk_useraccount_session_user
    foreign key (useraccount_ref)
    references useraccount(id);

alter table useraccount_application
    add constraint fk_useracc_app_useracc
    foreign key (useraccount_ref)
    references useraccount(id);
    
alter table  useraccount_application
    add constraint fk_useracc_app_app
    foreign key (application_ref)
    references application(id);
