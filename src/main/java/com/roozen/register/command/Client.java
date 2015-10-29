package com.roozen.register.command;

import com.roozen.register.services.SqlService;

public class Client {

    public static void executeCommands(String[] commands, SqlService sqlService) {
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
