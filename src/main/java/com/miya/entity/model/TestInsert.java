package com.miya.entity.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 
 * @TableName test_insert
 */
@Data
public class TestInsert implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private String username;

    /**
     * 
     */
    private Integer age;

    /**
     * 
     */
    private String field1;

    /**
     * 
     */
    private String field2;

    /**
     * 
     */
    private String field3;

    /**
     * 
     */
    private String field4;

    /**
     * 
     */
    private String field5;

    /**
     * 
     */
    private String field6;

    /**
     * 
     */
    private String field7;

    /**
     * 
     */
    private String field8;

    /**
     * 
     */
    private String field9;

    /**
     * 
     */
    private String field10;

    /**
     * 
     */
    private String field11;

    /**
     * 
     */
    private String field12;

    /**
     * 
     */
    private String field13;

    /**
     * 
     */
    private String field14;

    /**
     * 
     */
    private String field15;

    private static final long serialVersionUID = 1L;
}