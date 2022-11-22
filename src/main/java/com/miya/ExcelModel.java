package com.miya;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ExcelModel {

    @ExcelProperty("编号")
    private String code;

    @ExcelProperty("赠品是否准确")
    private String giftBool;
    @ExcelProperty("赠品错误情况")
    private String giftReason;

    @ExcelProperty("优惠券准确与否")
    private String couponBool;
    @ExcelProperty("如果优惠券不准确，问题在哪里")
    private String couponReason;

    @ExcelProperty("满减准确与否")
    private String priceBool;
    @ExcelProperty("如果满减有问题，满减问题是什么")
    private String priceReason;
}
