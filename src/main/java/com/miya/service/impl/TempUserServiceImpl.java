package com.miya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miya.dao.mysql.TempUserDAO;
import com.miya.entity.model.mysql.TempUser;
import com.miya.service.TempUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Desc:
 * @Author: 张翅
 * @Date: 2021/8/7 1:11
 */
@Service
public class TempUserServiceImpl implements TempUserService {

    @Autowired
    private TempUserDAO tempUserDAO;

    @Override
    public Integer addTempUser(List<TempUser> tempUserList) {
        for (TempUser tempUser : tempUserList) {
            tempUserDAO.insert(tempUser);
        }
        return tempUserList.size();
    }

    @Override
    public Page<TempUser> getAll(int pageNum, int pageSize) {
        Page<TempUser> tempUserPage = tempUserDAO.selectPage(
                new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<>(TempUser.class)
                        .ge(TempUser::getId, 0)
        );
        return tempUserPage;
    }
}
