package com.push;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@Slf4j
@SpringBootApplication
@ServletComponentScan("com.push.filter")
public class Application {

    public static void main(String[] args) {
        log.info("Application is starting......");
        SpringApplication.run(Application.class, args);
        log.info("Application startup completed......");
    }

}
