package com.miya.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * tempUser的更新入参
 *
 * @see com.miya.entity.model.TempUser
 */
@Data
public class TempUserUpdateDTO {

    @NotNull
    private Long id;

    @NotBlank
    private String username;

    @NotNull
    private Integer age;

}
