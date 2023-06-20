package com.miya.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miya.common.BaseResult;
import com.miya.entity.dto.TempUserInsertDTO;
import com.miya.entity.dto.TempUserUpdateDTO;
import com.miya.entity.model.TempUser;
import com.miya.event.MyEvent;
import com.miya.service.TempUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Auth: 张翅
 * Date: 2022/1/26
 * Desc:
 */
@RestController
@RequestMapping("test")
@Validated
@Slf4j
public class TestController {

    @Autowired
    private TempUserService tempUserService;

    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping("t1")
    public String t1(String name){
        System.out.println("name111222 = " + name);
        return name;
    }

    @GetMapping("list")
    public BaseResult tempUserList(@RequestParam(defaultValue = "1") Integer pageNum,
                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                   @RequestParam(required = false) String keyword){
        Page<TempUser> pageInfo = tempUserService.selectList(pageNum, pageSize, keyword);
        return BaseResult.success(pageInfo);
    }


    @PostMapping("insert")
    public BaseResult insert(@RequestBody @Validated TempUserInsertDTO insertDTO){
        tempUserService.insert(insertDTO);
        return BaseResult.success();
    }

    @PostMapping("update")
    public BaseResult update(@RequestBody @Validated TempUserUpdateDTO updateDTO) {
        tempUserService.update(updateDTO);
        return BaseResult.success();
    }


    @GetMapping("event")
    public BaseResult eventTest(){
        applicationContext.publishEvent(new MyEvent("zzz", 14));
        log.info("MyEvent send success!");
        return BaseResult.success();
    }


}
