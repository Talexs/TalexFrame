package com.talexframe;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Method;

@SpringBootTest
class TalexFrameApplicationTests {

    @SneakyThrows
    public static void main(String[] args) {

        System.out.println("params[0]".substring(8, 9));

        Hello hello = new Hello();

        Method method = hello.getClass().getMethods()[0];

        System.out.println(method.getReturnType());
        System.out.println(method.getGenericReturnType());

        System.out.println(method.invoke(hello));

    }

    @SneakyThrows
    @Test
    void contextLoads() {

        // Print now folder path
        // System.out.println("Current folder path: " + System.getProperty("user.dir"));

    }

    private static class Hello {

        public void help() {

            System.out.println("Hello!");

        }

    }

}
