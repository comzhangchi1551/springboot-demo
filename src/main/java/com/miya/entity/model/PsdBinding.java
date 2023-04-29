package com.miya.entity.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 
 * @TableName binding
 */
@Data
public class PsdBinding implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * psd_project表的主键id
     */
    private Long psdProjectId;

    /**
     * 绑定的名称
     */
    private String bindingName;

    /**
     * 删除标记，0：未删除，1：已删除；
     */
    private Integer delFlag;

    private static final long serialVersionUID = 1L;
}