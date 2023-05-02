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
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("seckill")
@Validated
public class SeckillController {

    @Autowired
    private SeckillUserService seckillUserService;

    /**
     * 登录
     *
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
     *
     * @param sUser
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("goodsList")
    public BaseResult goodsList(@CurrentUserInfo SUser sUser,
                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        Page<SGoodsVO> page = seckillUserService.goodsList(sUser, pageNum, pageSize);
        return BaseResult.success(page);
    }


    /**
     * 商品详情
     *
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
    public BaseResult doSeckill(@CurrentUserInfo SUser sUser, @NotNull Long seckillGoodsId) throws InterruptedException {
        SOrder sOrder = seckillUserService.doSeckill(sUser, seckillGoodsId);
        return BaseResult.success(sOrder);
    }


    @RequestMapping("createUser")
    public BaseResult createUser(HttpServletRequest request, HttpServletResponse response) throws IOException {

        File file = new File("./abc.txt");
        if (file.exists()) {
            file.delete();
        }

        long along = 13700000000L;
        RandomAccessFile raf = new RandomAccessFile(file, "rw");

        List<SUser> allUser = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            List<SUser> list = new ArrayList<>();
            for (int j = 0; j < 1000; j++) {
                SUser sUser = new SUser();
                sUser.setNickname("gaga"+along);
                sUser.setPhoneNumber(String.valueOf(along++));
                sUser.setPassword("0687f9701bca74827fcefcd7e743d179");
                sUser.setSlat("1a2b3c4d");
                sUser.setHead("");
                list.add(sUser);
            }
            seckillUserService.saveBatch(list);
            allUser.addAll(list);
        }


        raf.seek(0);
        for (SUser sUser : allUser) {
            String ticket = seckillUserService.doLogin(sUser.getPhoneNumber(), "123456", request, response);
            raf.seek(raf.length());
            String row = sUser.getPhoneNumber() + "," + ticket + "\r\n";
            raf.write(row.getBytes());
        }

        raf.close();


        return BaseResult.success();
    }


}
