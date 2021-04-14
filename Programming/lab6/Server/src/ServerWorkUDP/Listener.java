package ServerWorkUDP;

import Tools.ClientAnswers;
import Tools.SerializeTo;
import Tools.StringEntity;
import Tools.WorkWithFiles;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import essence.Inflammable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ConcurrentHashMap;

public class Listener extends Thread{

    private Object ClientAnswer;
    private SerializeTo serialize;
    private WorkWithCollection collection;
    private DatagramPacket UDPPacket;
    private String path;


    public Listener(SerializeTo ser, WorkWithCollection col,DatagramPacket datagramPacket, String path){
        this.serialize =ser;
        this.collection = col;
        this.UDPPacket = datagramPacket;
        this.path = path;
    }

    public Object getClientAnswer(){return ClientAnswer;}

    @Override
    public void run(){
        try {
            DatagramSocket udpSocket = new DatagramSocket();
            this.ClientAnswer = ParseCommand(serialize);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            ClientAnswers response = new ClientAnswers(this.getClientAnswer());
            System.out.println(response.getClientAnswer());

            if (response.getClientAnswer() != null) {
                oos.writeObject(response);
                oos.flush();
                UDPPacket.setData(baos.toByteArray());
                //System.out.println(UDPPacket);//для дебага
                udpSocket.send(UDPPacket);
            }
        }
        catch (Exception e)
        {e.printStackTrace();}
    }

    private  Object ParseCommand(SerializeTo to){
        synchronized (Listener.class){
            String command = to.getCommand();
            Object data =  to.getData();
            System.out.println(data);

            byte[] buff = "Wow, such empty".getBytes();

            switch (command.toLowerCase()){
                case "connecting":
                    buff = "connected".getBytes();
                    break;
                case "insert":
                    if (data != null)
                    {
                       Inflammable in = createinflam((String) data);
                        buff = collection.add(in,true).getBytes();
                    } else {buff = collection.help().getBytes();}
                    break;
                case "add_if_max":
                    if (data != null) {
                        Inflammable in = createinflam((String) data);
                        buff = collection.AddIfMax(in).getBytes();
                    } else {buff = collection.help().getBytes();}
                    break;
                case "remove_greater":
                    if (data != null) {
                        //Inflammable in = createinflam((String) data);
                        buff = collection.removeGreater((String)data).getBytes();
                    } else {buff = collection.help().getBytes();}
                    break;
                case "remove":
                    if (data != null) {
                        //Inflammable in = createinflam((String) data);
                        buff = collection.remove((String)data).getBytes();
                    } else {buff = collection.help().getBytes();}
                    break;
                case "show":
                    buff = collection.show().getBytes();
                    break;
                case "save":
                    buff = collection.save(path);
                    break;
                case "import":
                    try {
                        buff = collection.imprt((String) data).getBytes();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "clear":
                    buff = collection.clear().getBytes();
                    break;
                case "info":
                    buff = collection.info().getBytes();
                    break;
                case "help":
                    buff = collection.help().getBytes();
                    break;
                default:
                    buff = "Error: undefined command Type \"help\" for commands".getBytes();
            }
            return buff;
        }


    }
    private Inflammable createinflam(String data){
        Gson r = new Gson();
        //Type type = new TypeToken<Inflammable>(){}.getType();
        data = data.replaceAll(" ","");
        data = data.replaceAll("\t","");
        data = data.replaceAll("\n","");
        data = data.replaceAll("\r","");
        char[] b =data.toCharArray();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.enableComplexMapKeySerialization();
        Gson gson = gsonBuilder.create();
        Type type = new TypeToken<ConcurrentHashMap<String, Inflammable>>(){}.getType();
        ConcurrentHashMap<String,Inflammable> map = gson.fromJson(data,type);
        ConcurrentHashMap<String,Inflammable> concurrentHashMap = null;
        Inflammable inflam = map.entrySet().iterator().next().getValue();
        return inflam;
    }

}
