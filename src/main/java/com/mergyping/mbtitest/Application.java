package com.mergyping.mbtitest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@ComponentScan(basePackages = "com.mergyping")

/* 궁금증 ComponentScan은 언제 쓸까?
ComponentScan들이 여러개일때
ComponentScans ({
@ComponentScan("com.mergyping")
@ComponentScan("com.mergyping2")
@ComponentScan("com.mergyping3")
이런 양식으로 보이며 basePackages는 사용하지 않는다.
})
*/
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
