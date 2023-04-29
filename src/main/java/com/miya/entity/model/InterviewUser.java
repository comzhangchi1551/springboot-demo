package com.miya.entity.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName interview_user
 */
@Data
public class InterviewUser implements Serializable {
    /**
     * 
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String pwd;

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

    /**
     * 管理经理的id，值取自本表id
     */
    private Long mgrId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 
     */
    private Integer delFlag;

    private static final long serialVersionUID = 1L;
}