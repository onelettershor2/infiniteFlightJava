package com.etcox.infiniteFlightConnect;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Main {

	private Socket clientSocket;
	private DataOutputStream out;
	private DataInputStream input;
    private BufferedReader in;

    ArrayList<String> mana; 
    
    public Main(){
    	
    }
    
    public void startConnection(String ip, int port) {
        try {
        	System.out.println("Starting Connection");
			clientSocket = new Socket(ip, port);
			out = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
			input = new DataInputStream((clientSocket.getInputStream()));
	        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	        
	        //mana = getManafest();
	        //getManafest();
	       	        
	        //sendMessage(635);
	        //getResult();
	      
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public void sendMessage(int id) {    	
    	
    	System.out.println("Sending message");
    	
    	try{
    		
    		out.writeInt(1048649);
    		out.writeBoolean(false);
    		out.flush();
    		
    		String line = in.readLine();
        	
        	if(line == null){
        		System.out.println("Full Manafest Found; Finished Getting. (Null)");
        		return;
        	}
        	
        	while(line != null){
        		System.out.println(in.readLine());
        		if(in.readLine().contains("635")){
        			System.out.println("Full Manafest Found; Finished Getting.");
        			return;
        		}
        	}
    		
    		
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    	

        
    }
    
    public String getResult() throws IOException {
    	return null;
    }
    
    public ArrayList<String> getManafest(){
    
    	ArrayList<String> toReturn = new ArrayList<String>();
    	
    	try{
    		
    		// Call API
        	out.writeInt(-1);
        	out.writeBoolean(false);
        	out.flush();
        	
        	// Get results
        	String line = in.readLine();
        	
        	if(line == null){
        		System.out.println("Full Manafest Found; Finished Getting. (Null)");
        		return toReturn;
        	}
        	
        	while(line != null){
        		System.out.println(in.readLine());
        		toReturn.add(in.readLine());
        		if(in.readLine().contains("635")){
        			System.out.println("Full Manafest Found; Finished Getting.");
        			return toReturn;
        		}
        	}
        	return toReturn;
    		
    	}catch(IOException e){
    		e.printStackTrace();
    		return null;
    	}
    }

    public void stopConnection() {
        try {
			in.close();
			out.close();
	        clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public static void main(String[] args) throws IOException{
		
		 //TestServer s = new TestServer();
		 //s.startServer(51675);
		
		 InetAddress add = InetAddress.getByName("172.20.10.2");
		
		 UDPConnection sender = new UDPConnection(add.getHostAddress(), 15000);
	     sender.start();
		
		 Main m = new Main();
		 m.startConnection(add.getHostAddress(), 10112);
	     
	}
	
}
