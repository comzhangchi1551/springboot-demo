package com.miya.controller;

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
public class TestController {

    @GetMapping("t1")
    public String t1(String name){
        System.out.println("name = " + name);
        return name;
    }
}
