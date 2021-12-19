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
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
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


    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

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
            log.info("插入" + (i + 1) + "万条成功！");
        }
    }


    /**
     * 自定义标题，且多个sheet导出demo；---------------------一个线程读，一个线程写；
     *
     * 因为excel的写比读更耗时，所以没必要优化读的速度，后续会考虑尝试优化写的速度；
     * @param response
     * @param pageSize
     * @throws IOException
     */
    @Override
    public void exportExcel(HttpServletResponse response, Integer pageSize) throws IOException {


        // 得到分页总数；--暂定一页1w条数据；
        int page = 10_0000;
        int totalPageNum = pageSize % page == 0 ? pageSize / page : (pageSize / page + 1);

        // 读到一次，写入一次；
        ArrayBlockingQueue<List<TempUserEE>> synchronousQueue = new ArrayBlockingQueue(1);

        AtomicBoolean atomicBoolean = new AtomicBoolean();
        atomicBoolean.set(true);

        // 一个线程读；
        threadPoolTaskExecutor.execute(()->{
            for (int i = 1; i <= totalPageNum; i++) {
                System.out.println(i + "查询数据   " + Thread.currentThread().getName());
                // 获取数据
                List<TempUser> records = getAll(i, page).getRecords();

                // 转换要展示的对象；
                List<TempUserEE> collect = records.stream().map(record -> {
                    TempUserEE tempUserEE = new TempUserEE();
                    tempUserEE.setId(record.getId());
                    tempUserEE.setName(record.getName());
                    tempUserEE.setAge(record.getAge());
                    tempUserEE.setCustom1("嘿嘿嘿");
                    tempUserEE.setCustom2("哈哈哈");
                    return tempUserEE;
                }).collect(Collectors.toList());

                try {
                    System.out.println(i + "开始填入数据   " + Thread.currentThread().getName());
                    synchronousQueue.put(collect);
                    System.out.println(i + "填入完成   " + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            atomicBoolean.set(false);
        });


        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(response.getOutputStream()).head(this.head()).build();
            WriteSheet writeSheet = EasyExcel.writerSheet("sheet-1").build();

            // 主线程写；
            ExcelWriter finalExcelWriter = excelWriter;
            // 写入一个sheet；
            try {
                while (atomicBoolean.get()) {
                    System.err.println("拿数据  " + Thread.currentThread().getName());
                    List<TempUserEE> tempUserEEList = synchronousQueue.take();
                    System.err.println("拿到数据  " + Thread.currentThread().getName());
                    finalExcelWriter.write(tempUserEEList, writeSheet);
                    System.err.println("写入完成  " + Thread.currentThread().getName());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }


    }


//    /**
//     * 自定义标题，且多个sheet导出demo；-----------------单线程读写
//     * @param response
//     * @param pageSize
//     * @throws IOException
//     */
//    @Override
//    public void exportExcel(HttpServletResponse response, Integer pageSize) throws IOException {
//
//        long readStart = System.currentTimeMillis();
//        System.out.println("readStart = " + readStart);
//
//        // 获取数据
//        List<TempUser> records = getAll(1, pageSize).getRecords();
//        long readTime = System.currentTimeMillis() - readStart;
//        System.out.println("readTime = " + readTime);
//
//
//        long handleDataStart = System.currentTimeMillis();
//        System.out.println("handleDataStart = " + handleDataStart);
//
//        ExcelWriter excelWriter = null;
//        try {
//            // 自定义标题，并构造writer；
//            excelWriter = EasyExcel.write(response.getOutputStream()).head(this.head()).build();
//
//            // 创建第一个sheet对象；
//            WriteSheet writeSheet = EasyExcel.writerSheet("sheet-1").build();
//
//            // 转换要展示的对象；
//            List<TempUserEE> collect = records.stream().map(record -> {
//                TempUserEE tempUserEE = new TempUserEE();
//                tempUserEE.setName(record.getName());
//                tempUserEE.setAge(record.getAge());
//                tempUserEE.setCustom1("嘿嘿嘿");
//                tempUserEE.setCustom2("哈哈哈");
//                return tempUserEE;
//            }).collect(Collectors.toList());
//
//            long handleDataTime = System.currentTimeMillis() - handleDataStart;
//            System.out.println("handleDataTime = " + handleDataTime);
//
//
//
//            long writeStart = System.currentTimeMillis();
//            System.out.println("writeStart = " + writeStart);
//
//            // 写入一个sheet；
//            excelWriter.write(collect, writeSheet);
//
//            long writeTime = System.currentTimeMillis() - writeStart;
//            System.out.println("writeTime = " + writeTime);
//
//            // 构造第二个sheet，并写入；
////            WriteSheet writeSheet2 = EasyExcel.writerSheet("sheet-2").build();
////            excelWriter.write(records, writeSheet2);
//        } finally {
//            // 千万别忘记finish 会帮忙关闭流
//            if (excelWriter != null) {
//                excelWriter.finish();
//            }
//        }
//
//
//
//    }


//    /**
//     * 多线程导出demo；
//     *
//     * @param response
//     * @param pageSize
//     * @throws IOException
//     */
//    @Override
//    public void exportExcel(HttpServletResponse response, Integer pageSize) throws IOException {
//        // 得到分页总数；
//        Integer totalCount = tempUserDAO.selectCount(null);
//        int totalPageNum = totalCount / pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize + 1);
//
//
//
//        // 多线程读数据；
//        List<Future<List<TempUserEE>>> futureList = new ArrayList<>();
//        // for循环中多次构造sheet对象即可；
//        for (int i = 1; i <= totalPageNum; i++) {
//
//            int finalI = i;
//
//            Future<List<TempUserEE>> submitList = threadPoolTaskExecutor.submit(() -> {
//                // 获取数据
//                List<TempUser> records = getAll(finalI, pageSize).getRecords();
//
//                // 转换要展示的对象；
//                List<TempUserEE> collect = records.stream().map(record -> {
//                    TempUserEE tempUserEE = new TempUserEE();
//                    tempUserEE.setName(record.getName());
//                    tempUserEE.setAge(record.getAge());
//                    tempUserEE.setCustom1("嘿嘿嘿" + finalI);
//                    tempUserEE.setCustom2("哈哈哈" + finalI);
//                    return tempUserEE;
//                }).collect(Collectors.toList());
//
//                return collect;
//
//                // 写入sheet；
////                    finalExcelWriter.write(collect, writeSheet);
//            });
//
//            futureList.add(submitList);
//        }
//
//        for (Future<List<TempUserEE>> listFuture : futureList) {
//            List<TempUserEE> tempUserEES = listFuture.get();
//        }
//
//        ExcelWriter excelWriter = null;
//        try {
//            // 自定义标题，并构造writer；由于是同一个excel文件，所以这里构造一个就行了；
//            excelWriter = EasyExcel.write(response.getOutputStream()).head(this.head()).autoCloseStream(false).build();
//
//
//
//
//
//
//
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } finally {
//            // 千万别忘记finish 会帮忙关闭流
//            if (excelWriter != null) {
//                excelWriter.finish();
//            }
//        }
//
//    }


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
