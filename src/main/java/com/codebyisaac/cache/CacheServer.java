package com.codebyisaac.cache;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CacheServer {
    public static final int PORT = 6379;

    public static void main(String[] args) {
        CacheStorage storage = new CacheStorage();
        CommandHandler processor = new CommandHandler(storage);
        
        Thread evictionThread = new Thread(new ActiveEvictionService(storage));
        evictionThread.setDaemon(true);
        evictionThread.start();

        ExecutorService threadPool = Executors.newCachedThreadPool(); //cached thread pool to manage client connections

        //open serversocket bound to port 6379
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Cache Engine started on port " + PORT);

            while (true) {
                System.out.println("Waiting for a client to connect...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected from: " + clientSocket.getRemoteSocketAddress());
                
                threadPool.submit(() -> handleClient(clientSocket, processor)); //submit client handling logic as a seprate runnable task to the pool
            }
        } catch (IOException e) {
            System.out.println("Could not start server on port " + PORT + ". is Redis already running?");
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }
    private static void handleClient(Socket clientSocket, CommandHandler processor) {
        String threadName = Thread.currentThread().getName();
        System.out.println("Connection handled by worker thread: " + threadName);
        
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
