create table binding_layer_relation
(
    id         bigint auto_increment comment '主键id'
        primary key,
    binding_id bigint            not null,
    layer_id   bigint            not null,
    del_flag   tinyint default 0 not null
)
    comment '绑定和图层的关联中间表';

create table group_layer_relation
(
    id       bigint auto_increment
        primary key,
    group_id bigint            not null comment 'layer_group表的主键id',
    layer_id bigint            not null comment 'layer表的主键id',
    del_flag tinyint default 0 not null
)
    comment '分组和图层中间表';

create table psd_binding
(
    id             bigint auto_increment comment '主键id'
        primary key,
    psd_project_id bigint                  not null comment 'psd_project表的主键id',
    binding_name   varchar(255) default '' not null comment '绑定的名称',
    del_flag       tinyint      default 0  not null comment '删除标记，0：未删除，1：已删除；'
);

create table psd_group
(
    id             bigint auto_increment
        primary key,
    psd_project_id bigint                  not null comment 'psd_project表的主键id',
    group_name     varchar(255) default '' not null comment '分组名称',
    group_sort     int unsigned default 0  not null comment '分组排序',
    del_flag       tinyint      default 0  not null
)
    comment '图层分组表';

create table psd_project
(
    id           bigint auto_increment comment '主键id'
        primary key,
    project_name varchar(255) default ''                not null comment 'psd项目名称',
    create_time  datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    creator_id   bigint       default 0                 not null comment '创建者id',
    update_time  datetime     default CURRENT_TIMESTAMP not null comment '最近更新时间',
    updater_id   bigint       default 0                 not null comment '最近更新者id',
    del_flag     tinyint      default 0                 not null comment '是否删除0：未删除，1：已删除'
);

