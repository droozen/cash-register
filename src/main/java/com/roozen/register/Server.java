package com.roozen.register;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ComponentScan(basePackages = {"com.roozen.register.service", "com.roozen.register.dao"})
@Configuration
@ImportResource(value = {"server/server-context.xml"})
@EnableConfigurationProperties
public class Server {

    public static void main(String args[]) {
        SpringApplication.run(Server.class, args);
    }

}
