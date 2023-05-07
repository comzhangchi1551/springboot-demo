package com.miya;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan("com.miya.dao")
@EnableCaching
public class ZccTempApp {

    public static void main(String[] args) {
        SpringApplication.run(ZccTempApp.class, args);
    }

}
