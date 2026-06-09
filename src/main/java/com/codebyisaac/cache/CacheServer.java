package com.codebyisaac.cache;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class CacheServer {
    public static final int PORT = 6379;

    public static void main(String[] args) {
        CacheEngine cache = new CacheEngine();
        CommandProcessor processor = new CommandProcessor(cache);

        //open serversocket bound to port 6379
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Cache Engine started on port " + PORT);

            while (true) {
                System.out.println("Waiting for a client to connect...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Clent connected from: " + clientSocket.getRemoteSocketAddress());
                
                handleClient(clientSocket, processor);
            }
        } catch (IOException e) {
            System.out.println("Could not start server on port " + PORT + ". is Redis already running?");
            e.printStackTrace();
        }
    }
    private static void handleClient(Socket clientSocket, CommandProcessor processor) {
        try ( 
            //input stream to read data from client
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            //output stream send response back
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
        ) {
            List<String> tokens;
            while ((tokens = ProtocolParser.parseCommand(reader)) != null) {
                String response = processor.execute(tokens);
                writer.print(response);
                writer.flush();
            }
            
        } catch (IOException e) {
            System.out.println("Client disconnected");
        } finally {
            try {
                clientSocket.close();
                System.out.println("connection closed");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
