package com.miya.dao;

import com.miya.entity.model.TempUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TempUserMapper {

    List<TempUser> selectAll();

    int deleteByPrimaryKey(Long id);

    int insert(TempUser record);

    int insertSelective(TempUser record);

    TempUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TempUser record);

    int updateByPrimaryKey(TempUser record);

    TempUser selectByName(@Param("name") String name, @Param("id") Long id);

    TempUser selectDetail(@Param("name") String name);
}