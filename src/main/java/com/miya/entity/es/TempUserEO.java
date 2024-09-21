package com.miya.entity.es;

import lombok.Data;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Document(indexName = "temp_user_index")
public class TempUserEO {

    @NotNull
    @Field(type = FieldType.Long)
    private Long id;

    @NotBlank
    @Field(type = FieldType.Auto)
    private String username;

    @NotNull
    @Field(type = FieldType.Integer)
    private Integer age;


    @NotBlank
    @Field(type = FieldType.Text)
    private String say;

}
