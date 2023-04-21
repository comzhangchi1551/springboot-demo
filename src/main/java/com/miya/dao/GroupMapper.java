package com.miya.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.miya.entity.model.PsdGroup;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* @author 10100769
* @description 针对表【group(图层分组表)】的数据库操作Mapper
* @createDate 2023-04-21 16:33:30
* @Entity com.miya.entity/model.Group
*/
@Mapper
@Repository
public interface GroupMapper extends BaseMapper<PsdGroup> {

}




