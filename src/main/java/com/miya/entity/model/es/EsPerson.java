package com.miya.entity.model.es;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EsPerson {

    @JSONField(serialize = false)
    private Integer id;

    private String name;

    private Integer age;

    @JSONField(format = "yyyy-MM-dd")
    private Date birthday;
}
