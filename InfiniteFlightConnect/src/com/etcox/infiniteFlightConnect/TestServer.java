package com.etcox.infiniteFlightConnect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TestServer {

	/**
	 * THIS CLASS IS NOT BEING USED FOR THE TIME BEING.
	 */
	
	private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startServer(int port) throws IOException {
    	System.out.println("Starting the server");
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String greeting = in.readLine();
        System.out.println("hereio");
        out.println(greeting);
    }

    public void stopServer() throws IOException{
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }
	
}
