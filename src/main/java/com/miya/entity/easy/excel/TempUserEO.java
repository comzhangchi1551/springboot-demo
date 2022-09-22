package com.miya.entity.easy.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * Auth: 张
 * Desc: 用户导入的easyExcel解析实体类；
 * Date: 2021/2/22 13:41
 */
@Data
public class TempUserEO {

    @ExcelProperty("主键ID")
    private Long id;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("年龄")
    private Integer age;
}
