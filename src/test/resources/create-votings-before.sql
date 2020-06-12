delete from voting;
delete from vote_option;

-- public voting key - see properties.yaml - voting.public.key;
insert into voting(id, creation_date, is_private_voting, is_protected_voting, is_checking_ip_voting, voting_key, voting_title, owner_id) values

(1, '2020-01-05 16:14:02.616', false, false, false, 'public', 'search1', (select id from usr u where u.id = '3')),
(2, '2020-02-05 16:14:02.616', false, false, false, 'public', 'search2', (select id from usr u where u.id = '3')),
(3, '2020-03-05 16:14:02.616', false, false, false, 'public', 'search3', (select id from usr u where u.id = '3')),

(4, '2020-04-05 16:14:02.616', true, false, false, 'public', 'private1', (select id from usr u where u.id = '3')),
(5, '2020-05-05 16:14:02.616', true, false, false, 'public', 'private2', (select id from usr u where u.id = '3')),

(6, '2020-06-05 16:14:02.616', false, true, false, 'notPublic', 'protected1', (select id from usr u where u.id = '3')),
(7, '2020-07-05 16:14:02.616', false, true, false, 'notPublic', 'protected2', (select id from usr u where u.id = '3')),

(8, '2020-06-05 16:14:02.616', true, true, false, 'notPublic', 'protected1', (select id from usr u where u.id = '1')),
(9, '2020-07-05 16:14:02.616', true, true, false, 'notPublic', 'protected2', (select id from usr u where u.id = '1'));

insert into vote_option(id, pluses, vote_discription, vote_id) values
(1, 7, 'disc', 1),
(2, 8, 'disc', 1),
(3, 9, 'disc', 1),

(4, 1, 'disc', 2),
(5, 2, 'disc', 2),
(6, 3, 'disc', 2),

(7, 10, 'disc', 3),
(8, 11, 'disc', 3),
(9, 12, 'disc', 3),

(10, 4, 'disc', 4),
(11, 5, 'disc', 4),
(12, 6, 'disc', 4),

(13, 1, 'disc', 5),
(14, 0, 'disc', 5),
(15, 1, 'disc', 5),

(16, 0, 'disc', 6),
(17, 6, 'disc', 6),
(18, 0, 'disc', 6),

(19, 0, 'disc', 7),
(20, 9, 'disc', 7),
(21, 0, 'disc', 7);

alter sequence hibernate_sequence restart with 100;