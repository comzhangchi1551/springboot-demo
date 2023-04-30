package com.miya;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.miya.mapper")
public class ZccTempApp {

    public static void main(String[] args) {
        SpringApplication.run(ZccTempApp.class, args);
    }

}
