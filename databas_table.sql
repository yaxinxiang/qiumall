use qiumall;

##暂时不做改动
drop table if exists user;
create table user(
                     id integer auto_increment,
                     username varchar(30) not null unique ,
                     password varchar(30) not null,
                     phone varchar(11) default null unique ,
                     status numeric(1,0) default 1, ## 1：买家， 2： 商家， 3： 管理员 以后会用到
                         sex numeric(1,0) check ( sex in (1,2) ), ## 1为男 2为女
                         primary key (id)
);

##暂时不做改动
drop table if exists userimg;
create table userimg(
                        id integer auto_increment,
                        user_id integer not null ,
                        username varchar(30) not null ,
                        u_img_url varchar(200)not null ,
                        primary key (id)
);

##暂时不做改动
drop table if exists product;
create table product(
                        id integer auto_increment,
                        name varchar(50) not null ,
                        price numeric(11,2) default null,
                        category varchar(30) default null,
                        primary key (id)
);

##暂时不做改动
drop table if exists productimg;
create table productimg(
                           id integer auto_increment,
                           p_id integer,
                           name varchar(50),
                           p_img_url varchar(200),
                           primary key (id)
);




create table bus(
                    id integer not null ,
                    name integer ,
                    user_id integer not null ,
                    primary key (id),
                    foreign key (user_id) references user(id)
);


create table category(

);

create table orderitems(

);

create table orders(

);

create table qiu(

);

