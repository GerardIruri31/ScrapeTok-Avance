package com.example.scrapetok.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(PostgresTestContainerConfig.class)
class ScrapeTokApplicationTests {
    @Test void contextLoads() {}
}
