package com.miya;

import com.miya.dao.TempUserMapper;
import com.miya.entity.model.TempUser;
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
    private TempUserMapper tempUserMapper;

    @SneakyThrows
    @Test
    void contextLoads() {
        List<TempUser> tempUserPage = tempUserMapper.selectAll();
        log.info(tempUserPage.toString());
    }

}
