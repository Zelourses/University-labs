package server;

import Tools.SerializeTo;

import java.io.*;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class  ServerWork {
    private DatagramSocket udpSocket;
    //public ConcurrentHashMap<String, Inflammable> storage;
    public String filename;
    private Listener handler;
    byte[] buf = new byte[8192];
    private DataBaseConnection db;

    public ServerWork(int port) throws IOException {
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        }catch (UnsupportedEncodingException ignored){}

        try {
            this.udpSocket = new DatagramSocket(port);
        } catch (BindException e) {
            System.out.println("this port is already binded,try again");
            System.exit(0);
        }
        System.out.println("Server started on "+InetAddress.getLocalHost()+" with port: "+port );
        db = new DataBaseConnection();
        //storage = new ConcurrentHashMap<>();
        //System.out.println("Added " + db.loadEssences(storage) + " Inflammables.");
    }


    public void listen() throws Exception {
        ArrayList<Integer> raw=new ArrayList<>();
        while (true) {

            DatagramPacket datagramPacket = new DatagramPacket(buf,buf.length);
            udpSocket.receive(datagramPacket);
            InetAddress address = datagramPacket.getAddress();
            int port = datagramPacket.getPort();
             raw=fill(datagramPacket.getPort(),raw);
            datagramPacket = new DatagramPacket(buf,buf.length,address,port);

            SerializeTo ser;

            try (ByteArrayInputStream bais = new ByteArrayInputStream(buf);
                 ObjectInputStream ois = new ObjectInputStream(bais)){
                try {
                    ser = (SerializeTo) ois.readObject();

                System.out.println("Client connected, and his command: "+ser.getCommand());
                System.out.println("List: "+raw.size());
                for (int S: raw){
                    System.out.println(S);

                }

                handler = new Listener();
                handler.setStart(ser,  datagramPacket,db,raw,datagramPacket.getPort());
                handler.start();
                } catch (ClassCastException e) {
                    System.out.println("We have a problem. Somehow, we get");
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }


            } catch (IOException  ignored) {

            }
        }

    }
    private ArrayList<Integer> fill(int s,ArrayList<Integer> last){
            boolean add=true;
            for (Integer t: last){
                if (t==s){
                    add=false;
                }
            }
            if (add){
                last.add(s);
            }
            return last;
    }


}
