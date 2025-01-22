package com.example.demo;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
public class MyBean implements InitializingBean, DisposableBean {

    public MyBean() {
        System.out.println("1. Bean Instantiated");
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("2. PostConstruct: Initialization logic");
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("3. InitializingBean: afterPropertiesSet");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("4. PreDestroy: Before destroying");
    }

    @Override
    public void destroy() {
        System.out.println("5. DisposableBean: destroy");
    }
}
