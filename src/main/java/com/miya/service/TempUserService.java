package com.miya.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.miya.entity.dto.TempUserInsertDTO;
import com.miya.entity.dto.TempUserUpdateDTO;
import com.miya.entity.model.TempUser;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface TempUserService extends IService<TempUser> {

    Page<TempUser> selectByPage(int pageNum, int pageSize);

    void exportExcel(HttpServletResponse response, Integer pageSize) throws IOException;

    void insert(TempUserInsertDTO insertDTO);

    void update(TempUserUpdateDTO updateDTO);

    Page<TempUser> selectList(Integer pageNum, Integer pageSize, String keyword);

    TempUser getDetailById(Long id);
}
