package com.miya.dao.ck;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.miya.entity.model.ck.CkUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Desc:
 * @Author: 张翅
 * @Date: 2021/8/4 11:48
 */
@Repository
@Mapper
public interface CkUserDAO extends BaseMapper<CkUser> {
//    void insert(@Param("ckUserDO") CkUserDO ckUserDO);
}
