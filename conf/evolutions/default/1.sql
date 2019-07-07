# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table comment (
  id                            bigint not null,
  content                       varchar(255),
  user_email_id                 varchar(255),
  productcomment_product_id     bigint,
  constraint pk_comment primary key (id)
);
create sequence comment_seq;

create table coupon_receive_center (
  user_email                    varchar(255) not null,
  constraint pk_coupon_receive_center primary key (user_email)
);

create table coupon_receive_center_coupons (
  coupon_receive_center_user_email varchar(255) not null,
  coupons_id                    bigint not null,
  constraint pk_coupon_receive_center_coupons primary key (coupon_receive_center_user_email,coupons_id)
);

create table coupon_send_center (
  user_email                    varchar(255) not null,
  product_id                    varchar(255),
  constraint pk_coupon_send_center primary key (user_email)
);

create table coupons (
  id                            bigint not null,
  couponprice                   varchar(255),
  sender_user_email             varchar(255),
  constraint pk_coupons primary key (id)
);
create sequence coupons_seq;

create table credit_card (
  id                            bigint not null,
  full_name                     varchar(255),
  address                       varchar(255),
  city                          varchar(255),
  state                         varchar(255),
  zip                           varchar(255),
  country                       varchar(255),
  phone                         varchar(255),
  name                          varchar(255),
  credit_card_number            varchar(255),
  expiry_month                  integer,
  expiry_year                   integer,
  cvv                           integer,
  constraint pk_credit_card primary key (id)
);
create sequence credit_card_seq;

create table delivery (
  id                            bigint not null,
  delivery_method               varchar(255),
  constraint pk_delivery primary key (id)
);
create sequence delivery_seq;

create table inventory (
  user_email                    varchar(255) not null,
  constraint pk_inventory primary key (user_email)
);

create table notification_message (
  id                            bigint not null,
  content                       varchar(255),
  sender_user_email             varchar(255),
  constraint pk_notification_message primary key (id)
);
create sequence notification_message_seq;

create table notification_receive_center (
  user_email                    varchar(255) not null,
  constraint pk_notification_receive_center primary key (user_email)
);

create table notification_receive_center_notification_message (
  notification_receive_center_user_email varchar(255) not null,
  notification_message_id       bigint not null,
  constraint pk_notification_receive_center_notification_message primary key (notification_receive_center_user_email,notification_message_id)
);

create table notification_send_center (
  user_email                    varchar(255) not null,
  constraint pk_notification_send_center primary key (user_email)
);

create table product (
  id                            bigint not null,
  name                          varchar(255),
  price                         double,
  developer_email_id            varchar(255),
  shopping_cart_id              bigint,
  constraint pk_product primary key (id)
);
create sequence product_seq;

create table product_user_info (
  product_id                    bigint not null,
  user_info_email_id            varchar(255) not null,
  constraint pk_product_user_info primary key (product_id,user_info_email_id)
);

create table product_comment (
  product_id                    bigint not null,
  constraint pk_product_comment primary key (product_id)
);
create sequence product_comment_seq;

create table product_order (
  id                            bigint not null,
  product_id                    bigint,
  delivery                      varchar(255),
  coupon                        varchar(255),
  purchase_order_id             bigint,
  shopping_cart_id              bigint,
  quantity                      integer,
  inventory_user_email          varchar(255),
  status_state                  varchar(255),
  constraint pk_product_order primary key (id)
);
create sequence product_order_seq;

create table product_status (
  state                         varchar(255) not null,
  constraint pk_product_status primary key (state)
);

create table purchase_history (
  user_email                    varchar(255) not null,
  constraint pk_purchase_history primary key (user_email)
);

create table purchase_order (
  id                            bigint not null,
  customer_email_id             varchar(255),
  history_user_email            varchar(255),
  address                       varchar(255),
  gift_card                     varchar(255),
  constraint pk_purchase_order primary key (id)
);
create sequence purchase_order_seq;

create table question (
  id                            bigint not null,
  content                       varchar(255),
  answer                        varchar(255),
  product_id                    bigint,
  cur_index                     integer,
  poster_user_email             varchar(255),
  receiver_user_email           varchar(255),
  constraint pk_question primary key (id)
);
create sequence question_seq;

create table question_post_center (
  user_email                    varchar(255) not null,
  constraint pk_question_post_center primary key (user_email)
);

create table question_receive_center (
  user_email                    varchar(255) not null,
  constraint pk_question_receive_center primary key (user_email)
);

create table shopping_cart (
  id                            bigint not null,
  constraint pk_shopping_cart primary key (id)
);
create sequence shopping_cart_seq;

create table user_info (
  email_id                      varchar(255) not null,
  name                          varchar(255),
  password                      varchar(255),
  type                          varchar(255),
  shopping_cart_id              bigint,
  constraint pk_user_info primary key (email_id)
);

alter table comment add constraint fk_comment_user_email_id foreign key (user_email_id) references user_info (email_id) on delete restrict on update restrict;
create index ix_comment_user_email_id on comment (user_email_id);

alter table comment add constraint fk_comment_productcomment_product_id foreign key (productcomment_product_id) references product_comment (product_id) on delete restrict on update restrict;
create index ix_comment_productcomment_product_id on comment (productcomment_product_id);

alter table coupon_receive_center_coupons add constraint fk_coupon_receive_center_coupons_coupon_receive_center foreign key (coupon_receive_center_user_email) references coupon_receive_center (user_email) on delete restrict on update restrict;
create index ix_coupon_receive_center_coupons_coupon_receive_center on coupon_receive_center_coupons (coupon_receive_center_user_email);

alter table coupon_receive_center_coupons add constraint fk_coupon_receive_center_coupons_coupons foreign key (coupons_id) references coupons (id) on delete restrict on update restrict;
create index ix_coupon_receive_center_coupons_coupons on coupon_receive_center_coupons (coupons_id);

alter table coupons add constraint fk_coupons_sender_user_email foreign key (sender_user_email) references coupon_send_center (user_email) on delete restrict on update restrict;
create index ix_coupons_sender_user_email on coupons (sender_user_email);

alter table notification_message add constraint fk_notification_message_sender_user_email foreign key (sender_user_email) references notification_send_center (user_email) on delete restrict on update restrict;
create index ix_notification_message_sender_user_email on notification_message (sender_user_email);

alter table notification_receive_center_notification_message add constraint fk_notification_receive_center_notification_message_notif_1 foreign key (notification_receive_center_user_email) references notification_receive_center (user_email) on delete restrict on update restrict;
create index ix_notification_receive_center_notification_message_notif_1 on notification_receive_center_notification_message (notification_receive_center_user_email);

alter table notification_receive_center_notification_message add constraint fk_notification_receive_center_notification_message_notif_2 foreign key (notification_message_id) references notification_message (id) on delete restrict on update restrict;
create index ix_notification_receive_center_notification_message_notif_2 on notification_receive_center_notification_message (notification_message_id);

alter table product add constraint fk_product_developer_email_id foreign key (developer_email_id) references user_info (email_id) on delete restrict on update restrict;
create index ix_product_developer_email_id on product (developer_email_id);

alter table product add constraint fk_product_shopping_cart_id foreign key (shopping_cart_id) references shopping_cart (id) on delete restrict on update restrict;
create index ix_product_shopping_cart_id on product (shopping_cart_id);

alter table product_user_info add constraint fk_product_user_info_product foreign key (product_id) references product (id) on delete restrict on update restrict;
create index ix_product_user_info_product on product_user_info (product_id);

alter table product_user_info add constraint fk_product_user_info_user_info foreign key (user_info_email_id) references user_info (email_id) on delete restrict on update restrict;
create index ix_product_user_info_user_info on product_user_info (user_info_email_id);

alter table product_order add constraint fk_product_order_purchase_order_id foreign key (purchase_order_id) references purchase_order (id) on delete restrict on update restrict;
create index ix_product_order_purchase_order_id on product_order (purchase_order_id);

alter table product_order add constraint fk_product_order_shopping_cart_id foreign key (shopping_cart_id) references shopping_cart (id) on delete restrict on update restrict;
create index ix_product_order_shopping_cart_id on product_order (shopping_cart_id);

alter table product_order add constraint fk_product_order_inventory_user_email foreign key (inventory_user_email) references inventory (user_email) on delete restrict on update restrict;
create index ix_product_order_inventory_user_email on product_order (inventory_user_email);

alter table product_order add constraint fk_product_order_status_state foreign key (status_state) references product_status (state) on delete restrict on update restrict;
create index ix_product_order_status_state on product_order (status_state);

alter table purchase_order add constraint fk_purchase_order_customer_email_id foreign key (customer_email_id) references user_info (email_id) on delete restrict on update restrict;
create index ix_purchase_order_customer_email_id on purchase_order (customer_email_id);

alter table purchase_order add constraint fk_purchase_order_history_user_email foreign key (history_user_email) references purchase_history (user_email) on delete restrict on update restrict;
create index ix_purchase_order_history_user_email on purchase_order (history_user_email);

alter table question add constraint fk_question_poster_user_email foreign key (poster_user_email) references question_post_center (user_email) on delete restrict on update restrict;
create index ix_question_poster_user_email on question (poster_user_email);

alter table question add constraint fk_question_receiver_user_email foreign key (receiver_user_email) references question_receive_center (user_email) on delete restrict on update restrict;
create index ix_question_receiver_user_email on question (receiver_user_email);


# --- !Downs

alter table comment drop constraint if exists fk_comment_user_email_id;
drop index if exists ix_comment_user_email_id;

alter table comment drop constraint if exists fk_comment_productcomment_product_id;
drop index if exists ix_comment_productcomment_product_id;

alter table coupon_receive_center_coupons drop constraint if exists fk_coupon_receive_center_coupons_coupon_receive_center;
drop index if exists ix_coupon_receive_center_coupons_coupon_receive_center;

alter table coupon_receive_center_coupons drop constraint if exists fk_coupon_receive_center_coupons_coupons;
drop index if exists ix_coupon_receive_center_coupons_coupons;

alter table coupons drop constraint if exists fk_coupons_sender_user_email;
drop index if exists ix_coupons_sender_user_email;

alter table notification_message drop constraint if exists fk_notification_message_sender_user_email;
drop index if exists ix_notification_message_sender_user_email;

alter table notification_receive_center_notification_message drop constraint if exists fk_notification_receive_center_notification_message_notif_1;
drop index if exists ix_notification_receive_center_notification_message_notif_1;

alter table notification_receive_center_notification_message drop constraint if exists fk_notification_receive_center_notification_message_notif_2;
drop index if exists ix_notification_receive_center_notification_message_notif_2;

alter table product drop constraint if exists fk_product_developer_email_id;
drop index if exists ix_product_developer_email_id;

alter table product drop constraint if exists fk_product_shopping_cart_id;
drop index if exists ix_product_shopping_cart_id;

alter table product_user_info drop constraint if exists fk_product_user_info_product;
drop index if exists ix_product_user_info_product;

alter table product_user_info drop constraint if exists fk_product_user_info_user_info;
drop index if exists ix_product_user_info_user_info;

alter table product_order drop constraint if exists fk_product_order_purchase_order_id;
drop index if exists ix_product_order_purchase_order_id;

alter table product_order drop constraint if exists fk_product_order_shopping_cart_id;
drop index if exists ix_product_order_shopping_cart_id;

alter table product_order drop constraint if exists fk_product_order_inventory_user_email;
drop index if exists ix_product_order_inventory_user_email;

alter table product_order drop constraint if exists fk_product_order_status_state;
drop index if exists ix_product_order_status_state;

alter table purchase_order drop constraint if exists fk_purchase_order_customer_email_id;
drop index if exists ix_purchase_order_customer_email_id;

alter table purchase_order drop constraint if exists fk_purchase_order_history_user_email;
drop index if exists ix_purchase_order_history_user_email;

alter table question drop constraint if exists fk_question_poster_user_email;
drop index if exists ix_question_poster_user_email;

alter table question drop constraint if exists fk_question_receiver_user_email;
drop index if exists ix_question_receiver_user_email;

drop table if exists comment;
drop sequence if exists comment_seq;

drop table if exists coupon_receive_center;

drop table if exists coupon_receive_center_coupons;

drop table if exists coupon_send_center;

drop table if exists coupons;
drop sequence if exists coupons_seq;

drop table if exists credit_card;
drop sequence if exists credit_card_seq;

drop table if exists delivery;
drop sequence if exists delivery_seq;

drop table if exists inventory;

drop table if exists notification_message;
drop sequence if exists notification_message_seq;

drop table if exists notification_receive_center;

drop table if exists notification_receive_center_notification_message;

drop table if exists notification_send_center;

drop table if exists product;
drop sequence if exists product_seq;

drop table if exists product_user_info;

drop table if exists product_comment;
drop sequence if exists product_comment_seq;

drop table if exists product_order;
drop sequence if exists product_order_seq;

drop table if exists product_status;

drop table if exists purchase_history;

drop table if exists purchase_order;
drop sequence if exists purchase_order_seq;

drop table if exists question;
drop sequence if exists question_seq;

drop table if exists question_post_center;

drop table if exists question_receive_center;

drop table if exists shopping_cart;
drop sequence if exists shopping_cart_seq;

drop table if exists user_info;

