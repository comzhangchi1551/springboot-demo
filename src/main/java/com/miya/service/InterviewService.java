package com.miya.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miya.entity.vo.interview.InterviewDeptTree;
import com.miya.entity.vo.interview.InterviewUserRoleVO;
import com.miya.entity.vo.interview.InterviewUserVO;

import java.util.List;

public interface InterviewService {
    List<InterviewDeptTree> selectDeptTree();

    Page<InterviewUserVO> selectUserList(String keyword, Integer pageNum, Integer pageSize);

    List<InterviewUserRoleVO> selectUserRoleList();

}
