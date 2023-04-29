package com.miya.entity.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 绑定和图层的关联中间表
 * @TableName binding_layer_relation
 */
@Data
public class BindingLayerRelation implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private Long bindingId;

    /**
     * 
     */
    private Long layerId;

    /**
     * 
     */
    private Integer delFlag;

    private static final long serialVersionUID = 1L;
}