package com.miya;

import com.miya.entity.model.TempUser;
import com.miya.service.TempUserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
class ZccTempAppTests {

    @Autowired
    private TempUserService tempUserService;

    @SneakyThrows
    @Test
    void contextLoads() {
        TempUser detailById = tempUserService.getDetailById(1L);
        System.out.println(detailById);
    }


}
