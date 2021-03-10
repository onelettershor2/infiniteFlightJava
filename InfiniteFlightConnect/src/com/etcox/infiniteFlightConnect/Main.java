package com.etcox.infiniteFlightConnect;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
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
			
			// Set the max time it (BufferedReader, in) can take to read data
			clientSocket.setSoTimeout(1000);
			
			// Get the outputstream from the socket (used to write data)
			out = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
			
			// Get the innputstream from the socket (used to read data); ; not being used right now
			//input = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
			
			// Another form of getting data from the socket (one currently in use)
	        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	        
	        System.out.println("System made it here, so far.");
	        
			// Put the strings into a string array list and
			// May put it in an object in the future
	        mana = getManafest();
	       	        
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
    	try{
    		
    		// Send the data to the remote device (iPad Pro 2020 being used to test)
    		
    		out.writeInt(435);
    		out.writeBoolean(false);
    		out.flush();
    		
    		//out.writeInt(499);
    		//out.writeBoolean(false);
    		//out.flush();
    		
    		// Read the result, in a string, if applicable from the socket.
        	
    		while(true){
    			
    			System.out.println("about to read");
    			int l = in.read();
    			System.out.println("read");
    			
    			// Will return a -1 if there is nothing to read
    			if(l == -1){
    				System.out.println("Nothing more to read");
    				return;
    			}
    			System.out.println(l);
    			
    		}
    		
    	}catch(SocketTimeoutException e){
    		
    		// Catch the read time exception that is thrown if no data was read within time limit
    		System.out.println("There was not any data returned within the timelimit.");
    		
    	}catch(IOException e){
    		
    		//For any other uncaught errors
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
        	
        	// While there are new lines to read...
        	while(true){
        		
        		// SocketTimeout will be thrown for the last line
        		String l = in.readLine();
        		
        		// Never used; Timeout is thrown instead which returns the list.
        		if(l == null){
        			System.out.println("Full Manafest Found; Finished Getting.");
        			return toReturn;
        		}
        		
        		System.out.println(l);
        		toReturn.add(l);
        		
        	}
        	
        	
        	//Obsolete now; will remove when known it will no longer be needed
        	/*
        	// Same with above, does not seem to recognize the end of the manifest, not a big issue right now.
        	if(line == null){
        		System.out.println("Full Manafest Found; Finished Getting. (Null)");
        		return toReturn;
        	}
        	
        	// When there is data, add it to the list.
        	while(line != null){
        		System.out.println(line);
        		toReturn.add(line);
        		
        		// Temporary way to find end of manifest
        		// Will add more solid way to find it in future
        		if(line.contains(".Stop")){
        			System.out.println("Full Manafest Found; Finished Getting.");
        			return toReturn;
        		}
        		//line = in.readLine();
        		break;
        	}
        	return toReturn;
        	*/
    		
    	}catch(SocketTimeoutException e){
    		
    		//Nothing more to read from the manifest
    		System.out.println("Finished obtaining the manifest, timed-out");
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
		 InetAddress add = InetAddress.getByName("192.168.4.89");
		
		 // Get the UDP broadcast, does nothing really.
		 UDPConnection sender = new UDPConnection(add.getHostAddress(), 15000);
	     sender.start();
		
	     // Define a new one of these.
		 Main m = new Main();
		 // Start the connection
		 m.startConnection(add.getHostAddress(), 10112);
	     
	}
	
}
