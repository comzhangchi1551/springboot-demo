package com.miya.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.miya.common.BaseException;
import com.miya.dao.TempUserMapper;
import com.miya.entity.dto.TempUserInsertDTO;
import com.miya.entity.dto.TempUserUpdateDTO;
import com.miya.entity.easy.excel.TempUserEO;
import com.miya.entity.model.TempUser;
import com.miya.service.TempUserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @Desc:
 * @Author: 张翅
 * @Date: 2021/8/7 1:11
 */
@Service
@Slf4j
public class TempUserServiceImpl extends ServiceImpl<TempUserMapper, TempUser> implements TempUserService {

    @Autowired
    private TempUserMapper tempUserMapper;


    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;


    public Page<TempUser> selectByPage(int pageNum, int pageSize) {
        Page<TempUser> page = PageDTO.of(pageNum, pageSize);
        page = tempUserMapper.selectPage(page, null);
        return page;
    }


    /**
     * 自定义标题，且多个sheet导出demo；---------------------一个线程读，一个线程写；
     *
     * 因为excel的写比读更耗时，所以没必要优化读的速度，后续会考虑尝试优化写的速度；
     * @param response
     * @param pageSize
     * @throws IOException
     */
    @SneakyThrows
    @Override
    public void exportExcel(HttpServletResponse response, Integer pageSize) {


        // 得到分页总数；--暂定一页1w条数据；
        int page = 10_0000;
        int totalPageNum = pageSize % page == 0 ? pageSize / page : (pageSize / page + 1);

        // 读到一次，写入一次；
        ArrayBlockingQueue<List<TempUserEO>> synchronousQueue = new ArrayBlockingQueue(1);

        AtomicBoolean atomicBoolean = new AtomicBoolean();
        atomicBoolean.set(true);

        // 一个线程读；
        threadPoolTaskExecutor.execute(()->{
            for (int i = 1; i <= totalPageNum; i++) {
                System.out.println(i + "查询数据   " + Thread.currentThread().getName());
                // 获取数据
                List<TempUser> records = selectByPage(i, page).getRecords();

                // 转换要展示的对象；
                List<TempUserEO> collect = records.stream().map(record -> {
                    TempUserEO tempUserEO = new TempUserEO();
                    tempUserEO.setId(record.getId());
                    tempUserEO.setName(record.getUsername());
                    tempUserEO.setAge(record.getAge());
                    return tempUserEO;
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


        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).head(this.head()).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("sheet-1").build();

        // 写入一个sheet；
        while (atomicBoolean.get()) {
            System.err.println("拿数据  " + Thread.currentThread().getName());
            List<TempUserEO> tempUserEOList = synchronousQueue.take();
            System.err.println("拿到数据  " + Thread.currentThread().getName());
            excelWriter.write(tempUserEOList, writeSheet);
            System.err.println("写入完成  " + Thread.currentThread().getName());
        }


    }

    @Override
    public void insert(TempUserInsertDTO insertDTO) {
        TempUser tempUser = tempUserMapper.selectOne(
                new LambdaQueryWrapper<TempUser>()
                        .eq(TempUser::getUsername, insertDTO.getUsername())
                        .last("limit 1")
        );

        if (tempUser != null) {
            throw new BaseException("名称重复");
        }

        tempUser = new TempUser();
        tempUser.setUsername(insertDTO.getUsername());
        tempUser.setAge(insertDTO.getAge());

        tempUserMapper.insert(tempUser);
    }


    private static final ReentrantLock TEMP_USER_UPDATE_LOCK = new ReentrantLock();

    @Override
    public void update(TempUserUpdateDTO updateDTO) {
        TempUser tempUser = tempUserMapper.selectById(updateDTO.getId());
        if (tempUser == null) {
            throw new BaseException("数据不存在，无法修改");
        }

        try {
            TEMP_USER_UPDATE_LOCK.tryLock(3, TimeUnit.SECONDS);
            TempUser selectByName = tempUserMapper.selectOne(
                    new LambdaQueryWrapper<TempUser>()
                            .eq(TempUser::getUsername, updateDTO.getUsername())
                            .ne(TempUser::getId, updateDTO.getId())
                            .last("limit 1")
            );

            if (selectByName != null) {
                throw new BaseException("姓名重复");
            }

            tempUser.setUsername(updateDTO.getUsername());
            tempUser.setAge(updateDTO.getAge());
            tempUserMapper.updateById(tempUser);

        } catch (InterruptedException e) {
            throw new BaseException("获取锁异常");
        } finally {
            TEMP_USER_UPDATE_LOCK.unlock();
        }

    }

    @Override
    public Page<TempUser> selectList(Integer pageNum, Integer pageSize, String keyword) {
        Page<TempUser> tempUserMapperPage = tempUserMapper.selectPageCustom(PageDTO.of(pageNum, pageSize), keyword);
        return tempUserMapperPage;
    }

    @Override
    @Cacheable(value = "tempUser", key = "#root.methodName")
    public TempUser getDetailById(Long id) {
        TempUser tempUser = tempUserMapper.selectById(id);
        return tempUser;
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
