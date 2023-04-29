package com.miya.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class InterviewUserVO {
    /**
     * id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;


    /**
     * 头像
     */
    private String headImg;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 归属的部门
     */
    private Long deptId;

    private String deptName;

    private String parentDeptName;

    /**
     * 管理经理的id，值取自本表id
     */
    private Long mgrId;

    private String mgrName;

    /**
     * 创建时间
     */
    private Date createTime;
}
