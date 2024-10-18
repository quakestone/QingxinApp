package com.qingxin.app;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@MapperScan("com.qingxin.app.mapper")
@SpringBootApplication
public class QingXinAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(QingXinAppApplication.class, args);
    }
}
