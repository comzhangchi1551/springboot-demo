package com.miya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miya.mapper.InterviewDeptMapper;
import com.miya.mapper.InterviewRoleMapper;
import com.miya.mapper.InterviewUserMapper;
import com.miya.mapper.InterviewUserRoleMapper;
import com.miya.entity.model.InterviewDept;
import com.miya.entity.model.InterviewRole;
import com.miya.entity.model.InterviewUser;
import com.miya.entity.model.InterviewUserRole;
import com.miya.entity.vo.interview.InterviewDeptTree;
import com.miya.entity.vo.interview.InterviewUserRoleVO;
import com.miya.entity.vo.interview.InterviewUserVO;
import com.miya.service.InterviewService;
import org.springframework.beans.BeanUtils;
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
    private InterviewUserRoleMapper interviewUserRoleMapper;

    @Autowired
    private InterviewDeptMapper interviewDeptMapper;

    @Autowired
    private InterviewRoleMapper interviewRoleMapper;

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

    @Override
    public List<InterviewUserRoleVO> selectUserRoleList() {
        List<InterviewUser> allUser = interviewUserMapper.selectList(new LambdaQueryWrapper<InterviewUser>().eq(InterviewUser::getDelFlag, 0));
        List<Long> userIdList = allUser.stream().map(InterviewUser::getId).collect(Collectors.toList());
        List<InterviewUserRole> allUserRoleList = interviewUserRoleMapper.selectList(
                new LambdaQueryWrapper<InterviewUserRole>()
                        .eq(InterviewUserRole::getDelFlag, 0)
                        .in(InterviewUserRole::getUserId, userIdList)
        );


        Map<Long, List<InterviewUserRole>> userIdMap = allUserRoleList.stream().collect(Collectors.groupingBy(InterviewUserRole::getUserId));


        List<InterviewRole> allInterviewRoles = interviewRoleMapper.selectList(new LambdaQueryWrapper<InterviewRole>().eq(InterviewRole::getDelFlag, 0));
        Map<Long, InterviewRole> idRoleMap = allInterviewRoles.stream().collect(Collectors.toMap(InterviewRole::getId, o -> o));

        List<InterviewUserRoleVO> resultList = new ArrayList<>();
        for (InterviewUser interviewUser : allUser) {
            InterviewUserRoleVO interviewUserRoleVO = new InterviewUserRoleVO();
            BeanUtils.copyProperties(interviewUser, interviewUserRoleVO);
            List<InterviewUserRole> userRoles = userIdMap.getOrDefault(interviewUser.getId(), new ArrayList<>());
            List<Long> roleIdList = userRoles.stream().map(InterviewUserRole::getRoleId).collect(Collectors.toList());

            List<InterviewRole> roleList = new ArrayList<>();
            for (Long roleId : roleIdList) {
                InterviewRole interviewRole = idRoleMap.get(roleId);
                if (interviewRole == null) {
                    continue;
                }

                roleList.add(interviewRole);
            }

            interviewUserRoleVO.setRoleList(roleList);
            resultList.add(interviewUserRoleVO);
        }

        return resultList;
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
