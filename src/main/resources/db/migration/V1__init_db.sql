create sequence hibernate_sequence start 1 increment 1;

create table usr
(
    id       varchar(255) not null,
    email    varchar(255),
    user_pic varchar(255),
    username varchar(255),
    primary key (id)
);
create table voted_users_ips
(
    vote_option_id int8 not null,
    voted_ips      varchar(255)
);
create table vote_option
(
    id               int8 not null,
    pluses           int8 not null,
    vote_discription varchar(255),
    vote_id          int8,
    primary key (id)
);
create table voting
(
    id                int8    not null,
    creation_date     timestamp,
    is_private_voting boolean not null,
    total_votes       int4    not null,
    voting_title      varchar(255),
    owner_id          varchar(255),
    primary key (id)
);

create table user_role
(
    user_id varchar(255) not null,
    roles   varchar(255)
);

alter table if exists user_role
    add constraint FKfpm8swft53ulq2hl11yplpr5 foreign key (user_id) references usr;
alter table if exists voted_users_ips
    add constraint FKpq6nawaf6tbshxqcvtxlo2hov foreign key (vote_option_id) references voting;
alter table if exists vote_option
    add constraint FKf61kqjnymadv0kkqfh1u3xlsq foreign key (vote_id) references voting;
alter table if exists voting
    add constraint FKhh8hxbj8vc1xchs5f3864vrdk foreign key (owner_id) references usr;