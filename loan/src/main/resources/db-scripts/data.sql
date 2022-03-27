insert into Loan
(loan_id, customer_id, installment_state, loan_request_date, loan_amount)
values
('98d8-5aff1cate580','cust1001','SIMULATION' ,CURRENT_TIMESTAMP(),339.32),
('98d8-5aff1cate581','cust1002','SIMULATION' ,CURRENT_TIMESTAMP(),115.43),
('98d8-5aff1cate582','cust1003','SIMULATION' ,CURRENT_TIMESTAMP(),239.39);

insert into Loan_Repayment_Plan
(loan_Repayment_Plan_Id, loan_id, loan_Due_Date, loan_Amount)
values
('asdee-2332a-321','98d8-5aff1cate580', CURRENT_TIMESTAMP()+14, 84.83),
('asdee-2332a-322','98d8-5aff1cate580', CURRENT_TIMESTAMP()+28, 84.83),
('asdee-2332a-323','98d8-5aff1cate580', CURRENT_TIMESTAMP()+42, 84.83),
('asdee-2332a-324','98d8-5aff1cate580', CURRENT_TIMESTAMP()+56, 84.83),

('asdee-2332a-325','98d8-5aff1cate581', CURRENT_TIMESTAMP()+14, 28.86),
('asdee-2332a-326','98d8-5aff1cate581', CURRENT_TIMESTAMP()+28, 28.86),
('asdee-2332a-327','98d8-5aff1cate581', CURRENT_TIMESTAMP()+42, 28.86),
('asdee-2332a-328','98d8-5aff1cate581', CURRENT_TIMESTAMP()+56, 28.86),

('asdee-2332a-329','98d8-5aff1cate582', CURRENT_TIMESTAMP()+14, 59.85),
('asdee-2332a-330','98d8-5aff1cate582', CURRENT_TIMESTAMP()+28, 59.85),
('asdee-2332a-331','98d8-5aff1cate582', CURRENT_TIMESTAMP()+42, 59.85),
('asdee-2332a-332','98d8-5aff1cate582', CURRENT_TIMESTAMP()+56, 59.85);