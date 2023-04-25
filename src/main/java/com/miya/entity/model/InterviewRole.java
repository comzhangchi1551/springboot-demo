package com.miya.entity.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName interview_role
 */
@Data
public class InterviewRole implements Serializable {
    /**
     * 
     */
    private Long id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String roleDesc;

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