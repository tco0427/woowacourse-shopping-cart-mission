SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE orders_detail;
TRUNCATE TABLE cart_item;
TRUNCATE TABLE orders;
TRUNCATE TABLE product;
TRUNCATE TABLE customer;
SET FOREIGN_KEY_CHECKS = 1;

insert into customer (email, password, username)
values ('puterism@email.com', 'password1!','puterism'),
       ('tanney-102@email.com', 'password1!', 'tanney-102'),
       ('jho2301@email.com', 'password1!', 'jho2301'),
       ('365kim@email.com', 'password1!', '365kim'),
       ('dudtjr913@email.com', 'password1!', 'dudtjr913'),
       ('jum0@email.com', 'password1!', 'jum0'),
       ('hyuuunjukim@email.com', 'password1!', 'hyuuunjukim'),
       ('zereight@email.com', 'password1!', 'zereight'),
       ('devhyun637@email.com', 'password1!', 'devhyun637'),
       ('swon3210@email.com', 'password1!', 'swon3210'),
       ('bigsaigon333@email.com', 'password1!', 'bigsaigon333'),
       ('yungo1846@email.com', 'password1!', 'yungo1846'),
       ('zigsong@email.com', 'password1!', 'zigsong'),
       ('iborymagic@email.com', 'password1!', 'iborymagic'),
       ('0307kwon@email.com', 'password1!', '0307kwon'),
       ('gwangyeol-im@email.com', 'password1!', 'gwangyeol-im'),
       ('shinsehantan@email.com', 'password1!', 'shinsehantan'),
       ('ddongule@email.com', 'password1!', 'ddongule'),
       ('seojihwan@email.com', 'password1!', 'seojihwan'),
       ('0imbean0@email.com', 'password1!', '0imbean0'),
       ('sunyoungkwon@email.com', 'password1!', 'sunyoungkwon'),
       ('hchayan@email.com', 'password1!', 'hchayan'),
       ('2sooy@email.com', 'password1!', '2sooy'),
       ('yujo11@email.com', 'password1!', 'yujo11'),
       ('sunhpark4@email.com2', 'password1!', 'sunhpark42')
;
