package com.miya.event;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;

@Data
public class MyEvent extends ApplicationEvent implements Serializable {
    private String name;
    private Integer age;

    public MyEvent(String name, Integer age) {
        super(name);
        this.name = name;
        this.age = age;
    }
}
