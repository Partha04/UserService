package com.cloud.userservice;


import com.cloud.userservice.testUtils.PostgresTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceApplicationTests extends PostgresTestContainer {

    @Test
    void contextLoads() {
    }
}
