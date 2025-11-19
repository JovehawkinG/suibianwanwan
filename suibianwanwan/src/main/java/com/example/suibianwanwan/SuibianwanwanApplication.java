package com.example.suibianwanwan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SuibianwanwanApplication {

    public static void main(String[] args) {
        SpringApplication.run(SuibianwanwanApplication.class, args);
    }

}
