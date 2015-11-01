package com.roozen.register.main;

import com.roozen.register.service.ItemLoader;
import com.roozen.register.service.SqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.util.Assert;

@SpringBootApplication
@ComponentScan(basePackages = {"com.roozen.register.service"})
@Configuration
@ImportResource(value = {"client/client-context.xml"})
// TODO: Consider putting Client, ItemLoader, and SqlService in a different module from Server.class and friends.
public class Client {

    @Autowired
    SqlService sqlService;

    @Autowired
    ItemLoader itemLoader;

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
        // TODO: Improve command interface so that it doesn't matter the order we send parameters. But this is fine for starting up.
        String command = commands[0];
        switch (command) {
            case "init":
                sqlService.init();
                break;
            case "items":
                Assert.isTrue(commands.length >= 2);

                boolean includeHeader = true;
                if (commands.length >= 3 && commands[2].equalsIgnoreCase("N")) {
                    includeHeader = false;
                }
                itemLoader.loadItems(commands[1], includeHeader);
                break;

            default:
                break;
        }
    }

}
