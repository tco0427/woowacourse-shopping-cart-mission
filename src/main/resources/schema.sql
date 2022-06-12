drop table if exists orders_detail;

drop table if exists order;

drop table if exists cart_item;

drop table if exists product;

drop table if exists image;

drop table if exists customer;

create table customer
(
    id       bigint              not null auto_increment,
    email    varchar(255) unique not null,
    password varchar(12)        not null,
    username varchar(10)        not null,
    primary key (id)
) engine=InnoDB default charset=utf8mb4;

create table image
(
    id bigint not null auto_increment,
    image_url varchar(255),
    image_alt varchar(255),
    primary key (id)
) engine=InnoDB default charset=utf8mb4;

create table product
(
    id        bigint       not null auto_increment,
    name      varchar(255) not null,
    price     integer      not null,
    stock_quantity integer not null,
    image_id bigint not null,
    primary key (id)
) engine=InnoDB default charset=utf8mb4;

alter table product
    add constraint fk_product_to_image
        foreign key (image_id) references image (id) ON DELETE CASCADE;

create table cart_item
(
    id          bigint not null auto_increment,
    customer_id bigint not null,
    product_id  bigint not null,
    quantity bigint not null,
    primary key (id)
) engine=InnoDB default charset=utf8mb4;

alter table cart_item
    add constraint fk_cart_item_to_customer
        foreign key (customer_id) references customer (id) ON DELETE CASCADE;

alter table cart_item
    add constraint fk_cart_item_to_product
        foreign key (product_id) references product (id) ON DELETE CASCADE;

create table order
(
    id          bigint not null auto_increment,
    customer_id bigint not null,
    primary key (id)
) engine=InnoDB default charset=utf8mb4;

alter table order
    add constraint fk_orders_to_customer
        foreign key (customer_id) references customer (id) ON DELETE CASCADE;

create table orders_detail
(
    id         bigint  not null auto_increment,
    orders_id  bigint  not null,
    product_id bigint  not null,
    quantity   integer not null,
    primary key (id)
) engine=InnoDB default charset=utf8mb4;

alter table orders_detail
    add constraint fk_orders_detail_to_orders
        foreign key (orders_id) references order (id) ON DELETE CASCADE;

alter table orders_detail
    add constraint fk_orders_detail_to_product
        foreign key (product_id) references product (id) ON DELETE CASCADE;
