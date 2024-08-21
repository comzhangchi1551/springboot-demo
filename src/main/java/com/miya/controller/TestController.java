package com.miya.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.miya.common.BaseResult;
import com.miya.common.anno.CurrentUserInfo;
import com.miya.entity.model.TempUser;
import com.miya.entity.model.TestInsert;
import com.miya.event.MyEvent;
import com.miya.service.TempUserService;
import com.miya.service.TestInsertService;
import com.miya.service.impl.TestInsertServiceImpl;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.cache.GuavaCacheMetrics;
import io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Auth: 张翅
 * Date: 2022/1/26
 * Desc:
 */
@RestController
@RequestMapping("test")
@Validated
@Slf4j
public class TestController {

    @Autowired
    private TempUserService tempUserService;

    @Autowired
    private ApplicationContext applicationContext;

    private Counter visitCounter;

    /**
     * 创建一个被打点的本地缓存；
     */
    private final LoadingCache<String, String> REFRESH_CACHE;

    /**
     * 创建一个被打点的线程池；
     */
    private final ScheduledExecutorService REFRESH_EXECUTOR;


    @Autowired
    private MeterRegistry meterRegistry;


    public TestController(MeterRegistry meterRegistry){
        visitCounter = Counter.builder("visit_counter").description("counter desc").register(meterRegistry);

        this.meterRegistry = meterRegistry;

        LoadingCache<String, String> loadingCache = CacheBuilder.newBuilder()
                .recordStats()
                .maximumSize(10)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) throws Exception {
                        return "";
                    }
                });

        REFRESH_CACHE = GuavaCacheMetrics.monitor(
                meterRegistry,
                loadingCache,
                "refresh-cache"
        );


        REFRESH_EXECUTOR = ExecutorServiceMetrics.monitor(
                meterRegistry,
                new ScheduledThreadPoolExecutor(3, new ThreadFactory() {

                    @Override
                    public Thread newThread(@NotNull Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setDaemon(true);
                        thread.setName("aaa" + thread.getId());
                        return thread;
                    }
                }),
                "refresh-executor"
        );
    }



    @GetMapping("testAop")
    public BaseResult testAop(){
        tempUserService.testAop();
        return BaseResult.success();
    }




    @GetMapping("testCurrentUserInfo")
    public String testCurrentUserInfo(String str, @CurrentUserInfo TempUser tempUser){
        return tempUser.toString();
    }


    @SneakyThrows
    @Timed(value = "list_t1", description = "gagaga", histogram = true)
    @GetMapping("t1")
    public String t1(String name){
        String cacheValue = REFRESH_CACHE.get(name);
        if (cacheValue == null) {
            REFRESH_EXECUTOR.schedule(()->{
                REFRESH_CACHE.put(name, name + "-vvv");
            }, 3, TimeUnit.SECONDS);
        }

        visitCounter.increment();
        return cacheValue;
//        return name;
    }

    /**
     *
     * @Timed：打点
     *
     * @param pageNum
     * @param pageSize
     * @param keyword
     * @return
     */
    @Timed(value = "list_request_duration", description = "gagaga", histogram = true)
    @GetMapping("list")
    public BaseResult tempUserList(@RequestParam(defaultValue = "1") Integer pageNum,
                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                   @RequestParam(required = false) String keyword){
        Page<TempUser> pageInfo = tempUserService.selectList(pageNum, pageSize, keyword);
        return BaseResult.success(pageInfo);
    }



    @GetMapping("event")
    public BaseResult eventTest(){
        applicationContext.publishEvent(new MyEvent("zzz", 14));
        log.info("MyEvent send success!");
        return BaseResult.success();
    }

    @Autowired
    private TestInsertService testInsertService;

    @RequestMapping("insert")
    public String insertData(){

        List<TestInsert> list = new ArrayList<>();

        for (int i = 0; i < 1000_0000; i++) {
            TestInsert tempUser = buildModel(i);

            list.add(tempUser);

            if (i % 2000 == 0) {
                testInsertService.saveBatch(list);
                list = new ArrayList<>();
            }
        }
        testInsertService.saveBatch(list);

        return "success";
    }

    private TestInsert buildModel(Integer integer){

        String uuid = UUID.randomUUID().toString();


        TestInsert testInsert = new TestInsert();

        testInsert.setUsername(uuid);
        testInsert.setAge(integer);

        int i = 1;
        testInsert.setField1(integer + "__" + uuid + "___" + i++);
        testInsert.setField2(integer + "__" + uuid + "___" + i++);
        testInsert.setField3(integer + "__" + uuid + "___" + i++);
        testInsert.setField4(integer + "__" + uuid + "___" + i++);
        testInsert.setField5(integer + "__" + uuid + "___" + i++);
        testInsert.setField6(integer + "__" + uuid + "___" + i++);
        testInsert.setField7(integer + "__" + uuid + "___" + i++);
        testInsert.setField8(integer + "__" + uuid + "___" + i++);
        testInsert.setField9(integer + "__" + uuid + "___" + i++);
        testInsert.setField10(integer + "__" + uuid + "___" + i++);
        testInsert.setField11(integer + "__" + uuid + "___" + i++);
        testInsert.setField12(integer + "__" + uuid + "___" + i++);
        testInsert.setField13(integer + "__" + uuid + "___" + i++);
        testInsert.setField14(integer + "__" + uuid + "___" + i++);
        testInsert.setField15(integer + "__" + uuid + "___" + i);

        return testInsert;
    }




}
