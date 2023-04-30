package com.miya.controller;

import com.miya.common.BaseResult;
import com.miya.common.anno.CurrentUserInfo;
import com.miya.entity.model.SeckillUser;
import com.miya.service.SeckillUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@RestController
@RequestMapping("seckill")
@Validated
public class SeckillController {

    @Autowired
    private SeckillUserService seckillUserService;

    @GetMapping("login")
    public BaseResult login(@NotBlank @Pattern(regexp = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$", message = "请输入正确格式的手机号码") String phone,
                            @NotBlank String password,
                            HttpServletRequest request,
                            HttpServletResponse response
    ) {
        seckillUserService.doLogin(phone, password, request, response);
        return BaseResult.success();
    }


    @GetMapping("goodsList")
    public BaseResult goodsList(@CurrentUserInfo SeckillUser seckillUser){
        seckillUserService.goodsList(seckillUser);
        return BaseResult.success();
    }
}
