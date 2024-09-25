create sequence author_entity_seq start with 1 increment by 50;
create sequence languages_entity_seq start with 1 increment by 50;
create table author_entity
(
    id   bigint       not null,
    name varchar(255) not null unique,
    primary key (id)
);
create table languages_entity
(
    id       bigint       not null,
    language varchar(255) not null unique,
    primary key (id)
);
create table local_book_entity
(
    publish_year integer,
    id           bigint generated by default as identity,
    title        varchar(255),
    primary key (id)
);
create table local_book_entity_authors
(
    authors_id           bigint not null,
    local_book_entity_id bigint not null
);
create table local_book_entity_languages
(
    languages_id         bigint not null,
    local_book_entity_id bigint not null
);
alter table if exists local_book_entity_authors
    add constraint FK32of78ythrfhdc0c4aqlg6xro foreign key (authors_id) references author_entity;
alter table if exists local_book_entity_authors
    add constraint FKksidcw6r0eefqfqva4qthpxq8 foreign key (local_book_entity_id) references local_book_entity;
alter table if exists local_book_entity_languages
    add constraint FKj7jk0j8qbof8ow25dm0pb4q8y foreign key (languages_id) references languages_entity;
alter table if exists local_book_entity_languages
    add constraint FKjpcqwavk0cexisprtomfomms2 foreign key (local_book_entity_id) references local_book_entity;
