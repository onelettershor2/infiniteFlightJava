package com.etcox.infiniteFlightConnect.example;

import java.io.IOException;

import com.etcox.infiniteFlightConnect.InfiniteFlightConnectAPI;
import com.etcox.infiniteFlightConnect.UDPConnection;
import com.etcox.infiniteFlightConnect.objects.DataObject;

public class ExampleConnection {

    // Define your varibales
    private final InfiniteFlightConnectAPI api;
    private final UDPConnection udp;
    
    // Throws IO due to the UDP connection, surround it in try{}catch{} if you want to
    public ExampleConnection() throws IOException{
        
        // **NOTE** the ordering is important for the stuff below
        
        // Make a UDP connection on port 15000 to get a device's address that is running Infinite Flight
        this.udp = new UDPConnection(15000);
        
        // Starts listening for the broadcast
        this.udp.start();
        
        // Get the local address of the device running Infinite Flight
        String deviceAddress = this.udp.getAddress();
        
        // This will only work with the V2 API which requires port 10112
        this.api = new InfiniteFlightConnectAPI();
        // Make a TCP connection at the deviceAddress and on port 10112
        this.api.startConnection(deviceAddress, 10112);
    }
    
    @SuppressWarnings("unused")
    public void getAState(int id){
        
        DataObject o = api.getState(id);

        // if you dont know the data type, do this:
        boolean b;
        int i;
        float f;
        double d;
        String s;
        long l;
        
        switch(o.getDataType()) {
        case 0:
            b = o.getData0();
            break;
        case 1:
            i = o.getData1();
            break;
        case 2:
            f = o.getData2();
            break;
        case 3:
            d = o.getData3();
            break;
        case 4:
            s = o.getData4();
            break;
        case 5: 
            l = o.getData5();
            break;
        default:
            break;
        }  
    }
    
    @SuppressWarnings("unused")
    public void getAState(String path){
        
        DataObject o = api.getState(path);
        
        // if you do know the data type just call for it directly; no need to loop
        
        // In this example, just get an int value if you know it will be an int
        int i = o.getData1();
    }
    
    public void sendCommand(int id){
        api.sendCommand(id);
    }
    
    public void setState(int id, String value){
        
        // So this may be a little confusing... but what happens here is you send the data in a String format where my API will parse it for you.
        // Make value based on the data type of the specific id
        api.setState(id, value);
    }
    
    public void setState(String path, String value){
        api.setState(path, value);
    }
    
}
