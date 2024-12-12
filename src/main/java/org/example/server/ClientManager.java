package org.example.server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientManager extends Thread{

    private static ArrayList<ClientManager> clients = new ArrayList<>();
    private static int clientIndexer = 0;
    private static ArrayList<Integer> freedClientIndexes = new ArrayList<>();

    private int id;
    private String name;
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;

    public ClientManager(Socket socket) {
        ClientManager thisClientManager = this;
        this.socket = socket;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e){
            closeAll();
        }
    }

    private static int getNewId()
    {
        if (freedClientIndexes.size() > 0)
        {
            int ret = freedClientIndexes.getLast();
            freedClientIndexes.removeLast();
            return ret;
        }
        return clientIndexer++;
    }

    @Override
    public void run() {
        System.out.println("Running...");
        try{
            System.out.println("Retrieving name...");
            bufferedWriter.write("SERVER: Enter your name: ");
            bufferedWriter.newLine();
            bufferedWriter.flush();
            name = bufferedReader.readLine();
            id = getNewId();
            broadcastMessage("SERVER: " +name+ '#' +id+ " has connected...");
        } catch (IOException e){
            closeAll();
        }
        clients.add(this);
        String message;
        System.out.println("Chatting...");
        while(socket.isConnected()) {
            try {
                message = bufferedReader.readLine();
                if (message == null) {
                    closeAll();
                    break;
                }
                else broadcastMessage(name+ '#' +id+ ": " +message);
            } catch (IOException e){
                closeAll();
                break;
            }
        }
    }

    private void broadcastMessage(String s){
        for (ClientManager client: clients){
            try{
                if (client == this) continue;
                else client.bufferedWriter.write(s);
                client.bufferedWriter.newLine();
                client.bufferedWriter.flush();
            } catch (IOException e){
                closeAll();
            }
        }
    }

    private void closeAll()
    {
        remove();
        try{
            if (bufferedReader != null) bufferedReader.close();
            if (bufferedWriter != null) bufferedWriter.close();
            if (socket != null) socket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void remove(){
        clients.remove(this);
        broadcastMessage("SERVER: " +name+ '#' +id+ " has disconnected...");
    }
}
