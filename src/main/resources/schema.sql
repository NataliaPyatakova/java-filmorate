CREATE TABLE IF NOT EXISTS PUBLIC.GENRES
(
    GENRE_ID   INTEGER auto_increment,
    GENRE_NAME CHARACTER VARYING(50) not null
    constraint GENRES_UNIQUE unique,
    constraint GENRES_PK primary key (GENRE_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.RATINGS
(
    RATING_ID   INTEGER auto_increment,
    RATING_NAME CHARACTER VARYING(10) not null
    constraint RATINGS_UNIQUE unique,
    constraint RATINGS_PK primary key (RATING_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.FILMS
(
    FILM_ID      INTEGER auto_increment,
    FILM_NAME    CHARACTER VARYING(100) not null,
    DESCRIPTION  CHARACTER VARYING(200),
    RELEASE_DATE DATE                   not null,
    DURATION     INTEGER                not null,
    RATING_ID    INTEGER,
    constraint FILMS_PK primary key (FILM_ID),
    constraint FILMS_RATINGS_FK foreign key (RATING_ID) references PUBLIC.RATINGS
);

CREATE TABLE IF NOT EXISTS PUBLIC.GENRES_RELATION
(
    GENRES_RELATION_ID INTEGER auto_increment,
    FILM_ID            INTEGER not null,
    GENRE_ID           INTEGER not null,
    constraint GENRES_RELATION_PK primary key (GENRES_RELATION_ID),
    constraint GENRES_RELATION_FILMS_FK foreign key (FILM_ID) references PUBLIC.FILMS,
    constraint GENRES_RELATION_GENRES_FK foreign key (GENRE_ID) references PUBLIC.GENRES
);

CREATE TABLE IF NOT EXISTS PUBLIC.USERS
(
    USER_ID  INTEGER auto_increment,
    EMAIL    CHARACTER VARYING(50) not null
    constraint USERS_UNIQUE_EMAIL unique,
    LOGIN    CHARACTER VARYING(50) not null
    constraint USERS_UNIQUE_LOGIN unique,
    USERNAME CHARACTER VARYING(50) not null
    constraint USERS_UNIQUE_USERNAME unique,
    BIRTHDAY DATE                  not null,
    constraint USERS_PK primary key (USER_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.FRIENDS_RELATION
(
    FRIENDS_RELATION_ID INTEGER auto_increment,
    USER_ID             INTEGER not null,
    FRIEND_ID           INTEGER not null,
    constraint FRIENDS_RELATION_PK primary key (FRIENDS_RELATION_ID),
    constraint FRIENDS_RELATION_USERS_FRIEND_FK foreign key (FRIEND_ID) references PUBLIC.USERS,
    constraint FRIENDS_RELATION_USERS_USER_FK foreign key (USER_ID) references PUBLIC.USERS
);

CREATE TABLE IF NOT EXISTS PUBLIC.LIKES_RELATION
(
    LIKES_RELATION_ID INTEGER auto_increment,
    FILM_ID           INTEGER not null,
    USER_ID           INTEGER not null,
    constraint LIKES_RELATION_PK primary key (LIKES_RELATION_ID),
    constraint LIKES_RELATION_FILMS_FK foreign key (FILM_ID) references PUBLIC.FILMS,
    constraint LIKES_RELATION_USERS_FK foreign key (USER_ID) references PUBLIC.USERS
);