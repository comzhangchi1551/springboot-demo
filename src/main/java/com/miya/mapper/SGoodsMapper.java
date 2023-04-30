package com.miya.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miya.entity.model.SGoods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.miya.entity.vo.seckill.SGoodsVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
* @author zhangchi
* @description 针对表【s_goods】的数据库操作Mapper
* @createDate 2023-04-30 13:47:37
* @Entity com.miya.entity.model.SGoods
*/
@Repository
public interface SGoodsMapper extends BaseMapper<SGoods> {

    Page<SGoodsVO> getGoodsList(Page<SGoodsVO> page);

    SGoodsVO selectGoodsVO(@Param("goodsId") Long goodsId);
}




