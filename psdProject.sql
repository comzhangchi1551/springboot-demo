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

