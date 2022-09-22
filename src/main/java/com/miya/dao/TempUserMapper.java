package com.miya.dao;

import com.miya.entity.model.TempUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TempUserMapper {

    List<TempUser> selectAll();

    int deleteByPrimaryKey(Long id);

    int insert(TempUser record);

    int insertSelective(TempUser record);

    TempUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TempUser record);

    int updateByPrimaryKey(TempUser record);
}