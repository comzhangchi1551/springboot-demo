package com.miya.entity.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 
 * @TableName psd_project
 */
@Data
public class PsdProject implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * psd项目名称
     */
    private String projectName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建者id
     */
    private Long creatorId;

    /**
     * 最近更新时间
     */
    private Date updateTime;

    /**
     * 最近更新者id
     */
    private Long updaterId;

    /**
     * 是否删除0：未删除，1：已删除
     */
    private Integer delFlag;

    private static final long serialVersionUID = 1L;
}