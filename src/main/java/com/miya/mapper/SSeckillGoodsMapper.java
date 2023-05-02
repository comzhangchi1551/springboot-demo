package com.miya.mapper;

import com.miya.entity.model.SSeckillGoods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
* @author zhangchi
* @description 针对表【s_seckill_goods】的数据库操作Mapper
* @createDate 2023-04-30 13:47:37
* @Entity com.miya.entity.model.SSeckillGoods
*/
@Repository
public interface SSeckillGoodsMapper extends BaseMapper<SSeckillGoods> {

    boolean reduceStockCount(@Param("seckillGoodsId") Long seckillGoodsId);
}




