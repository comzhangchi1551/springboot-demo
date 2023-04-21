package com.miya;

import com.miya.entity.model.TempUser;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan("com.miya.dao")
public class ZccTempApp {

    public static void main(String[] args) {
        SpringApplication.run(ZccTempApp.class, args);
    }

    @Bean
    public TempUser getTempUser(){
        return new TempUser();
    }

}
