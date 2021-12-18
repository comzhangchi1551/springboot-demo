package com.miya.dao.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.miya.entity.model.mysql.TempUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Auth: 张
 * Desc: 实体类
 * Date: 2021/2/22 13:55
 */
@Repository
public interface TempUserDAO extends BaseMapper<TempUser> {

    int insertBatch(@Param("tempUserList") List<TempUser> tempUserList);

}
