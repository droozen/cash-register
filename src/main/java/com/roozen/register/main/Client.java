package com.roozen.register.main;

import com.roozen.register.service.SqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ComponentScan(basePackages = {"com.roozen.register.service"})
@Configuration
@ImportResource(value = "sql-service-context.xml")
public class Client {

    @Autowired
    SqlService sqlService;

    public static void main(String[] args) {
        // Start spring for the dependency injection only
        ApplicationContext context = startClient(args);

        // Execute commands
        try {
            context.getBean(Client.class).executeCommands(args);
        } catch (Exception e) {
            // TODO: Consider a logging framework
            e.printStackTrace();
        }
    }

    private static ApplicationContext startClient(String[] args) {
        SpringApplication app = new SpringApplication(Client.class);
        app.setWebEnvironment(false);
        return app.run(args);
    }

    public void executeCommands(String[] commands) throws Exception {
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
