package com.miya.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miya.entity.model.TempUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface TempUserMapper extends BaseMapper<TempUser> {

    Page<TempUser> selectPageCustom(IPage<?> page, @Param("username") String username);

}