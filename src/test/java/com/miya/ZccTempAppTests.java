package com.miya;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
@Slf4j
class ZccTempAppTests {

    @Autowired
    private RedissonClient redissonClient;

    @SneakyThrows
    @Test
    public void contextLoads() {
        for (int i = 0; i < 10; i++) {
            // 1. 声明一个限流器；
            RRateLimiter abc = redissonClient.getRateLimiter("abcd");

            // 2. 如果这个限流器不存在，则在redis服务中添加该限流器；
            if (!abc.isExists()) {

                // 2.1 设置为一秒钟内发放5个令牌；
                abc.setRate(RateType.OVERALL, 5, 1, RateIntervalUnit.SECONDS);
            }

            // 3. 试图获取一个令牌，获取到返回true；
            if (abc.tryAcquire(30, TimeUnit.SECONDS)) {
                System.out.println(System.currentTimeMillis());
            }
        }
    }

}
