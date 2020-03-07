delete from voting;
delete from vote_option;
-- public voting key - see properties.yaml - voting.public.key;
insert into voting(id, creation_date, is_private_voting, total_votes, is_protected_voting, voting_key, voting_title, owner_id) values

(1, '2020-01-05 16:14:02.616', false, 1, false, 'public', 'search1', (select id from usr u where u.id = '3')),
(2, '2020-02-05 16:14:02.616', false, 2, false, 'public', 'search2', (select id from usr u where u.id = '3')),
(3, '2020-03-05 16:14:02.616', false, 3, false, 'public', 'search3', (select id from usr u where u.id = '3')),

(4, '2020-04-05 16:14:02.616', true, 4, false, 'public', 'private1', (select id from usr u where u.id = '3')),
(5, '2020-05-05 16:14:02.616', true, 5, false, 'public', 'private2', (select id from usr u where u.id = '3')),

(6, '2020-06-05 16:14:02.616', false, 6, true, 'notPublic', 'protected1', (select id from usr u where u.id = '3')),
(7, '2020-07-05 16:14:02.616', false, 7, true, 'notPublic', 'protected2', (select id from usr u where u.id = '3')),

(8, '2020-06-05 16:14:02.616', true, 6, true, 'notPublic', 'protected1', (select id from usr u where u.id = '1')),
(9, '2020-07-05 16:14:02.616', true, 7, true, 'notPublic', 'protected2', (select id from usr u where u.id = '1'));

insert into vote_option(id, pluses, vote_discription, vote_id) values
(1, 0, 'disc', 1),
(2, 0, 'disc', 1),
(3, 0, 'disc', 1),

(4, 0, 'disc', 2),
(5, 0, 'disc', 2),
(6, 0, 'disc', 2),

(7, 0, 'disc', 3),
(8, 0, 'disc', 3),
(9, 0, 'disc', 3),

(10, 0, 'disc', 4),
(11, 0, 'disc', 4),
(12, 0, 'disc', 4),

(13, 0, 'disc', 5),
(14, 0, 'disc', 5),
(15, 0, 'disc', 5),

(16, 0, 'disc', 6),
(17, 0, 'disc', 6),
(18, 0, 'disc', 6),

(19, 0, 'disc', 7),
(20, 0, 'disc', 7),
(21, 0, 'disc', 7);