select * from member;

alter table member 
  add ( role varchar2(10) default 'USER');
  
insert into member
values( seq_member.nextval, 
        '관리자',
		'1234',
		'admin',
		'male',
		sysdate,
		'ADMIN' );
commit;		   