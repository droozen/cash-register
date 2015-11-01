package com.roozen.register.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ComponentScan(basePackages = {"com.roozen.register.controller", "com.roozen.register.dao"})
@Configuration
@ImportResource(value = {"server/server-context.xml"})
public class Server {

    public static void main(String args[]) {
        SpringApplication.run(Server.class, args);
    }

}
