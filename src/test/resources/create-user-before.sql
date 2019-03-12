delete from user_role;
delete from usr;

insert into usr(id, username, password, active) values
(1, 'admin', '202cb962ac59075b964b07152d234b70', true),
(2, 'testuser', '{67aoO1U4eqnbMFjtZDGVMHWl2gv9VfCrBMVny+RXxCQ=}d06ff1d11534b5219a32ad99de74b5b9', true);

insert into user_role(user_id, roles) values
(1, 'ADMIN'), (1, 'USER'),
(2, 'USER');
