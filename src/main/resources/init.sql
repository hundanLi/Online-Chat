create table tbl_authority
(
  authority_id   varchar(255) not null
    primary key,
  authority_name varchar(255) not null,
  constraint authority_name
  unique (authority_name)
);

create table tbl_friend_request
(
  friend_request_id     bigint auto_increment
    primary key,
  friend_requester      varchar(255)                       not null,
  friend_requested      varchar(255)                       not null,
  friend_request_status int(1)                             not null
  comment '请求处理状态:
0:未处理；
1:接受好友申请；
2:拒绝好友申请；',
  request_time          datetime default CURRENT_TIMESTAMP not null
);

create table tbl_friendship
(
  username1    varchar(255)                       not null,
  username2    varchar(255)                       not null,
  created_time datetime default CURRENT_TIMESTAMP null,
  primary key (username1, username2)
);

create table tbl_group
(
  group_id           varchar(255)                       not null
    primary key,
  group_name         varchar(255)                       not null,
  group_owner        varchar(255)                       not null
  comment '群主：用户名',
  group_size         int default '0'                    null,
  group_maxsize      int default '2000'                 null,
  group_created_time datetime default CURRENT_TIMESTAMP null,
  group_modify_time  datetime                           null
  on update CURRENT_TIMESTAMP
);

create table tbl_group_message
(
  group_message_id      bigint auto_increment
    primary key,
  group_id              varchar(255)                       not null,
  group_message_sender  varchar(255)                       not null
  comment '发送者用户名',
  group_message_content text                               not null,
  group_message_time    datetime default CURRENT_TIMESTAMP not null
);

create table tbl_last_query
(
  username      varchar(255)       not null
    primary key,
  last_query_id bigint default '0' not null
);

create table tbl_message
(
  message_id       bigint auto_increment
    primary key,
  message_content  text                               not null
  comment '消息内容',
  message_status   int default '0'                    not null
  comment '状态==>
1：已读;
0：未读;
-1：消息发送方已删除;
-2：消息接收方已删除；
-3：双方均已删除；',
  message_sender   varchar(255)                       not null
  comment '发送者用户名',
  message_receiver varchar(255)                       not null
  comment '接受者用户名',
  message_time     datetime default CURRENT_TIMESTAMP not null
  comment '发送时间'
);

create table tbl_role
(
  role_id   varchar(255) not null
    primary key,
  role_name varchar(255) not null,
  constraint role_name
  unique (role_name)
);

create table tbl_role_authority
(
  role_id      varchar(255) not null,
  authority_id varchar(255) not null,
  primary key (role_id, authority_id)
);

create table tbl_user
(
  user_id      varchar(255)                       not null
    primary key,
  username     varchar(255)                       not null,
  password     varchar(255)                       not null,
  email        varchar(255)                       null,
  avatar_url   varchar(255)                       null,
  created_time datetime default CURRENT_TIMESTAMP not null,
  constraint username
  unique (username),
  constraint tbl_user_email_uindex
  unique (email)
);

create table tbl_user_group
(
  user_id      varchar(255)                       not null,
  group_id     varchar(255)                       not null,
  created_time datetime default CURRENT_TIMESTAMP null,
  primary key (user_id, group_id)
);

create table tbl_user_role
(
  user_id varchar(255) not null,
  role_id varchar(255) not null,
  primary key (user_id, role_id)
)
  comment '用户角色表';

