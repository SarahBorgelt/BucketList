package com.BucketList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.BucketList")
public class BucketListApplication {
    public static void main(String[] args) {
        SpringApplication.run(BucketListApplication.class, args);
    }
}
