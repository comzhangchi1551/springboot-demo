package com.miya.entity.easy.excel;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * Auth: 张
 * Desc: 用户导入的easyExcel解析实体类；
 * Date: 2021/2/22 13:41
 */
@Data
public class TempUserEE {

    @ExcelProperty("主键ID")
    private Integer id;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("年龄")
    private Integer age;

    private String custom1;

    private String custom2;
}
