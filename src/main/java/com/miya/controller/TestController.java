package com.miya.controller;

import com.miya.common.BaseResult;
import com.miya.common.anno.CurrentUserInfo;
import com.miya.entity.model.TempUser;
import com.miya.event.MyEvent;
import com.miya.service.TempUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


    @GetMapping("testAop")
    public BaseResult testAop(){
        tempUserService.testAop();
        return BaseResult.success();
    }

    @GetMapping("testCurrentUserInfo")
    public String testCurrentUserInfo(String str, @CurrentUserInfo TempUser tempUser){
        return tempUser.toString();
    }


    @GetMapping("event")
    public BaseResult eventTest(){
        applicationContext.publishEvent(new MyEvent("zzz", 14));
        log.info("MyEvent send success!");
        return BaseResult.success();
    }


}
