package com.miya.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miya.common.BaseResult;
import com.miya.common.anno.CurrentUserInfo;
import com.miya.entity.model.SOrder;
import com.miya.entity.model.SUser;
import com.miya.entity.vo.seckill.SGoodsVO;
import com.miya.service.SeckillUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@RestController
@RequestMapping("seckill")
@Validated
public class SeckillController {

    @Autowired
    private SeckillUserService seckillUserService;

    /**
     * 登录
     * @param phone
     * @param password
     * @param request
     * @param response
     * @return
     */
    @GetMapping("login")
    public BaseResult login(@NotBlank @Pattern(regexp = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$", message = "请输入正确格式的手机号码") String phone,
                            @NotBlank String password,
                            HttpServletRequest request,
                            HttpServletResponse response
    ) {
        seckillUserService.doLogin(phone, password, request, response);
        return BaseResult.success();
    }


    /**
     * 商品列表
     * @param sUser
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("goodsList")
    public BaseResult goodsList(@CurrentUserInfo SUser sUser,
                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        Page<SGoodsVO> page = seckillUserService.goodsList(sUser, pageNum, pageSize);
        return BaseResult.success(page);
    }


    /**
     * 商品详情
     * @param sUser
     * @param goodsId
     * @return
     */
    @GetMapping("goodsDetail")
    public BaseResult goodsDetail(@CurrentUserInfo SUser sUser, @NotNull Long goodsId) {
        SGoodsVO goodsVO = seckillUserService.getGoodsDetail(goodsId);
        return BaseResult.success(goodsVO);
    }

    @RequestMapping("doSeckill")
    public BaseResult doSeckill(@CurrentUserInfo SUser sUser, @NotNull Long seckillGoodsId){
        SOrder sOrder = seckillUserService.doSeckill(sUser, seckillGoodsId);
        return BaseResult.success(sOrder);
    }






}
