package com.miya.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.miya.common.BaseResult;
import com.miya.common.anno.CurrentUserInfo;
import com.miya.entity.dto.TempUserInsertDTO;
import com.miya.entity.dto.TempUserUpdateDTO;
import com.miya.entity.model.TempUser;
import com.miya.event.MyEvent;
import com.miya.service.TempUserService;
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
import org.springframework.context.annotation.DependsOn;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.*;

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


    @PostMapping("insert")
    public BaseResult insert(@RequestBody @Validated TempUserInsertDTO insertDTO){
        tempUserService.insert(insertDTO);
        return BaseResult.success();
    }

    @PostMapping("update")
    public BaseResult update(@RequestBody @Validated TempUserUpdateDTO updateDTO) {
        tempUserService.update(updateDTO);
        return BaseResult.success();
    }


    @GetMapping("event")
    public BaseResult eventTest(){
        applicationContext.publishEvent(new MyEvent("zzz", 14));
        log.info("MyEvent send success!");
        return BaseResult.success();
    }


}
