package com.miya.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miya.dao.mysql.TempUserDAO;
import com.miya.entity.easy.excel.TempUserEE;
import com.miya.entity.model.mysql.TempUser;
import com.miya.service.TempUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Desc:
 * @Author: 张翅
 * @Date: 2021/8/7 1:11
 */
@Service
@Slf4j
public class TempUserServiceImpl implements TempUserService {

    @Autowired
    private TempUserDAO tempUserDAO;

    @Override
    public Integer addTempUser(List<TempUser> tempUserList) {
        for (TempUser tempUser : tempUserList) {
            tempUserDAO.insert(tempUser);
        }
        return tempUserList.size();
    }

    @Override
    public Page<TempUser> getAll(int pageNum, int pageSize) {
        Page<TempUser> tempUserPage = tempUserDAO.selectPage(
                new Page<>(pageNum, pageSize),
                new QueryWrapper<>()
        );

        return tempUserPage;
    }

    @Override
    public void insertTestData() {
        for (int i = 0; i < 100; i++) {
            List<TempUser> tempUserList = new ArrayList<>();
            for (int j = 0; j < 10000; j++) {
                tempUserList.add(new TempUser("张翅" + i + j + "Jetbrains 家的产品有一个很良心的地方，他会允许你试用 30 天（这个数字写死在代码里了）以评估是否你真的需要为它而付费。 但很多时候会出现一种情况：IDE 并不能按照我们实际的试用时间来计算。", i + j));
            }
            tempUserDAO.insertBatch(tempUserList);
            log.info("插入"+(i+1)+"万条成功！");
        }
    }

    @Override
    public void exportExcel(HttpServletResponse response, Integer pageSize) throws IOException {

        // 获取数据
        List<TempUser> records = getAll(1, pageSize).getRecords();


        ExcelWriter excelWriter = null;
        try {
            // 自定义标题，并构造writer；
            excelWriter = EasyExcel.write(response.getOutputStream()).head(this.head()).build();

            // 创建第一个sheet对象；
            WriteSheet writeSheet = EasyExcel.writerSheet("sheet-1").build();

            // 转换要展示的对象；
            List<TempUserEE> collect = records.stream().map(record -> {
                TempUserEE tempUserEE = new TempUserEE();
                tempUserEE.setName(record.getName());
                tempUserEE.setAge(record.getAge());
                tempUserEE.setCustom1("嘿嘿嘿");
                tempUserEE.setCustom2("哈哈哈");
                return tempUserEE;
            }).collect(Collectors.toList());


            // 写入第一个sheet；
            excelWriter.write(collect, writeSheet);


            // 构造第二个sheet，并写入；
            WriteSheet writeSheet2 = EasyExcel.writerSheet("sheet-2").build();
            excelWriter.write(records, writeSheet2);
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }



    }



    private List<List<String>> head() {
        List<List<String>> list = new ArrayList<List<String>>();
        list.add(Arrays.asList("ID"));
        list.add(Arrays.asList("姓名"));
        list.add(Arrays.asList("年龄"));
        list.add(Arrays.asList("自定义-1"));
        list.add(Arrays.asList("自定义-2"));
        return list;
    }
}
