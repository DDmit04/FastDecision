delete from user_role;
delete from usr;

insert into usr(id, username, password) values
('1', 'adminUsername', '$2a$08$fNUHI3FnO3cbT6VAcClJOOsIq93f2101ud2RAKiZFAh7Y2h.oFRzC'),
('2', 'otherAdminUsername', '$2a$08$fNUHI3FnO3cbT6VAcClJOOsIq93f2101ud2RAKiZFAh7Y2h.oFRzC'),
('3', 'commonUsername', '$2a$08$fNUHI3FnO3cbT6VAcClJOOsIq93f2101ud2RAKiZFAh7Y2h.oFRzC'),
('4', 'otherCommonUsername', '$2a$08$fNUHI3FnO3cbT6VAcClJOOsIq93f2101ud2RAKiZFAh7Y2h.oFRzC');

insert into user_role(user_id, roles) values
('1', 'ADMIN'),
('2', 'ADMIN'),
('3', 'USER'),
('4', 'USER');

alter sequence hibernate_sequence restart with 100;
