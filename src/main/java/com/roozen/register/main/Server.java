package com.roozen.register.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.roozen.register.controller"})
public class Server {

    public static void main(String args[]) {
        SpringApplication.run(Server.class, args);
    }

}
