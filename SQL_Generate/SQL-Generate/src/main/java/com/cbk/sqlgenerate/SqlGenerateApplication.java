package com.cbk.sqlgenerate;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.cbk.sqlgenerate.mapper")
@EnableScheduling
public class SqlGenerateApplication {

    public static void main(String[] args) {
        SpringApplication.run(SqlGenerateApplication.class, args);
    }

}
