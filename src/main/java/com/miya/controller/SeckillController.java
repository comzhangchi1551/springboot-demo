package com.miya.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("seckill")
public class SeckillController {

    @RequestMapping("hello")
    public String hello(Model model){
        model.addAttribute("name", "xxxxxx");
        return "hello";
    }

}
