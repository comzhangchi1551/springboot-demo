package com.miya;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miya.dao.mysql.TempUserDAO;
import com.miya.entity.model.mysql.TempUser;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class ZccTempAppTests {


    @Autowired
    private TempUserDAO tempUserDAO;

    @SneakyThrows
    @Test
    void contextLoads() {
        Page<TempUser> tempUserPage = tempUserDAO.selectPage(
                new Page<>(1, 5),
                new LambdaQueryWrapper<>(TempUser.class).ge(TempUser::getAge, 10)
        );

        log.info(tempUserPage.getRecords().toString());
    }

}
