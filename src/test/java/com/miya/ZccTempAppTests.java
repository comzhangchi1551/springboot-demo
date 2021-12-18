package com.miya;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miya.entity.model.mysql.TempUser;
import com.miya.service.TempUserService;
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

    @Test
    void contextLoads() {
        Page<TempUser> all = tempUserService.getAll(1, 10);
        List<TempUser> records = all.getRecords();
        System.out.println("records = " + records);
    }

}
