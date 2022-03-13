
-- 1000원 적립(만료)
insert into user_point (no, user_no, process_Date, point_Status, point_Amount) values(10000, 1, sysdate() - 370, 'CHARGE', 1000);
insert into user_point_detail(NO, user_no, POINT_AMOUNT, POINT_DETAIL_NO, POINT_STATUS, PROCESS_DATE, USER_POINT_NO) values (20000, 1, 1000, 20000, 'CHARGE', sysdate() - 370, 10000);

-- 1000원 적립
insert into user_point (no, user_no, process_Date, point_Status, point_Amount) values(10001, 1, sysdate() - 365, 'CHARGE', 1000);
insert into user_point_detail(NO, user_no, POINT_AMOUNT, POINT_DETAIL_NO, POINT_STATUS, PROCESS_DATE, USER_POINT_NO) values (20001, 1, 1000, 20001, 'CHARGE', sysdate() - 365, 10001);

-- 1000원 사용
insert into user_point (no, user_no, process_Date, point_Status, point_Amount) values(10002, 1, sysdate() - 364, 'USE', -1000);
insert into user_point_detail(NO, user_no, POINT_AMOUNT, POINT_DETAIL_NO, POINT_STATUS, PROCESS_DATE, USER_POINT_NO) values (20002, 1, -1000, 20001, 'USE', sysdate() - 364, 10002);

-- 1000원 적립
insert into user_point (no, user_no, process_Date, point_Status, point_Amount) values(10003, 1, sysdate() - 363, 'CHARGE', 1000);
insert into user_point_detail(NO, user_no, POINT_AMOUNT, POINT_DETAIL_NO, POINT_STATUS, PROCESS_DATE, USER_POINT_NO) values (20003, 1, 1000, 20003, 'CHARGE', sysdate() - 363, 10003);

-- 500원 사용
insert into user_point (no, user_no, process_Date, point_Status, point_Amount) values(10004, 1, sysdate() - 362, 'USE', -500);
insert into user_point_detail(NO, user_no, POINT_AMOUNT, POINT_DETAIL_NO, POINT_STATUS, PROCESS_DATE, USER_POINT_NO) values (20004, 1, -500, 20003, 'USE', sysdate() - 362, 10004);

-- 1000원 적립
insert into user_point (no, user_no, process_Date, point_Status, point_Amount) values(10005, 1, sysdate() - 361, 'CHARGE', 1000);
insert into user_point_detail(NO, user_no, POINT_AMOUNT, POINT_DETAIL_NO, POINT_STATUS, PROCESS_DATE, USER_POINT_NO) values (20005, 1, 1000, 20005, 'CHARGE', sysdate() - 361, 10005);
