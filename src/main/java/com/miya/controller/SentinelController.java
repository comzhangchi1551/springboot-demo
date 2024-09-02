package com.miya.controller;

import com.miya.common.BaseResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sentinel")
public class SentinelController {

    @RequestMapping("test")
    public BaseResult test() {
        return BaseResult.success();
    }
}
