package com.miya.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miya.common.BaseResult;
import com.miya.entity.dto.TempUserInsertDTO;
import com.miya.entity.dto.TempUserUpdateDTO;
import com.miya.entity.model.TempUser;
import com.miya.mq.rabbit.RabbitMQSender;
import com.miya.service.TempUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

/**
 * Auth: 张翅
 * Date: 2022/1/26
 * Desc:
 */
@RestController
@RequestMapping("test")
@Validated
@Slf4j
public class TestController implements InitializingBean {

    @Autowired
    private TempUserService tempUserService;

    @GetMapping("t1")
    public String t1(String name){
        System.out.println("name = " + name);
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


    @Autowired
    private RabbitMQSender rabbitMQSender;

    /**
     * 测试发送rabbitmq消息；
     */
    @RequestMapping("mq")
    public void mq(@NotBlank String msg){
        rabbitMQSender.send(JSON.toJSONString(msg));
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("===TestController===afterPropertiesSet===");
    }
}
