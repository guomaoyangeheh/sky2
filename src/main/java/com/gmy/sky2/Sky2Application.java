package com.gmy.sky2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.gmy.sky2.mapper")
public class Sky2Application {

    public static void main(String[] args) {
        SpringApplication.run(Sky2Application.class, args);
    }

}
