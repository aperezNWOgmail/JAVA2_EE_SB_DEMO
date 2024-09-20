package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DemoEndPoint {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, world!";
    }

    @GetMapping("/data")
    public MyData getData() {
        MyData data = new MyData();
        data.setName("John Doe");
        data.setAge(30);
        return data;
    }
}
