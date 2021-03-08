package com.etcox.infiniteFlightConnect;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPConnection {

	// Did not comment this one out because its not causing any issues right now
	
	private DatagramSocket udpSocket;
    private InetAddress serverAddress;
    private int port;
    
    public UDPConnection(String destinationAddr, int port) throws IOException {
    	
        this.serverAddress = InetAddress.getByName(destinationAddr);
        System.out.println("Obtaining information at: " + serverAddress.getHostAddress());
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
