create table categories
(
    id        int identity
        primary key,
    name      nvarchar(255)
        unique,
    is_active bit default 1
)
go

create table discounts
(
    id         int identity
        primary key,
    percentage decimal(5, 2),
    start_date datetime not null,
    end_date   datetime not null
)
go

create table products
(
    id           bigint identity
        primary key,
    name         nvarchar(max),
    price        decimal(18, 2),
    stock        int,
    description  nvarchar(max),
    brand        nvarchar(255),
    discount_id  int      default NULL
        references discounts,
    category_id  int not null
        references categories,
    created_date datetime default getdate(),
    updated_date datetime default getdate(),
    is_active    bit      default 1
)
go

create table product_images
(
    id         bigint identity
        primary key,
    url        varchar(255),
    is_primary bit,
    product_id bigint not null
        references products
)
go

create table product_specifications
(
    id         int identity
        primary key,
    spec_name  nvarchar(255),
    spec_value nvarchar(max),
    product_id bigint not null
        references products
)
go

create table users
(
    id        bigint identity
        primary key,
    email     varchar(255)
        unique,
    password  varchar(255),
    name      nvarchar(255),
    avatar    varchar(255),
    gender    bit,
    birthday  datetime,
    phone     varchar(13)
        unique,
    address   nvarchar(255),
    role      varchar(50) default 'User',
    is_active bit         default 1
)
go

create table likes
(
    id           bigint identity
        primary key,
    user_id      bigint not null
        references users,
    product_id   bigint not null
        references products,
    created_date datetime default getdate(),
    constraint unique_user_product_like
        unique (user_id, product_id)
)
go

create table orders
(
    id           bigint identity
        primary key,
    status       nvarchar(50) default 'Pending',
    total_amount decimal(18, 2),
    user_id      bigint not null
        references users,
    created_date datetime     default getdate()
)
go

create table order_details
(
    id               bigint identity
        primary key,
    quantity         int,
    unit_price       decimal(18, 2),
    discount_percent decimal(5, 2),
    total_price      decimal(18, 2),
    order_id         bigint not null
        references orders,
    product_id       bigint not null
        references products
)
go

create table payments
(
    id             bigint identity
        primary key,
    method         nvarchar(50),
    amount         decimal(18, 2),
    status         nvarchar(50) default 'Pending',
    transaction_id varchar(max),
    order_id       bigint not null
        references orders,
    created_date   datetime     default getdate(),
    updated_date   datetime     default getdate()
)
go

create table product_reviews
(
    id           bigint identity
        primary key,
    rating       int,
    user_id      bigint not null
        references users,
    product_id   bigint not null
        references products,
    created_date datetime default getdate()
)
go

