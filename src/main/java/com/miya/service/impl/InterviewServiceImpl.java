package com.miya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miya.dao.InterviewDeptMapper;
import com.miya.dao.InterviewUserMapper;
import com.miya.entity.model.InterviewDept;
import com.miya.entity.vo.InterviewDeptTree;
import com.miya.entity.vo.InterviewUserVO;
import com.miya.service.InterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InterviewServiceImpl implements InterviewService {

    @Autowired
    private InterviewUserMapper interviewUserMapper;

    @Autowired
    private InterviewDeptMapper interviewDeptMapper;

    @Override
    public List<InterviewDeptTree> selectDeptTree() {
        List<InterviewDept> interviewDeptList = interviewDeptMapper.selectList(
                new LambdaQueryWrapper<>(InterviewDept.class)
                        .eq(InterviewDept::getDelFlag, 0)
        );

        Map<Long, List<InterviewDept>> parentMap = interviewDeptList.stream().collect(Collectors.groupingBy(InterviewDept::getParentId));
        List<InterviewDeptTree> interviewDeptTrees = buildTree(parentMap, 0L);
        return interviewDeptTrees;
    }

    @Override
    public Page<InterviewUserVO> selectUserList(String keyword, Integer pageNum, Integer pageSize) {
        Page<InterviewUserVO> objectPage = new Page<>(pageNum, pageSize);
        objectPage = interviewUserMapper.selectUserVO(objectPage, keyword);
        return objectPage;
    }


    public List<InterviewDeptTree> buildTree(Map<Long, List<InterviewDept>> dataMap, Long key){
        List<InterviewDept> interviewDeptList = dataMap.get(key);
        if (CollectionUtils.isEmpty(interviewDeptList)) {
            return new ArrayList<>();
        }
        List<InterviewDeptTree> result = new ArrayList<>();

        for (InterviewDept interviewDept : interviewDeptList) {
            InterviewDeptTree interviewDeptTree = new InterviewDeptTree();
            interviewDeptTree.setLabel(interviewDept.getDeptName());

            interviewDeptTree.setValue(buildTree(dataMap, interviewDept.getId()));

            result.add(interviewDeptTree);
        }

        return result;
    }


}
