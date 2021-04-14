package ServerWorkUDP;

import Tools.SerializeTo;
import Tools.WorkWithFiles;
import essence.Inflammable;

import javax.lang.model.util.ElementScanner6;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class ServerWork extends Thread{
    private int port;
    //private Listener listener;
    private DatagramSocket UDPSocket;
    private String filename = "save.json";
    private WorkWithCollection collection;
    byte[] buff = new byte[8192];


    public ServerWork(int port)throws IOException {
        this.port = port;
        collection = new WorkWithCollection();
        this.UDPSocket =new DatagramSocket(port);
        System.out.println("Server is established");
    }

    public void SetFileName(String f){filename = f;
        System.out.println("FileName is set");}


    public void loadCollection()throws IOException{
        System.out.println("Starting to load collection");
        ConcurrentHashMap<String, Inflammable> col = new ConcurrentHashMap<>();
        ArrayList<Inflammable> raw = WorkWithFiles.ReadFile(filename);
        if (raw.size()!=0)
        {
            for (int i = 0; i < raw.size(); i++) {
                col.put(raw.get(i).getThing(), raw.get(i));
            }
        }else {
            raw.add(WorkWithFiles.CreateExample());
            Inflammable in=raw.get(0);
            col.put(in.getThing(),in);
        }

        collection.setCollection(col);
        System.out.println("Collection is loaded");
    }

    public void run(){
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                WorkWithFiles.ShutDown(collection.getCollection(),filename);
                System.out.println("Server data saved");
            } catch (Exception e) {
                System.err.println("Wow, Exception");
                e.printStackTrace();
            }
        }));
        try {
            System.out.println("Server is started on port "+ InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        while (true){


            try {
                DatagramPacket UDPPacket = new DatagramPacket(buff,buff.length);
                UDPSocket.receive(UDPPacket);

                InetAddress add = UDPPacket.getAddress();
                int Clientport = UDPPacket.getPort();
                UDPPacket = new DatagramPacket(buff,buff.length,add,Clientport);

                SerializeTo serialize;
                try (ByteArrayInputStream bais = new ByteArrayInputStream(buff);
                     ObjectInputStream ois = new ObjectInputStream(bais)){

                    serialize = (SerializeTo) ois.readObject();
                    System.out.println("Client is connected, and his command: "+serialize.getCommand());

                    Listener listener = new Listener(serialize,collection,UDPPacket,filename);
                    //listener.setDaemon(true);
                    listener.start();

                }catch (ClassNotFoundException e){
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
