
select 'Start installation of registerservice 0.0.1 MySQL schema.' as INFO;
select version();

drop table if exists user_registration;
drop table if exists user_session;
drop table if exists user;

create table user_registration (
    id bigint not null auto_increment,
    nickname varchar(20) not null unique,
    name varchar(50) not null,
    firstname varchar(50) not null,
    password varchar(60) not null,
    email varchar(50) not null,
    created datetime not null,
    token varchar(2048) not null,
    primary key(id)
) ENGINE=InnoDB;

create table user (
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
    primary key(id)
) ENGINE=InnoDB;

create table user_session (
    id bigint not null auto_increment,
    user_ref bigint,
    token varchar(2048) not null comment 'Session Token',
    login datetime comment 'Login date and time',
    logout datetime comment 'Logout date and time',
    remoteaddress varchar(30) comment 'IP address',
    browser varchar(200) comment 'Browser',
    primary key(id)
) ENGINE=InnoDB;

alter table user_session
    add constraint fk_user_session_user
    foreign key (user_ref)
    references user(id);
