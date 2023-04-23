package com.miya.entity.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class TempUser {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private Integer age;
}