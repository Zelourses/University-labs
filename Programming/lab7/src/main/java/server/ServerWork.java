package server;

import Tools.SerializeTo;
import essence.Inflammable;

import java.io.*;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ConcurrentHashMap;

public class ServerWork {
    private DatagramSocket udpSocket;
    private int port;
    public ConcurrentHashMap<String, Inflammable> storage;
    public String filename;
    private Listener handler;
    byte[] buf = new byte[8192];
    private DataBaseConnection db;

    public ServerWork(int port) throws IOException {
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        }catch (UnsupportedEncodingException ignored){}
        this.port = port;
        try {
            this.udpSocket = new DatagramSocket(port);
        } catch (BindException e) {
            System.out.println("this port is already binded,try again");
            System.exit(0);
        }
        System.out.println("Server started on "+InetAddress.getLocalHost()+" with port: "+port );
        db = new DataBaseConnection();
        storage = new ConcurrentHashMap<>();
        System.out.println("Added " + db.loadEssences(storage) + " Inflammables.");
    }

    public void listen() throws Exception {

        while (true) {

            DatagramPacket datagramPacket = new DatagramPacket(buf,buf.length);
            udpSocket.receive(datagramPacket);
            InetAddress address = datagramPacket.getAddress();
            int port = datagramPacket.getPort();
            datagramPacket = new DatagramPacket(buf,buf.length,address,port);

            SerializeTo ser;

            try (ByteArrayInputStream bais = new ByteArrayInputStream(buf);
                 ObjectInputStream ois = new ObjectInputStream(bais)){
                ser = (SerializeTo) ois.readObject();

                System.out.println("Client connected, and his command: "+ser.getCommand());


                handler = new Listener();
                handler.setStart(ser, storage, datagramPacket,db);
                handler.start();

            } catch (IOException | ClassNotFoundException ignored) {

            }
        }

    }


}
