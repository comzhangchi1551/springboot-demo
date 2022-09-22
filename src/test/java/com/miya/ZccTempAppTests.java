package com.miya;

import com.miya.dao.TempUserMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
@Slf4j
class ZccTempAppTests {


    @Autowired
    private TempUserMapper tempUserMapper;

    @Autowired
    private RedissonClient redissonClient;

    @SneakyThrows
    @Test
    public void contextLoads() {
        RBucket<Object> bucket = redissonClient.getBucket("k1");
        bucket.set("7787", 100, TimeUnit.SECONDS);

        for (int i = 0; i < 10; i++) {
            TimeUnit.SECONDS.sleep(3);
            RBucket<Object> k1 = redissonClient.getBucket("k1");
            long expireTime = k1.getExpireTime();
            System.out.println(expireTime);
        }
    }

}
