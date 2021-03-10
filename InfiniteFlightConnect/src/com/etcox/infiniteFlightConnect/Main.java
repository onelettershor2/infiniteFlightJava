package com.etcox.infiniteFlightConnect;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
//import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

	private Socket clientSocket;
	private DataOutputStream out;
	//private DataInputStream input;
    private BufferedReader in;

    ArrayList<ManifestObject> mani; 
    
    public Main() {}
    
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
	        mani = getManafest();
	        
	        System.out.println(getManifestObjectFromID(1048649).getPath());
	       	        
			//Send message of specified id. Does not take a variable in for the time being
	        // **NOTE** 635 is NOT being sent to server, just for time being.
	        //sendMessage(635);
	        
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
    		out.writeInt(1048649);
    		out.writeBoolean(false);
    		out.flush();
    		
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
    
    public ArrayList<ManifestObject> getManafest(){
    
    	// Define the list to return
    	ArrayList<ManifestObject> toReturn = new ArrayList<ManifestObject>();
    	
    	// Could put a "throws" with the void but try/catch will work for now.
    	try{
    		
    		// Call API
        	out.writeInt(-1);
        	out.writeBoolean(false);
        	out.flush();
        	
        	int len = 0;
        	boolean isFirstLine = true;
        	
        	// While there are new lines to read...
        	while(true){
        		
        		// SocketTimeout will be thrown for the last line
        		if(len == 0){
        			int id = in.read();
        			System.out.println(id);
        			len += 1;
        			continue;
        		}else if (len == 1){
        			int dataLength = in.read();
        			System.out.println(dataLength);
        			len = -1;
        			continue;
        		}
        	
        		String l = in.readLine();
        		
        		// Never used; Timeout is thrown instead which returns the list.
        		if(l == null){
        			System.out.println("Full Manafest Found; Finished Getting.");
        			return toReturn;
        		}
        		
        		System.out.println(l);
        		
        		String[] splitString = l.split(",", 3);
        		
        		int putID = -1;
        		int putType = -1;
        		String putPath = null;
        		
        		int i = 0;
        		
        		for(String s : splitString){
        			
        			// First line contains "Specials" so we must filter them out
        			if(isFirstLine) {
        				
        				// Make a pattern and match all specials
        				Pattern p = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);
        				Matcher m = p.matcher("�");
        				boolean b = m.find();
        				
        				// If it contains specials, replace everything leaving only integers
        				if(b){
        					s = s.replaceAll("[^\\d.]", "");
        				}
        				
        				isFirstLine = false;
        			}
        			
        			if(i == 0) putID = Integer.parseInt(s);
        			else if(i == 1) putType = Integer.parseInt(s);
        			else if(i == 2) putPath = s;
        			i+=1;
        		}
        		
        		
        		if(putID != -1 && putType != -1 && putPath != null){
        			toReturn.add(new ManifestObject(putID, putType, putPath));
        		}
        		
        	}
        	
    	}catch(SocketTimeoutException e){
    		
    		//Nothing more to read from the manifest
    		System.out.println("Finished obtaining the manifest, timed-out");
    		return toReturn;
    		
    	}catch(IOException e){
    		
    		e.printStackTrace();
    		return null;
    		
    	}
    }
   
    /**
     * Get a manifest object from the array list using an index
     * @param index the index to return
     * @return ManifestObject at specified index
     */
    public ManifestObject getManifestObject(int index){
    	return mani.get(index);
    }
    
    /**
     * try and locate an object from a specific path
     * @param path path to search for
     * @return null or an object based on if there is a path or not
     */
    public ManifestObject getManifestObjectFromPath(String path){
    	for(ManifestObject m : mani){
    		if(m.getPath().equalsIgnoreCase(path)){
    			return m;
    		}
    	}
    	return null;
    }
    
    /**
     * try and locate an object from a specific id
     * @param id id to search for
     * @return null or an object based on if there is a path or not
     */
    public ManifestObject getManifestObjectFromID(int id){
    	for(ManifestObject m : mani){
    		if(m.getID() == id){
    			return m;
    		}
    	}
    	return null;
    }
    
    /**
     * try an locate an object from a specific type
     * @param type type to search for
     * @return null or an object based on if there is a path or not
     */
    public ManifestObject getManifestObjectFromType(int type){
    	for(ManifestObject m : mani){
    		if(m.getType() == type){
    			return m;
    		}
    	}
    	return null;
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
