package com.codebyisaac.cache;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class CacheServer {
    public static final int PORT = 6379;

    public static void main(String[] args) {
        //open serversocket bound to port 6379
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Cache Engine started on port " + PORT);

            while (true) {
                System.out.println("Waiting for a client to connect...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Clent connected from: " + clientSocket.getRemoteSocketAddress());
                
                handleClient(clientSocket);
            }
        } catch (IOException e) {
            System.out.println("Could not start server on port " + PORT + ". is Redis already running?");
            e.printStackTrace();
        }
    }
    private static void handleClient(Socket clientSocket) {
        try ( 
            //input stream to read data from client
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            //output stream send response back
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
        ) {
            String clientMessage;
            while ((clientMessage = reader.readLine()) != null) {
                System.out.println("Recieved from client: " + clientMessage);
                writer.println("+PONG");  //string response
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
