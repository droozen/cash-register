package com.roozen.register.main;

import com.roozen.register.service.SqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.roozen.register.service"})
public class Client {

    @Autowired
    SqlService sqlService;

    public static void main(String[] args) {
        // Start spring for the dependency injection only
        ApplicationContext context = startClient(args);

        // Execute commands
        context.getBean(Client.class).executeCommands(args);
    }

    private static ApplicationContext startClient(String[] args) {
        SpringApplication app = new SpringApplication(Client.class);
        app.setWebEnvironment(false);
        return app.run(args);
    }

    public void executeCommands(String[] commands) {
        String command = commands[0];
        switch (command) {
            case "init":
                sqlService.init();
                break;
            default:
                break;
        }
    }

}
