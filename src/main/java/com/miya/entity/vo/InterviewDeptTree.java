package com.miya.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class InterviewDeptTree {

    private String value;

    private List<InterviewDeptTree> child;
}
