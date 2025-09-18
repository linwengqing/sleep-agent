package com.sleephelper;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 睡眠助手应用程序启动类
 * 
 * @author SleepHelper
 * @since 2024-01-01
 */
@SpringBootApplication
@MapperScan("com.sleephelper.mapper")
public class SleepHelperApplication {

    public static void main(String[] args) {
        SpringApplication.run(SleepHelperApplication.class, args);
        System.out.println("=================================");
        System.out.println("睡眠数据管理系统启动成功！");
        System.out.println("访问地址：http://localhost:8080");
        System.out.println("API文档：http://localhost:8080/api/sleep/health");
        System.out.println("=================================");
    }
}
