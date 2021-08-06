package com.miya.ck;

import com.miya.dao.ck.CkUserDAO;
import com.miya.dao.mysql.TempUserDAO;
import com.miya.entity.model.ck.CkUserDO;
import com.miya.entity.model.mysql.TempUser;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Desc:
 * @Author: 张翅
 * @Date: 2021/8/6 17:24
 */
@SpringBootTest
public class DoubleDatabasesTest{

    @Autowired
    private TempUserDAO tempUserDAO;

    @Autowired
    private CkUserDAO ckUserDAO;

    @Test
    public void insertMysql(){
        TempUser tempUser = new TempUser();
        tempUser.setName("你不好");
        tempUser.setAge(14);
        tempUserDAO.insert(tempUser);
    }


    @Test
    public void insertCkUser(){
        CkUserDO ckUser = new CkUserDO();
        ckUser.setId(2L);
        ckUser.setName("嗯嗯嗯2");
        ckUser.setAge(14);
        ckUserDAO.insert(ckUser);
    }
}
