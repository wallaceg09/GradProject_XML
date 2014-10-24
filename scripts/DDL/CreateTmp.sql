create table tmp(
	ident int,
	name varchar2(20),
	primary key (ident)
);

insert into tmp 
values (0, 'bob');

insert into tmp 
values (1, 'dylan');

insert into tmp 
values (2, 'anderson');