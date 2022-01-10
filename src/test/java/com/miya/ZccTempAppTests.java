package com.miya;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miya.entity.model.mysql.TempUser;
import com.miya.service.TempUserService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class ZccTempAppTests {

    @Autowired
    private TempUserService tempUserService;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @SneakyThrows
    @Test
    void contextLoads() {
        for (int i = 0; i < 10; i++) {
            threadPoolTaskExecutor.execute(() -> {
                System.out.println(Thread.currentThread().getName());
            });
        }

        TimeUnit.SECONDS.sleep(3);

        System.out.println("success");
    }

}
