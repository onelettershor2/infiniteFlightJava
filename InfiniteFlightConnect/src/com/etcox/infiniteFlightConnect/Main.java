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
        	
        	// Create a socket on specified port an ip
			clientSocket = new Socket(ip, port);
			
			// Get the outputstream from the socket (used to write data)
			out = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
			
			// Get the innputstream from the socket (used to read data); ; not being used right now
			//input = new DataInputStream((clientSocket.getInputStream()));
			
			// Another form of getting data from the socket (one currently in use)
	        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	        
			// Put the strings into a string array list and
			// May put it in an object in the future
	        mana = getManafest();
	        //getManafest();
	       	        
			//Send message of specified id. Does not take a variable in for the time being
	        // **NOTE** 635 is NOT being sent to server, just for time being.
	        sendMessage(635);
	      
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public void sendMessage(int id) {    	
    	
    	System.out.println("Sending message");
    	
    	// Could put a "throws" with the void but try/catch will work for now.
    	try{
    		
    		// Send the data to the remote device (iPad Pro 2020 being used to test)
    		out.writeInt(1048649);
    		out.writeBoolean(false);
    		out.flush();
    		
    		// Read the result, in a string, if applicable from the socket.
    		String line = in.readLine();
        	
    		// If there is not a line, return null;
    		// For some reason null does not seem to be a response from the InputStream.readLine().
        	if(line == null){
        		return;
        	}
        	
        	// If the line is not null, read it.
        	while(line != null){
        		// Just printing to console for testing.
        		System.out.println(in.readLine());
        	}
    		
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    }
    
    public ArrayList<String> getManafest(){
    
    	// Define the list to return
    	ArrayList<String> toReturn = new ArrayList<String>();
    	
    	// Could put a "throws" with the void but try/catch will work for now.
    	try{
    		
    		// Call API
        	out.writeInt(-1);
        	out.writeBoolean(false);
        	out.flush();
        	
        	// Get results
        	String line = in.readLine();
        	
        	// Same with above, does not seem to recognize the end of the manifest, not a big issue right now.
        	if(line == null){
        		System.out.println("Full Manafest Found; Finished Getting. (Null)");
        		return toReturn;
        	}
        	
        	// When there is data, add it to the list.
        	while(line != null){
        		System.out.println(in.readLine());
        		toReturn.add(in.readLine());
        		
        		// Temporary way to find end of manifest
        		// Will add more solid way to find it in future
        		if(in.readLine().contains("Engine.Stop")){
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
    	
    	// close the connections and such
    	
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
	
		 // find the devices ip, hard coded for now.
		 InetAddress add = InetAddress.getByName("172.20.10.2");
		
		 // Get the UDP broadcast, does nothing really.
		 UDPConnection sender = new UDPConnection(add.getHostAddress(), 15000);
	     sender.start();
		
	     // Define a new one of these.
		 Main m = new Main();
		 // Start the connection
		 m.startConnection(add.getHostAddress(), 10112);
	     
	}
	
}
