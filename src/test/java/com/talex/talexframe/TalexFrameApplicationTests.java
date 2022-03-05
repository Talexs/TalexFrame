package com.talex.talexframe;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TalexFrameApplicationTests {

    @Test
    void contextLoads() {

        // Print now folder path
        System.out.println("Current folder path: " + System.getProperty("user.dir"));

    }

}
