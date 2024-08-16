package com.miya.controller;

import com.miya.bean.BeanA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    private BeanA beanA;

    @RequestMapping("printTest")
    public String printTest(@RequestParam String msg) {
        beanA.print(msg);
        return "success";
    }
}
