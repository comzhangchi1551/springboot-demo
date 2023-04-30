package com.miya.entity.vo.interview;

import com.miya.entity.model.InterviewRole;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class InterviewUserRoleVO {
    /**
     * id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;


    /**
     * 头像
     */
    private String headImg;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 归属的部门
     */
    private Long deptId;


    /**
     * 管理经理的id，值取自本表id
     */
    private Long mgrId;

    /**
     * 创建时间
     */
    private Date createTime;


    private List<InterviewRole> roleList;
}
