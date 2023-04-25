package com.miya.entity.model;

import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName interview_dept
 */
@Data
public class InterviewDept implements Serializable {
    /**
     * 部门表/主键
     */
    private Long id;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 部门办公地点
     */
    private String loc;

    /**
     * 同级别排序号，越小越靠前
     */
    private Integer sortNo;

    /**
     * 父级部门id，0表示没有上级部门
     */
    private Long parentId;

    /**
     * 
     */
    private Integer delFlag;

    private static final long serialVersionUID = 1L;
}