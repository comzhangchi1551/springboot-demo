package com.miya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.miya.common.BaseException;
import com.miya.common.utils.MD5Util;
import com.miya.entity.cost.SeckillLoginCost;
import com.miya.entity.model.*;
import com.miya.entity.vo.seckill.SGoodsVO;
import com.miya.mapper.*;
import com.miya.service.SeckillUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class SeckillUserServiceImpl extends ServiceImpl<SUserMapper, SUser> implements SeckillUserService {

    @Autowired
    private SUserMapper sUserMapper;

    @Autowired
    private SGoodsMapper sGoodsMapper;

    @Autowired
    private SOrderMapper sordermapper;

    @Autowired
    private SSeckillGoodsMapper sSeckillGoodsMapper;

    @Autowired
    private SSeckillOrderMapper sSeckillOrderMapper;

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    @Transactional
    public SOrder doSeckill(SUser sUser, Long seckillGoodsId) throws InterruptedException {
        // 1. 判断商品是否有库存（包括商品和秒杀商品的库存）；
        boolean reduceStockCount = sSeckillGoodsMapper.reduceStockCount(seckillGoodsId);
        if (!reduceStockCount) {
            throw new BaseException("秒杀商品库存不足");
        }

        // 2. 判断该用户是否已经秒杀过此商品；
        SSeckillOrder seckillOrder = sSeckillOrderMapper.selectOne(
                new LambdaQueryWrapper<SSeckillOrder>()
                        .eq(SSeckillOrder::getDelFlag, 0)
                        .eq(SSeckillOrder::getSUserId, sUser.getId())
                        .eq(SSeckillOrder::getSSeckillGoodsId, seckillGoodsId)
                        .last("limit 1")
        );

        if (seckillOrder != null) {
            throw new BaseException("一人仅限秒杀一次");
        }

        // 3.2 生成订单数据；
        SSeckillGoods seckillGoods = sSeckillGoodsMapper.selectById(seckillGoodsId);
        SGoods sGoods = sGoodsMapper.selectById(seckillGoods.getSGoodsId());

        SOrder sOrder = new SOrder();
        sOrder.setSUserId(sUser.getId());
        sOrder.setSGoodsId(seckillGoodsId);
        sOrder.setDeliveryAddrId(1L);
        sOrder.setGoodsName(sGoods.getGoodsName());
        sOrder.setGoodsCount(1);
        sOrder.setGoodsPrice(seckillGoods.getSeckillPrice());
        sOrder.setOrderChannel(1);
        sOrder.setStatus(0);
        sOrder.setCreateTime(new Date());
        sOrder.setPayTime(null);
        sordermapper.insert(sOrder);

        // 3.3 生成秒杀订单数据；
        seckillOrder = new SSeckillOrder();
        seckillOrder.setSUserId(sUser.getId());
        seckillOrder.setSOrderId(sOrder.getId());
        seckillOrder.setSSeckillGoodsId(seckillGoodsId);
        sSeckillOrderMapper.insert(seckillOrder);


        return null;
    }

    @Override
    public String doLogin(String phone, String password, HttpServletRequest request, HttpServletResponse response) {
        String DBPass = MD5Util.inputPassTODBPass(password, MD5Util.SALT);
        SUser user = sUserMapper.selectOne(
                new LambdaQueryWrapper<SUser>()
                        .eq(SUser::getDelFlag, 0)
                        .eq(SUser::getPhoneNumber, phone)
                        .eq(SUser::getPassword, DBPass)
                        .last("limit 1")
        );
        if (user == null) {
            throw new BaseException("用户名或密码错误");
        }

        String ticket = UUID.randomUUID().toString();
        log.info("ticket: [" + ticket + "]");
        redisTemplate.opsForValue().set(String.format(SeckillLoginCost.LOGIN_REDIS_KEY, ticket), user);
//        response.addCookie(new Cookie(SeckillLoginCost.LOGIN_COOKIE_NAME, ticket));

        return ticket;
    }

    @Override
    public Page<SGoodsVO> goodsList(SUser sUser, Integer pageNum, Integer pageSize) {
        Page<SGoodsVO> page = PageDTO.of(pageNum, pageSize);
        page = sGoodsMapper.getGoodsList(page);
        return page;
    }

    @Override
    public SUser selectSeckillUserFromRedis(String userTicket) {
        if (StringUtils.isBlank(userTicket)) {
            return null;
        }

        Object object = redisTemplate.opsForValue().get(String.format(SeckillLoginCost.LOGIN_REDIS_KEY, userTicket));

        return object == null ? null : (SUser) object;
    }

    @Override
    public SGoodsVO getGoodsDetail(Long goodsId) {
        SGoodsVO sGoodsVO = sGoodsMapper.selectGoodsVO(goodsId);
        return sGoodsVO;
    }


}
