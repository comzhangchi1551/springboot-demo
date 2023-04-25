package com.miya.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miya.entity.model.InterviewUser;
import com.miya.entity.vo.InterviewUserVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
* @author zhangchi
* @description 针对表【interview_user】的数据库操作Mapper
* @createDate 2023-04-25 22:23:34
* @Entity com.miya.entity.model.InterviewUser
*/
@Repository
public interface InterviewUserMapper extends BaseMapper<InterviewUser> {


    Page<InterviewUserVO> selectUserVO(Page<InterviewUserVO> objectPage, @Param("keyword") String keyword);
}
