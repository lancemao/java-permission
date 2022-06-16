package com.example;

import cn.authing.permission.core.Authing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        Authing.appId = "62a95682be15fc593002307b";
        Authing.appSecret = "6ae12b9baed0158d5dd0fb18a8c4cf2d";
        SpringApplication.run(Application.class, args);
    }
}
