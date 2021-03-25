package com.etcox.infiniteFlightConnect;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
//import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.etcox.infiniteFlightConnect.objects.DataObject;
import com.etcox.infiniteFlightConnect.objects.ManifestObject;

public class InfiniteFlightConnectAPI {

    private Socket clientSocket;
    private DataOutputStream out;
    private BufferedReader in;
    private EndianDataInputStream endianStream;

    ArrayList<ManifestObject> mani;

    public InfiniteFlightConnectAPI() {
    }

    public void startConnection(String ip, int port) {
        try {
            // Create a socket on specified port an ip
            clientSocket = new Socket(ip, port);

            // Set the max time it (BufferedReader, in) can take to read data
            clientSocket.setSoTimeout(2000);

            // Get the output stream from the socket (used to write data)
            out = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));

            endianStream = new EndianDataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
            endianStream.order(ByteOrder.LITTLE_ENDIAN);

            // Another form of getting data from the socket (one currently in
            // use)
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
            
            // Put the strings into a string array list and
            // May put it in an object in the future
            mani = getManifest();

            /*
             * ManifestObject idObj =
             * getManifestObjectFromPath("aircraft/0/systems/signs/seatbelt");
             * 
             * int id = -1;
             * 
             * if(idObj != null) id = idObj.getID(); //343 //341 //542
             * 
             * ManifestObject o = getManifestObjectFromID(id); if(o == null){
             * System.out.println("null object"); }else{
             * System.out.println("Path: " + o.getPath());
             * System.out.println("ID: "+ o.getID());
             * System.out.println("Type: " + o.getType()); }
             * 
             * //Send message of specified id. Does not take a variable in for
             * the time being sendCommand(1048598); sendCommand(1048600);
             * sendCommand(1048601); sendCommand(1048649); setState(id, "true");
             * getState(id);
             */

        } catch (ConnectException e) {
            System.out.println("Could not make a socket connection");
        } catch (UnknownHostException e) {
            System.out.println("Could not connect to the host");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a command to the Infinite Flight client; This is sort of old an
     * unreliable but it works
     * 
     * @param id Command ID to send
     */
    public void sendCommand(int id) {
        try {

            out.writeInt(Integer.reverseBytes(id));
            out.writeBoolean(false);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set a state to the Infinite Flight client
     * 
     * @param id the path of the state to set
     * @param toParse The corresponding datatype of the ID in it's string format (Is
     * parsed into the correct type automatically)
     */
    public void setState(String path, String toParse) {
        ManifestObject obj = getManifestObjectFromPath(path);
        if (obj == null)
            return;
        setState(obj.getID(), toParse);
    }

    /**
     * Set a state to the Infinite Flight client
     * 
     * @param id the ID of the state to set
     * @param toParse The corresponding datatype of the ID in it's string format (Is
     * parsed into the correct type automatically)
     */
    public void setState(int id, String toParse) {

        ManifestObject obj = getManifestObjectFromID(id);
        if (obj == null)
            return;

        int toWriteType = obj.getType();

        try {
            out.writeInt(Integer.reverseBytes(id));
            out.writeBoolean(true);

            try {
                if (toWriteType == 0) {

                    boolean write = Boolean.parseBoolean(toParse);
                    out.writeBoolean(write);

                } else if (toWriteType == 1) {

                    int write = Integer.parseInt(toParse);
                    out.writeInt(Integer.reverseBytes(write));

                } else if (toWriteType == 2) {

                    float write = reverseFloat(Float.parseFloat(toParse));
                    out.writeFloat(write);

                } else if (toWriteType == 3) {

                    double write = Double.parseDouble(toParse);
                    out.writeDouble(write);

                } else if (toWriteType == 4) {

                    out.writeUTF(toParse);

                } else if (toWriteType == 5) {

                    long write = Long.parseLong(toParse);
                    out.writeLong(Long.reverseBytes(write));

                }
            } catch (NumberFormatException e) {
                System.out.println("Could not send data. An invalid data type was used");
                return;
            }

            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a state from the API
     * 
     * @param path the path of the state to get
     * @return a DataObject which holds the corresponding value
     */
    public DataObject getState(String path) {
        ManifestObject obj = getManifestObjectFromPath(path);
        if (obj == null)
            return new DataObject();
        return getState(obj.getID());
    }

    /**
     * Get a state from the API
     * 
     * @param id the ID of the state to get
     * @return a DataObject which holds the corresponding value
     */
    public DataObject getState(int id) {

        if (id == -1) {
            System.out.println("Dont send -1 to getState() please");
            return null;
        }

        ManifestObject obj = getManifestObjectFromID(id);
        if (obj == null)
            return null;

        try {

            // out.writeInt(Integer.reverseBytes(obj.getID()));
            out.writeInt(Integer.reverseBytes(id));
            out.writeBoolean(false);
            out.flush();

            while (true) {

                int echoID = -1;
                int echoLength = -1;
                int echoLengthString = -1;

                // System.out.println("here");
                for (int i = 0; i < 3; i++) {
                    if (i == 0)
                        echoID = endianStream.readInt();
                    else if (i == 1)
                        echoLength = endianStream.readInt();
                    else if (i == 2 && obj.getType() == 4)
                        echoLengthString = endianStream.readInt();
                }

                System.out.println("Echo ID: " + echoID);
                System.out.println("Echo Length: " + echoLength);
                System.out.println("Echo Length For String: " + echoLengthString);

                if (obj.getType() == 0) {

                    boolean data = endianStream.readBoolean();

                    System.out.println(data);
                    return new DataObject(data);

                } else if (obj.getType() == 1) {

                    int data = endianStream.readInt();

                    System.out.println(data);
                    return new DataObject(data);

                } else if (obj.getType() == 2) {

                    byte[] bytes = new byte[4];
                    @SuppressWarnings("unused")
                    int len;

                    while ((len = endianStream.read(bytes)) > 0) {
                        int bits = (bytes[0] & 0xFF) | ((bytes[1] & 0xFF) << 8) | ((bytes[2] & 0xFF) << 16)
                                | ((bytes[3] & 0xFF) << 24);

                        float data = Float.intBitsToFloat(bits);
                        System.out.println("Data: " + data);
                        return new DataObject(data);
                    }

                    float data = endianStream.readFloat();
                    System.out.println(data);
                    return new DataObject(data);

                } else if (obj.getType() == 3) {

                    double data = endianStream.readDouble();
                    System.out.println(data);
                    return new DataObject(data);

                } else if (obj.getType() == 4) {
                    byte[] bytes = new byte[echoLengthString];
                    int len;

                    while ((len = endianStream.read(bytes)) > 0) {
                        String data = new String(bytes, 0, len);
                        System.out.println("Data: " + data);
                        return new DataObject(data);
                    }

                } else if (obj.getType() == 5) {

                    long data = endianStream.readLong();
                    System.out.println(data);
                    return new DataObject(data);

                } else {

                    System.out.println("Not supported");
                    return new DataObject();

                }
            }

        } catch (SocketTimeoutException e) {
            System.out.println("No data was returned");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new DataObject();
    }

    /**
     * Gets the manifest from the API via TCP
     * 
     * @return and ArrayList of ManifestObjects, if there are no errors
     */
    public ArrayList<ManifestObject> getManifest() {

        // Define the list to return
        ArrayList<ManifestObject> toReturn = new ArrayList<ManifestObject>();

        // Could put a "throws" with the void but try/catch will work for now.
        try {

            // Call API
            out.writeInt(-1);
            out.writeBoolean(false);
            out.flush();

            boolean isFirstLine = true;

            @SuppressWarnings("unused")
            int echoID = -1;
            int dataLength = -1;

            for (int i = 0; i < 2; i++) {
                if (i == 0)
                    echoID = in.read();
                else if (i == 1)
                    dataLength = in.read();
            }

            while (true) {

                if (dataLength == -1) {
                    System.out.println("There was an issue getting the manifest, will retry.");
                    mani = getManifest();
                    return null;
                }

                String l = in.readLine();

                String[] splitString = l.split(",", 3);

                int putID = -2;
                int putType = -2;
                String putPath = null;

                int i = 0;

                for (String s : splitString) {

                    // First line contains "Specials" so we must filter them out
                    if (isFirstLine) {

                        // Make a pattern and match all specials
                        System.out.println("First line is here");
                        Pattern p = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);
                        Matcher m = p.matcher("�");
                        boolean b = m.find();

                        // If it contains specials, replace everything leaving
                        // only integers
                        if (b) {
                            s = s.replaceAll("[^\\d.].", "");
                        }

                        isFirstLine = false;
                    }

                    try {
                        if (i == 0)
                            putID = Integer.parseInt(s);
                        else if (i == 1)
                            putType = Integer.parseInt(s);
                        else if (i == 2)
                            putPath = s;
                        i += 1;
                    } catch (NumberFormatException e) {
                        System.out.println("Misread a line of the manifest:" + s);
                    }
                }

                if (putID != -2 && putType != -2 && putPath != null) {
                    toReturn.add(new ManifestObject(putID, putType, putPath));
                }
            }

        } catch (SocketTimeoutException e) {
            // Nothing more to read from the manifest
            return toReturn;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get a manifest object from the array list using an index
     * 
     * @param index the index to return
     * @return ManifestObject at specified index
     */
    public ManifestObject getManifestObject(int index) {
        return mani.get(index);
    }

    /**
     * try and locate an object from a specific path
     * 
     * @param path to search for
     * @return null or an object based on if there is a path or not
     */
    public ManifestObject getManifestObjectFromPath(String path) {
        for (ManifestObject m : mani) {
            if (m.getPath().equalsIgnoreCase(path)) {
                return m;
            }
        }
        return null;
    }

    /**
     * try and locate an object from a specific id
     * 
     * @param id to search for
     * @return null or an object based on if there is a path or not
     */
    public ManifestObject getManifestObjectFromID(int id) {

        if (mani == null)
            return null;
        else if (mani.isEmpty())
            return null;

        for (ManifestObject m : mani) {
            if (m.getID() == id) {
                return m;
            }
        }
        return null;
    }

    /**
     * try an locate an object from a specific type
     * 
     * @param type to search for
     * @return null or an object based on if there is a path or not
     */
    public ManifestObject getManifestObjectFromType(int type) {
        for (ManifestObject m : mani) {
            if (m.getType() == type) {
                return m;
            }
        }
        return null;
    }

    public static float convertMetersPerSecondToFeetPerMinute(float ms) {
        return ms * 196.85f;
    }

    public static float convertMetersPerSecondToKnots(float ms) {
        return ms * 1.94384f;
    }

    public static int convertRadianToDegree(float r) {
        float val = r * 57.29580406904963f;
        return Math.round(val);
    }

    public static float convertKGSToLBS(float kgs) {
        return kgs * 2.20462f;
    }

    public static float convertDegreeToRadian(float d) {
        return d * 0.0174533f;
    }

    /**
     * Converts a Big Endian float into a Little Endian float which is needed to
     * communicate with the API
     * 
     * @param f the float value to convert
     * @return a Little Endian float
     */
    public static float reverseFloat(float f) {
        ByteBuffer buf = ByteBuffer.allocate(8);
        buf.order(ByteOrder.BIG_ENDIAN);
        buf.putFloat(f);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf.getFloat(0);
    }

    // Not used locally, could be used externally though if needed.
    /**
     * Stop the socket connection
     */
    public void stopConnection() {
        // close the connections and such
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
