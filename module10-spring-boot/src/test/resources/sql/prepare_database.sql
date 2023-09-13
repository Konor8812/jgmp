create table ROLES
(
    ID   BIGINT auto_increment,
    ROLE CHARACTER VARYING(255),
    constraint PK_ROLES
        primary key (ID)
);

create table PUBLIC.USERS
(
    ID       BIGINT auto_increment,
    USERNAME CHARACTER VARYING(255) not null
        unique,
    PASSWORD CHARACTER VARYING(255) not null,
    constraint PK_USERS
        primary key (ID)
);

create table USER_ROLES
(
    USER_ID BIGINT,
    ROLE_ID BIGINT,
    constraint USER_ROLES_FK
        foreign key (USER_ID) references PUBLIC.USERS,
    constraint USER_ROLES_FK2
        foreign key (ROLE_ID) references PUBLIC.ROLES
);

