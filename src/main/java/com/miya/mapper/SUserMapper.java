package com.miya.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.miya.entity.model.SUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author zhangchi
* @description 针对表【seckill_user】的数据库操作Mapper
* @createDate 2023-04-29 21:17:46
* @Entity com.miya.entity.model.SUser
*/
@Repository
public interface SUserMapper extends BaseMapper<SUser> {
    void insertBatch(@Param("list") List<SUser> list);
}




