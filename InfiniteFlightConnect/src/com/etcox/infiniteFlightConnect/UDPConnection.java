package com.etcox.infiniteFlightConnect;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class UDPConnection {

    private DatagramSocket udpSocket;
    private int port;
    
    private String address = "127.0.0.1";

    /**
     * 
     * @param port What port should the socket listen (Default for Infinite Flight is 15000)
     * @throws IOException if any errors getting a connection
     */
    public UDPConnection(int port) throws IOException {
        this.port = port;
        udpSocket = new DatagramSocket(this.port);
    }

    /**
     * Start the process of getting the address
     * @return 0 always. This value means nothing. The UDP won't be recieved for some reason if the method does not return
     * @throws IOException if there is an issue getting data on port 15000
     */
    public int start() throws IOException {
        byte[] b = new byte[512];
        while (true) {

            DatagramPacket packet = new DatagramPacket(b, b.length);

            this.udpSocket.receive(packet);
            
            String get = new String(b, "utf-8");
            
            // Wierdest thing here... it is replacing a {Nul} character even though it is not visible
            get = get.replaceAll(" ", "");

            address = setAddress(get);
            
            udpSocket.close();
            return 0;
        }
    }
    
    /**
     *  Get address of local device on network
     * @return a localhost address if a local device could not be found or an address of the device to connect to
     */
    public String getAddress(){
        return address;
    }
    
    private String setAddress(String toGet) {
        Object obj;
        try {
            obj = new JSONParser().parse(toGet);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        
        JSONObject jo = (JSONObject) obj;
        
        JSONArray array = (JSONArray) jo.get("Addresses");
        
        if(array.size() > 1)
            return (String) array.get(1);
        return "127.0.0.1";
    }

}
