select * from member;

drop sequence seq_member;
create sequence seq_member
start with 1
increment by 1
maxvalue 9999999999;

alter table member add ( role VARCHAR2(10) default 'USER');

insert into member
values( seq_member.nextval, '관리자', 'admin', '1234', 'male', sysdate, 'ADMIN' ); 
commit;
