package com.miya.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.miya.entity.model.SOrder;
import com.miya.entity.model.SUser;
import com.miya.entity.vo.seckill.SGoodsVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SeckillUserService extends IService<SUser> {
    String doLogin(String phone, String password, HttpServletRequest request, HttpServletResponse response);

    Page<SGoodsVO> goodsList(SUser sUser, Integer pageNum, Integer pageSize);

    SUser selectSeckillUserFromRedis(String userTicket);

    SGoodsVO getGoodsDetail(Long goodsId);

    SOrder doSeckill(SUser sUser, Long seckillGoodsId) throws InterruptedException;

}
