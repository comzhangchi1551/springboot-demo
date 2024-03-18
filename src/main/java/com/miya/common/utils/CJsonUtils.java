package com.miya.common.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.miya.entity.model.TempUser;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 基于 jackson 自定义的JsonUtils；
 */
@Slf4j
public class CJsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        // 该特性决定了当遇到未知属性（没有映射到属性，没有任何setter或者任何可以处理它的handler），是否应该抛出一个JsonMappingException异常。
        OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        OBJECT_MAPPER.getSerializerProvider().setNullKeySerializer(new CustomNullKeySerializer());
    }


    @SneakyThrows
    public static String toJson(Object object) {
        return OBJECT_MAPPER.writeValueAsString(object);
    }


    /**
     * 将json字符串反序列化为一个 Object
     */
    @SneakyThrows
    public static <T> T fromJson(String jsonString, Class<T> valueType) {
        return OBJECT_MAPPER.readValue(jsonString, valueType);
    }


    /**
     * 将json字符串反序列化为一个复杂对象；
     *
     * @param jsonString
     * @param valueTypeRef 复杂对象的泛型，定义在此入参；
     * @return
     * @param <T>
     */
    @SneakyThrows
    public static <T> T fromJson(String jsonString, TypeReference<T> valueTypeRef) {
        return OBJECT_MAPPER.readValue(jsonString, valueTypeRef);
    }


    /**
     * 使用示例如下
     * @param args
     */
    public static void main(String[] args) {

        Map<String, String> map = new HashMap<>();
        map.put("abc", "abc");
        map.put(null, "bbb");
        map.put("ddd", null);
        map.put("fff", "fff");

        String mapToJson = CJsonUtils.toJson(map);
        System.out.println("mapToJson = " + mapToJson);


        String objectToJson = CJsonUtils.toJson(new TempUser(1l, "ee", null));
        System.out.println("objectToJson = " + objectToJson);




        String tempUserJson = "{\n" +
                "        \"id\": null,\n" +
                "        \"username\": \"zhangchi\",\n" +
                "        \"username22\": \"zhangchi22\",\n" +
                "        \"age\": 12\n" +
                "    }";

        TempUser jsonToObject = CJsonUtils.fromJson(tempUserJson, TempUser.class);
        System.out.println("jsonToObject = " + jsonToObject);


        String tempUserListJson = "[\n" +
                "    {\n" +
                "        \"id\": 1,\n" +
                "        \"userna2\": \"zhangchi\",\n" +
                "        \"age\": null\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\": 2,\n" +
                "        \"username\": \"wangwu\",\n" +
                "        \"age\": 13\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\": 3,\n" +
                "        \"username\": \"zhaoliu\",\n" +
                "        \"age3Ω\": 14\n" +
                "    }\n" +
                "]";

        List<TempUser> jsonToList = CJsonUtils.fromJson(tempUserListJson, new TypeReference<List<TempUser>>() {});
        System.out.println("jsonToList = " + jsonToList);


        String mapTest = "{\n" +
                "    \"key1\":[\n" +
                "        {\n" +
                "            \"id\": 1,\n" +
                "            \"userna2\": \"zhangchi\",\n" +
                "            \"age\": null\n" +
                "        }\n" +
                "    ],\n" +
                "    \"key2\":[\n" +
                "        {\n" +
                "            \"id\": 2,\n" +
                "            \"id2\": 2,\n" +
                "            \"username\": \"zhangchi\",\n" +
                "            \"age\": 12\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": null,\n" +
                "            \"username\": \"zhangchi\",\n" +
                "            \"username2\": \"zhangchi\",\n" +
                "            \"age\": 31\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        Map<String, List<TempUser>> testJsonToMap = CJsonUtils.fromJson(mapTest, new TypeReference<Map<String, List<TempUser>>>() {
        });
        System.out.println("testJsonToMap = " + testJsonToMap);

        String testMapToJson = CJsonUtils.toJson(testJsonToMap);
        System.out.println("testMapToJson = " + testMapToJson);
    }


    /**
     * 自定义key为null的时候，怎么展示key；
     */
    static class CustomNullKeySerializer extends StdSerializer<Object> {
        public CustomNullKeySerializer() {
            this(null);
        }

        public CustomNullKeySerializer(Class<Object> t) {
            super(t);
        }

        @Override
        public void serialize(Object nullKey, JsonGenerator jsonGenerator, SerializerProvider unused)
                throws IOException, JsonProcessingException {
            jsonGenerator.writeFieldName("null");
        }
    }

}



