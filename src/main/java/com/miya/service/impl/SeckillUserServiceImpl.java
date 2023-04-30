package com.miya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miya.common.BaseException;
import com.miya.common.utils.MD5Util;
import com.miya.entity.cost.SeckillLoginCost;
import com.miya.entity.model.SeckillUser;
import com.miya.mapper.SeckillUserMapper;
import com.miya.service.SeckillUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
@Slf4j
public class SeckillUserServiceImpl implements SeckillUserService {

    @Autowired
    private SeckillUserMapper seckillUserMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void doLogin(String phone, String password, HttpServletRequest request, HttpServletResponse response) {
        String DBPass = MD5Util.inputPassTODBPass(password, MD5Util.SALT);
        SeckillUser user = seckillUserMapper.selectOne(
                new LambdaQueryWrapper<SeckillUser>()
                        .eq(SeckillUser::getDelFlag, 0)
                        .eq(SeckillUser::getPhoneNumber, phone)
                        .eq(SeckillUser::getPassword, DBPass)
                        .last("limit 1")
        );
        if (user == null) {
            throw new BaseException("用户名或密码错误");
        }

        String ticket = UUID.randomUUID().toString();
        log.info("ticket: ["+ticket+"]");
//        request.getSession().setAttribute(ticket, user);
        redisTemplate.opsForValue().set(String.format(SeckillLoginCost.LOGIN_REDIS_KEY, ticket), user);
        response.addCookie(new Cookie(SeckillLoginCost.LOGIN_COOKIE_NAME, ticket));
    }

    @Override
    public void goodsList(SeckillUser ticket) {
        log.info(ticket.toString());
    }

    @Override
    public SeckillUser selectSeckillUserFromRedis(String userTicket) {
        if (StringUtils.isBlank(userTicket)) {
            return null;
        }

        Object object = redisTemplate.opsForValue().get(String.format(SeckillLoginCost.LOGIN_REDIS_KEY, userTicket));

        return object == null ? null : (SeckillUser) object;
    }
}
