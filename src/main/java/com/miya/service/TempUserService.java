package com.miya.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miya.entity.model.mysql.TempUser;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface TempUserService {
    Integer addTempUser(List<TempUser> tempUserList);

    Page<TempUser> getAll(int pageNum, int pageSize);

    void insertTestData();

    void exportExcel(HttpServletResponse response, Integer pageSize) throws IOException;
}
