package com.BucketList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//This is a meta notation that combines configuration, enables auto-configuration for beans, and component scan
//to automatically create beans
@SpringBootApplication(scanBasePackages = "com.BucketList")
public class BucketListApplication {
    public static void main(String[] args) {
        SpringApplication.run(BucketListApplication.class, args);
    }
}
