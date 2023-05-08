package com.miya;

import com.miya.dao.TempUserMapper;
import com.miya.entity.model.TempUser;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
@Slf4j
class ZccTempAppTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @SneakyThrows
    @Test
    void contextLoads() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        TempUser tempUser = new TempUser();
        tempUser.setId(32L);
        tempUser.setUsername("zhangchi");
        tempUser.setAge(14);
        valueOperations.set("大傻逼", tempUser);
        Object 大傻逼 = valueOperations.get("大傻逼");
        System.out.println(大傻逼);
    }


}
