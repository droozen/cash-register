package com.roozen.register;

import com.roozen.register.init.ItemLoader;
import com.roozen.register.init.SqlLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ComponentScan(basePackages = {"com.roozen.register.init"})
@Configuration
@ImportResource(value = {"client/client-context.xml"})
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "client")
// TODO: Consider putting Client, ItemLoader, and SqlLoader in a different module from Server.class and friends.
public class Client {

    @Autowired
    SqlLoader sqlLoader;

    @Autowired
    ItemLoader itemLoader;

    public static void main(String[] args) {
        // Start spring for the dependency injection only
        ApplicationContext context = startClient(args);

        // Execute commands
        try {
            context.getBean(Client.class).execute();
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

    private String action;
    private String file;
    private boolean includeHeader = true;

    public void execute() throws Exception {
        switch (action) {
            case "init":
                sqlLoader.init();
                break;

            case "items":
                itemLoader.loadItems(file, includeHeader);
                break;

            default:
                throw new RuntimeException("Client executed without an action.");
        }
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public boolean isIncludeHeader() {
        return includeHeader;
    }

    public void setIncludeHeader(boolean includeHeader) {
        this.includeHeader = includeHeader;
    }

}
