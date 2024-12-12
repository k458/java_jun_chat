package org.example.server;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {
    public static void main(String[] args) throws IOException {
        Server server = new Server(new ServerSocket(1300));
        server.runServer();
    }
}
