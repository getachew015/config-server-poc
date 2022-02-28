insert into CUSTOMER
(customer_ref_number,customer_id, cust_first_name, cust_last_name)
values
('98d8-5aff1cate580','cust1001','Jhon','Doe'),
('1698-5aff1cadc221','cust1002','Silvester', 'Bekalu'),
('y672-5aff1cadc232','cust1003','Emma', 'Brooks'),
('87w4-5aff1cadc278','cust1004','Mike', 'Gialianni'),
('90g2-5aff1cadc246','cust1005','Jessica', 'Alba');

insert into BANK_ACCOUNTS
(account_number, customer_id, account_type, account_open_date, account_balance)
values
('asdee-2332a-321','cust1001','CHECKING', CURRENT_TIMESTAMP(), 1212),
('asdee-2332a-322','cust1002','SAVING', CURRENT_TIMESTAMP(), 4312),
('asdee-2332a-323','cust1003','CHECKING', CURRENT_TIMESTAMP(), 1212),
('asdee-2332a-324','cust1002','CHECKING', CURRENT_TIMESTAMP(), 2312),
('asdee-2332a-325','cust1004','LOAN', CURRENT_TIMESTAMP(), -250),
('asdee-2332a-326','cust1005','CHECKING', CURRENT_TIMESTAMP(), 4233);