package com.miya.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miya.entity.model.mysql.TempUser;

import java.util.List;

public interface TempUserService {
    Integer addTempUser(List<TempUser> tempUserList);

    Page<TempUser> getAll(int pageNum, int pageSize);
}
