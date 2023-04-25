package com.miya.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miya.entity.vo.InterviewDeptTree;
import com.miya.entity.vo.InterviewUserVO;

import java.util.List;

public interface InterviewService {
    List<InterviewDeptTree> selectDeptTree();

    Page<InterviewUserVO> selectUserList(String keyword, Integer pageNum, Integer pageSize);
}
