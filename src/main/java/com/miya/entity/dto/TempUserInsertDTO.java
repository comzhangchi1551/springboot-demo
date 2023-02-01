package com.miya.entity.dto;

import com.miya.entity.model.TempUser;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TempUser的新增dto，如果修改TempUser，这里要同步更新；
 *
 * @see com.miya.entity.model.TempUser
 */
@Data
public class TempUserInsertDTO{

    @NotBlank
    private String name;

    @NotNull
    private Integer age;
}
