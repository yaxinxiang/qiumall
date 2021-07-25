use qiumall;
drop table user;
create table user(
                     id integer auto_increment,
                     username varchar(30) not null unique ,
                     password varchar(30) not null,
                     phone varchar(11) default null unique ,
                     sex numeric(1,0) check ( sex in (1,2) ),
                     primary key (id)
);

drop table userimg;
create table userimg(
                        userid integer,
                        uimgurl varchar(200),
                        primary key (userid, uimgurl)
);

create table bus(
                    id integer not null ,
                    name integer ,
                    user_id integer not null ,
                    primary key (id),
                    foreign key (user_id) references user(id)
);

drop table product;
# 测试用具体还需要商议
create table product(
                        id integer auto_increment,
                        name varchar(50) not null ,
                        price numeric(11,2) default null,
                        category varchar(30) default null,
                        primary key (id)
);

drop table productimg;
create table productimg(
                           productid integer,
                           pimgurl varchar(200),
                           primary key (productid, pimgurl),
                           foreign key (productid) references product(id) on delete cascade
);

create table category(

);

create table orderitems(

);

create table orders(

);

create table qiu(

);

