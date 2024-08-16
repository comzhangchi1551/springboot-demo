package com.miya;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class ZccTempApp {

    public static void main(String[] args) {
        SpringApplication.run(ZccTempApp.class, args);
    }

}
