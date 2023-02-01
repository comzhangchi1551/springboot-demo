package com.miya.service;

import com.github.pagehelper.PageInfo;
import com.miya.entity.dto.TempUserInsertDTO;
import com.miya.entity.dto.TempUserUpdateDTO;
import com.miya.entity.model.TempUser;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface TempUserService {

    PageInfo<TempUser> selectByPage(int pageNum, int pageSize);

    void exportExcel(HttpServletResponse response, Integer pageSize) throws IOException;

    void insert(TempUserInsertDTO insertDTO);

    void update(TempUserUpdateDTO updateDTO);

    TempUser detail(String name);
}
