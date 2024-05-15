package com.miya.entity.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TempUser implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private Integer age;

    private List<String> hobbies;

    private Optional<String> nickname;


    public static List<TempUser> getTestTempUserList(Integer count){
        if (count == null || count == 0) {
            return new ArrayList<>();
        }

        List<TempUser> result = new ArrayList<>();
        for (Integer i = 0; i < count; i++) {

            TempUser tempUser = new TempUser(
                    Long.valueOf(i),
                    UUID.randomUUID().toString(),
                    i,
                    Lists.newArrayList(UUID.randomUUID().toString(), UUID.randomUUID().toString()),
                    Optional.ofNullable(UUID.randomUUID().toString())
            );

            result.add(tempUser);
        }

        return result;
    }
}