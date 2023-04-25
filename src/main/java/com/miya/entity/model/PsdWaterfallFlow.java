package com.miya.entity.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 
 * @TableName psd_waterfall_flow
 */
@Data
public class PsdWaterfallFlow implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private Long psdProjectId;

    /**
     * 
     */
    private String imageIds;

    /**
     * 
     */
    private String version;

    /**
     * 
     */
    private Integer delFlag;

    private static final long serialVersionUID = 1L;
}