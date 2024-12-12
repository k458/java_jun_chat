package org.example.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void runServer(){
        try {
            while (!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                System.out.println("New client has connected...");
                ClientManager client = new ClientManager(socket);
                Thread thread = new Thread(client);
                client.start();
            }
        } catch (IOException e) {
            closeSocket();
        }
    }

    public void closeSocket(){
        try{
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
