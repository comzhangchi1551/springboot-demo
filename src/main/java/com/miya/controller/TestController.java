package com.miya.controller;

import com.github.pagehelper.PageInfo;
import com.miya.common.BaseResult;
import com.miya.entity.model.TempUser;
import com.miya.service.TempUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Auth: 张翅
 * Date: 2022/1/26
 * Desc:
 */
@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    private TempUserService tempUserService;

    @GetMapping("t1")
    public String t1(String name){
        System.out.println("name = " + name);
        return name;
    }

    @GetMapping("list")
    public BaseResult tempUserList(@RequestParam(defaultValue = "1") Integer pageNum,
                                   @RequestParam(defaultValue = "10") Integer pageSize){
        PageInfo<TempUser> pageInfo = tempUserService.selectByPage(pageNum, pageSize);
        return BaseResult.success(pageInfo);
    }
}
