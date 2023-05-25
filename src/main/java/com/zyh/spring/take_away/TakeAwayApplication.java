package com.zyh.spring.take_away;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@ServletComponentScan()
@EnableTransactionManagement
public class TakeAwayApplication {

    public static void main(String[] args) {
        SpringApplication.run(TakeAwayApplication.class, args);
        log.info("take_away应用启动了");
        System.out.println("hello,git");
    }

}