package com.miya;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.miya.common.utils.CJsonUtils;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

public class MainTest {

    @SneakyThrows
    public static void main(String[] args) {
        testToJson();
    }


    private static void testToJson() throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        map.put("abc", "abc");
        map.put(null, "bbb");
        map.put("ddd", null);
        map.put("fff", "fff");

        String json = CJsonUtils.toJson(map);
        System.out.println("json = " + json);
    }



    @Data
    public static class CustomPerson {
        private String name;
        private int age;
    }

}
