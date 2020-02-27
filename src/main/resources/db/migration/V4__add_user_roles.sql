alter table usr add column password varchar(255) not null;

create table user_role
(
    user_id varchar(255) not null,
    roles   varchar(255)
);

alter table if exists user_role add constraint
    FKfpm8swft53ulq2hl11yplpr5 foreign key (user_id) references usr;