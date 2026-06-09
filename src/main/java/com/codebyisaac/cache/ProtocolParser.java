package com.codebyisaac.cache;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProtocolParser {
    
    //read incoming RESP array from client and cov to java List
    public static List<String> parseCommand(BufferedReader reader) throws IOException{
        String firstLine = reader.readLine();
        if (firstLine == null) return null;
        if (!firstLine.startsWith("*")){
            System.out.println("unknown protocol format: " + firstLine);
            return null;
        }

        int numElements = Integer.parseInt(firstLine.substring(1));
        List<String> commandTokens = new ArrayList<>();

        for (int i = 0; i < numElements; i++) {
            String sizeLine = reader.readLine();
            if (sizeLine == null || !sizeLine.startsWith("$")) {
                throw new IOException("Malformed RESP protocol: Expected '$'");
            }

            String actualValue = reader.readLine();
            if (actualValue != null) commandTokens.add(actualValue);
        }
        return commandTokens;
    }

    //format responses: wrap standard string into a RESP simple string (+MESSAGE/r/n)
    public static String toString(String message) {
        return "+" + message + "\r\n";
    }

    public static String toError(String errorMsg){
        return "-" + errorMsg + "\r\n";
    }

    public static String toBulkString(String value) {
        return "$" + value.length() + value + "\r\n";
    }

    public static String toNullBulkString() {
        return "$-1\r\n";
    }

}
