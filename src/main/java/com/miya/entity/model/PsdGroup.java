package com.miya.entity.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 图层分组表
 * @TableName group
 */
@Data
public class PsdGroup implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * psd_project表的主键id
     */
    private Long psdProjectId;

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 分组排序
     */
    private Integer groupSort;

    /**
     * 
     */
    private Integer delFlag;

    private static final long serialVersionUID = 1L;
}