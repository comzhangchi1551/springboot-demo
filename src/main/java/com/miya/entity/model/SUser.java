package com.miya.entity.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName seckill_user
 */
@TableName(value ="s_user")
@Data
public class SUser implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private String nickname;


    private String phoneNumber;

    /**
     * 
     */
    private String password;

    /**
     * 
     */
    private String slat;

    /**
     * 
     */
    private String head;

    /**
     * 
     */
    private Date registerDate;

    /**
     * 
     */
    private Date lastLoginDate;

    /**
     * 
     */
    private Integer loginCount;

    /**
     * 
     */
    private Integer delFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}