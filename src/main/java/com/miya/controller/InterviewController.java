package com.miya.controller;

import com.miya.common.BaseResult;
import com.miya.entity.vo.InterviewDeptTree;
import com.miya.service.InterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
