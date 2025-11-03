package com.BucketList;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

//@SpringBootTest tels Spring to start the application context for the text. This means that
//all beans, controllers, services, and repositories are initialized like when the app actually runs
@SpringBootTest

//This tells Spring to use the test profile
@ActiveProfiles("test")
class BucketListApplicationTests {

    //This is a smoke test to determine whether the Spring application context loads successfully
    @Test
    void contextLoads() {
    }
}