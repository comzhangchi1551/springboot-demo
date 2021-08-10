package com.miya.ck;

import com.miya.dao.ck.CkUserDAO;
import com.miya.dao.mysql.TempUserDAO;
import com.miya.entity.model.ck.CkUser;
import com.miya.entity.model.mysql.TempUser;
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
        tempUser.setName("mysql 111");
        tempUser.setAge(14);
        tempUserDAO.insert(tempUser);
    }


    @Test
    public void insertCkUser(){
        CkUser ckUser = new CkUser();
        ckUser.setId(2L);
        ckUser.setName("ck111");
        ckUser.setAge(14);
        ckUserDAO.insert(ckUser);
    }
}
