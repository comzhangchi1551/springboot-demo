package com.miya.entity.vo.seckill;

import com.miya.entity.model.SGoods;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SGoodsVO extends SGoods implements Serializable {

    /**
     * 秒杀价
     */
    private Integer seckillPrice;

    /**
     * 库存数量
     */
    private Integer stockCount;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;
}
