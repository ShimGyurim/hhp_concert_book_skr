delete from sugang_history;
delete from sugang_schedule;
delete from sugang;
delete from student;


insert into student(student_id)
select 1 from dual union
select 2 from dual union
select 3 from dual union
select 4 from dual union
select 5 from dual ;




insert into sugang(sugang_id,class_name)
select 1 , 'a' from dual union
select 2 , 'b' from dual union
select 3 , 'c' from dual union
select 4 , 'd' from dual union
select 5 , 'e' from dual;


insert into SUGANG_SCHEDULE (SUGANGSCHEDULE_ID , avail_num, classdate, sugang_id)
select 1 , 10, '20240801',1from dual union
select 2 , 10, '20240802',2 from dual union
select 3 , 10, '20240803',3 from dual union
select 4, 20, '20240804',4 from dual union
select 5 , 30, '20240805',5 from dual;
