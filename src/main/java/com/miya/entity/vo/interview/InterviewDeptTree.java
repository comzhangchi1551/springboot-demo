package com.miya.entity.vo.interview;

import lombok.Data;

import java.util.List;

@Data
public class InterviewDeptTree {

    private String label;

    private List<InterviewDeptTree> value;
}
