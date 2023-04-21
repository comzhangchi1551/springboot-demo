package com.miya.dao;

import com.miya.entity.model.PsdProject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* @author 10100769
* @description 针对表【psd_project】的数据库操作Mapper
* @createDate 2023-04-21 16:11:10
* @Entity com.miya.entity.model.PsdProject
*/
@Mapper
@Repository
public interface PsdProjectMapper extends BaseMapper<PsdProject> {

}




