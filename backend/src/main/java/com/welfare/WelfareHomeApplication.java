package com.welfare;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.welfare.mapper")
public class WelfareHomeApplication {
    public static void main(String[] args) {
        SpringApplication.run(WelfareHomeApplication.class, args);
    }
}
