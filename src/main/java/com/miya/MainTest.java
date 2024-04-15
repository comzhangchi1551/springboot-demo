package com.miya;

import com.miya.entity.model.TempUser;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainTest {

    @SneakyThrows
    public static void main(String[] args) {
        boolean b = Stream.of("", "").filter(StringUtils::isNotBlank).anyMatch(code -> "".equalsIgnoreCase(code));
        System.out.println(b);
    }


    public static void t1(String memberKey) {

        // 连接到Redis服务器，这里假设Redis运行在本地机器上，端口为6379
        Jedis jedis = new Jedis("localhost", 6379);

        String redisKey = "trn:geo:cache:zset:searchByKeyword";

        Double zscore = jedis.zscore(redisKey, memberKey);
        if (zscore == null) {
            jedis.zadd(redisKey, 1l, memberKey);
        } else {
            jedis.zincrby(redisKey,  1, memberKey);
            jedis.zremrangeByRank(redisKey, 10, -1);
        }
    }
}
