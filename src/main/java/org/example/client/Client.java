package org.example.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private Socket socket;

    public Client(Socket socket){
        this.socket = socket;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e){
            closeAll();
        }
    }

    public void send()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()){
                    try{
                        Scanner scanner = new Scanner(System.in);
                        while (socket.isConnected()){
                            String message = scanner.nextLine();
                            bufferedWriter.write(message);
                            bufferedWriter.newLine();
                            bufferedWriter.flush();
                        }
                    } catch (IOException e){
                        closeAll();
                    }
                }
            }
        }).start();
    }

    public void listen(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String incomingMessage;
                while (socket.isConnected()){
                    try{
                        incomingMessage = bufferedReader.readLine();
                        System.out.println(incomingMessage);
                    } catch (IOException e){
                        closeAll();
                    }
                }
            }
        }).start();
    }

    private void closeAll()
    {
        try{
            if (bufferedReader != null) bufferedReader.close();
            if (bufferedWriter != null) bufferedWriter.close();
            if (socket != null) socket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
