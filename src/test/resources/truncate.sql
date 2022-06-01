SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE orders_detail;
TRUNCATE TABLE cart_item;
TRUNCATE TABLE orders;
TRUNCATE TABLE product;
TRUNCATE TABLE customer;
SET FOREIGN_KEY_CHECKS = 1;

insert into customer (email, password, username)
values ('puterism@email.com', 'password1!','puterism'),
       ('gwangyeol-im@email.com', 'password1!', 'gwangyeol');
