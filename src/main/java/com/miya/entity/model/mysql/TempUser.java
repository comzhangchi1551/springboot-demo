package com.miya.entity.model.mysql;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Auth: 张
 * Desc: 用户实体类
 * Date: 2021/2/22 13:41
 */
@Data
@NoArgsConstructor
public class TempUser {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private Integer age;




    public TempUser(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
