package com.miya.dao.ck;

import com.miya.entity.model.ck.CkUserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Desc:
 * @Author: 张翅
 * @Date: 2021/8/4 11:48
 */
@Repository
@Mapper
public interface CkUserDAO {
    void insert(@Param("ckUserDO") CkUserDO ckUserDO);
}
