select 1 from dual;

delete from payment;

delete from book;

delete from seat;

delete from concert_item;

delete from concert;


delete from wallet;

delete from wait_token;

delete from userinfo;

--insert into userinfo (user_id,user_login_id)
--select 1,'a' from dual union
--select 2,'b' from dual ;
--
--insert into wallet(amount,user_id,wallet_id,version)
--select 500,1,1,0 from dual;
--
--insert into concert(concert_id,concert_name,fee)
--select 1,'1',1 from dual;
--
--insert into concert_item(allseats,avail_seats,concert_id,concert_item_id,concertd,concertt)
--select 50,50,1,1,'20201010','000000' from dual;
--
--insert into seat(seat_id,seat_no,concert_item_id,is_use,version)
--select 0,0,1,false,0 from dual;
--
--insert into book(book_id,seat_id,user_id,version)
--select 1,0,1,0 from dual;