package com.miya;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.miya.common.CustomJsonUtils;
import com.miya.entity.model.TempUser;

import java.util.ArrayList;
import java.util.List;

public class MainTest {

    public static void main(String[] args) {

        ArrayList<TempUser> tempUsers = Lists.newArrayList(
                new TempUser(1L, "nihao", 133),
                new TempUser(2L, "enws", 13),
                new TempUser(3L, "ajfkoie", 223),
                new TempUser(4L, "eowi", 32)
        );
        String json = CustomJsonUtils.toJson(tempUsers);
        System.out.println("json = " + json);


        String str = "[\n" +
                "    {\n" +
                "        \"id\": null,\n" +
                "        \"id2\": 1,\n" +
                "        \"username\": \"zhangchi\",\n" +
                "        \"age\": 12\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\": 2,\n" +
                "        \"username\": \"wangwu\",\n" +
                "        \"age\": 13\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\": 3,\n" +
                "        \"username\": \"zhaoliu\",\n" +
                "        \"age\": 14\n" +
                "    }\n" +
                "]";


        List<TempUser> tempUserList = CustomJsonUtils.fromJson(str, new TypeReference<List<TempUser>>(){});
        System.out.println("tempUserList = " + tempUserList);
    }
}
