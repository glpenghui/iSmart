use ismart_demo_test;
TRUNCATE customer;
INSERT INTO customer (name, contact, telephone, email, remark) VALUES ('tcustomer1', 'tJack', '13512345678', 'jack@gmail.com', null);
INSERT INTO customer (name, contact, telephone, email, remark) VALUES ('tcustomer2', 'tRose', '测试中文联系地址', 'rose@gmail.com', null);