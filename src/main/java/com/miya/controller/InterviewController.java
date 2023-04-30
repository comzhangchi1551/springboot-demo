package com.miya.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miya.common.BaseResult;
import com.miya.entity.vo.interview.InterviewDeptTree;
import com.miya.entity.vo.interview.InterviewUserRoleVO;
import com.miya.entity.vo.interview.InterviewUserVO;
import com.miya.service.InterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("interview")
public class InterviewController {

    @Autowired
    private InterviewService interviewService;

    @GetMapping("all/dept")
    public BaseResult queryDeptTree(){
        List<InterviewDeptTree> result = interviewService.selectDeptTree();
        return BaseResult.success(result);
    }


    @GetMapping("user/list")
    public BaseResult queryUserList(String keyword,
                                    @RequestParam(defaultValue = "1", required = false) Integer pageNum,
                                    @RequestParam(defaultValue = "10", required = false) Integer pageSize){
        Page<InterviewUserVO> page = interviewService.selectUserList(keyword, pageNum, pageSize);
        return BaseResult.success(page);
    }


    @GetMapping("userRoleList")
    public BaseResult queryUserRoleList(){
        List<InterviewUserRoleVO> result = interviewService.selectUserRoleList();
        return BaseResult.success(result);
    }
}
