package com.miya.service;

import com.miya.entity.model.SeckillUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SeckillUserService {
    void doLogin(String phone, String password, HttpServletRequest request, HttpServletResponse response);

    void goodsList(SeckillUser ticket);

    SeckillUser selectSeckillUserFromRedis(String userTicket);
}
