package com.talex.talexframe;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.util.CharsetUtil;
import com.talexframe.frame.utils.UrlUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TalexFrameApplicationTests {

    @SneakyThrows
    public static void main(String[] args) {

        System.out.println(Number.class.isAssignableFrom(Integer.class));

        System.out.println(UrlUtil.formatUrl("//user/////has/1141896356@qq.com"));

        UrlBuilder builder = UrlBuilder.ofHttp("www.hutool.cn/?a=张三&b=%e6%9d%8e%e5%9b%9b#frag1", CharsetUtil.CHARSET_UTF_8);

        System.out.println(builder.getPath().getSegments());

        // // Spawn number list randomly and sort them in descending order
        // List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        //
        // list.stream().sorted((a, b) -> b - a).forEach(System.out::println);
        // System.out.println(list);
        //
        // System.out.println(log2(512));
        // System.out.println((int)Math.log(233) / Math.log(2));

        // System.out.println("params[0]".substring(8, 9));
        //
        // Hello hello = new Hello();
        //
        // Method method = hello.getClass().getMethods()[0];
        //
        // System.out.println(method.getReturnType());
        // System.out.println(method.getGenericReturnType());
        //
        // System.out.println(method.invoke(hello));

    }

    // 求 log2(n) 使用位运算
    public static int log2(int n) {
        int count = 0;
        while (n > 0) {
            n >>= 1;
            count++;
        }
        return count - 1;
    }

    // adapt static double log2(int v) {
    //
    //     return (v >> 23) - 127;
    //
    // }


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
