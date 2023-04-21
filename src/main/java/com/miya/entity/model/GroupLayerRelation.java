package com.miya.entity.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 分组和图层中间表
 * @TableName group_layer_relation
 */
@Data
public class GroupLayerRelation implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * layer_group表的主键id
     */
    private Long groupId;

    /**
     * layer表的主键id
     */
    private Long layerId;

    /**
     * 
     */
    private Integer delFlag;

    private static final long serialVersionUID = 1L;
}