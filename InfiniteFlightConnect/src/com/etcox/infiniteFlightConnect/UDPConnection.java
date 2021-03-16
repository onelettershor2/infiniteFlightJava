package com.etcox.infiniteFlightConnect;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPConnection {

	// Did not comment this one out because its not causing any issues right now
	
	private DatagramSocket udpSocket;
    private int port;
    
    public UDPConnection(int port) throws IOException {
    	
        System.out.println("Obtaining information at 15000");
        this.port = port;
        udpSocket = new DatagramSocket(this.port);
        
    }       
  
    public int start() throws IOException {
        byte[] b = new byte[512];
        while (true) {
            
            DatagramPacket packet = new DatagramPacket(b, b.length);

            this.udpSocket.receive(packet);
            
            String get = new String(b);
            
            System.out.println(get);
            
            udpSocket.close();
            return 0;
        }
    }
    
	
	
}
