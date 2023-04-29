package com.miya.entity.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName interview_user_role
 */
@Data
public class InterviewUserRole implements Serializable {
    /**
     * 
     */
    private Long id;

    /**
     * 用户表id
     */
    private Long userId;

    /**
     * 角色表id
     */
    private Long roleId;

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