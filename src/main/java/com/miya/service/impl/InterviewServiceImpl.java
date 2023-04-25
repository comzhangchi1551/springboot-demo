package com.miya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miya.dao.InterviewRoleMapper;
import com.miya.entity.model.InterviewRole;
import com.miya.entity.vo.InterviewDeptTree;
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
    private InterviewRoleMapper interviewRoleMapper;

    @Override
    public List<InterviewDeptTree> selectDeptTree() {
        List<InterviewRole> interviewUserRoles = interviewRoleMapper.selectList(
                new LambdaQueryWrapper<>(InterviewRole.class)
                        .eq(InterviewRole::getDelFlag, 0)
        );

        Map<Long, List<InterviewRole>> parentMap = interviewUserRoles.stream().collect(Collectors.groupingBy(InterviewRole::getParentId));
        List<InterviewDeptTree> interviewDeptTrees = buildTree(parentMap, 0L);
        return interviewDeptTrees;
    }


    public List<InterviewDeptTree> buildTree(Map<Long, List<InterviewRole>> dataMap, Long key){
        List<InterviewRole> interviewRoles = dataMap.get(key);
        if (CollectionUtils.isEmpty(interviewRoles)) {
            return new ArrayList<>();
        }
        List<InterviewDeptTree> result = new ArrayList<>();

        for (InterviewRole interviewRole : interviewRoles) {
            InterviewDeptTree interviewDeptTree = new InterviewDeptTree();
            interviewDeptTree.setValue(interviewRole.getUsername());

            interviewDeptTree.setChild(buildTree(dataMap, interviewRole.getId()));

            result.add(interviewDeptTree);
        }

        return result;
    }


}
