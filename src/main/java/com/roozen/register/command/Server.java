package com.roozen.register.command;

import com.roozen.register.services.SqlService;

public class Server {

    public static void main(String args[]) {
        if (args.length <= 0) {
            startServer();
            return;
        }

        SqlService sqlService = new SqlService();
        Client.executeCommands(args, sqlService);
    }

    public static void startServer() {

    }

}
