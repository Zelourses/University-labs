package server;

import Tools.Response;
import Tools.SerializeTo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import essence.Inflammable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ConcurrentHashMap;

public class Listener extends Thread {

    private InetAddress inetAddress;
    DatagramPacket datagramPacket;
    private String username = null;
    private String password = null;
    private String mail = null;
    private Object response;
    private ConcurrentHashMap<Integer, Inflammable> starthashMap;
    private SerializeTo serializeTo;
    private DataBaseConnection db;
    private ArrayList<Integer> connections;
    private int Thisport;

    public Listener(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    public Listener() {

    }

    public void setStart(SerializeTo ser, /*ConcurrentHashMap<String,Inflammable> map,*/ DatagramPacket datagramPacket, DataBaseConnection db,ArrayList<Integer> s,int port){
        this.serializeTo = ser;
        //this.starthashMap = map;
        this.datagramPacket = datagramPacket;
        this.db = db;
        this.connections = s;
        this.Thisport=port;

    }

    @Override
    public void run() {
        try {
            DatagramSocket udpSocket = new DatagramSocket();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            Response response = handleCommand(serializeTo,db);
            System.out.println("We are here");

            if (response.getResponse() != null) {
                oos.writeObject(response);
                oos.flush();
                datagramPacket.setData(baos.toByteArray());
                System.out.println(datagramPacket);
                udpSocket.send(datagramPacket);
            }
            if (response.getCommand().equals("Logged in")){
                SendNewClient();
            }
            if (response.getCommand().equals("Changes")){
                SendToAll(Thisport);
            }

        }

        catch (Exception e)
        {e.printStackTrace();}
    }

    public Object getResponse() {
        return response;
    }

    public Response handleCommand(SerializeTo cmd, DataBaseConnection db) {

        Object authority = cmd.getAuthority();
        if (cmd.getAuthority() != null && ((String) authority).split(" ").length > 2) {
            username = ((String) authority).split(" ")[0];
            mail = ((String) authority).split(" ")[1];
            password = ((String) authority).split(" ")[2];
        } else if (authority != null && !authority.equals(" ")&& !Objects.equals(cmd.getCommand(), "connecting") ) {
            username = ((String) authority).split(" ")[0];
            password = ((String) authority).split(" ")[1];
        }
        String command = cmd.getCommand();
        Object data = cmd.getData();
        if (username!=null){
            Object response;
            String com;
            switch (command.toLowerCase()){
                case "connecting":
                    com="connected";
                    response="connected";
                break;
                case "add":
                    boolean b=db.AddtoDatabase((Inflammable) data,username);
                    if (b){
                        com="Changes";
                        /*Inflammable in=createinflam((String)data);
                        response=db.getInflammable(in.getBurningPower(),in.getSize(),username,in.getThing(),in.getSizeUI(),in.getName());*/
                        response=db.reload2();

                    }else {
                        com="Error";
                        response="ErrorWhileAddInflam".getBytes();
                    }
                break;

                case "remove":
                    int isdeleted=db.removeInflammable(username,((Inflammable)data));
                    if (isdeleted!=0){
                        com="Changes";
                        response=db.reload2();
                    }else {
                        com="Error";
                        response="YouCantRemoveIt".getBytes();
                    }
                break;

                case "remove_lower":
                    starthashMap=db.reload2();
                    if (starthashMap!=null) {
                        remove_lower(starthashMap, ((Inflammable) data), db, username);
                        com="Changes";
                        response=db.reload2();
                    }
                    else {
                        com="Error";
                        response="SomeError".getBytes();
                    }
                break;

                case "add_if_max":
                    int isaddedMax=add_if_max(starthashMap,((Inflammable)data),db,username);
                    if (isaddedMax==1){
                        com="Changes";
                        response=db.reload2();
                    }else if (isaddedMax==0){
                        com="Error";
                        response="ItsNotMax".getBytes();
                    }else {
                        com="Error";
                        response="SomeError".getBytes();
                    }

                break;

                case "import":
                    int imported=import1(starthashMap,(ConcurrentHashMap<String,Inflammable>)data,db,username);
                    if (imported==1){
                        com="Changes";
                        response=db.reload2();
                    }else {
                        com="Error";
                        response="SomeError".getBytes();
                    }

                break;
                case "change":
                    Inflammable inflam1=db.ChangeInflam((Inflammable)data);
                    if (inflam1==null){
                        com="Error";
                        response="YouCantChangeIt".getBytes();
                    }else {
                        com="change";
                        response=inflam1;
                    }
                break;

                case "clear":
                    clear(db,username);
                    com="Changes";
                    response=db.reload2();
                break;

                case "register":
                    int resultR = db.executeRegister(username, mail, password);
                    if (resultR == 1) {
                        response = "Email was registered".getBytes();
                        com="Email was registered";
                    } else if (resultR == 0) {
                        response = "This Email is already registered".getBytes();
                        com="Error";
                    } else {
                        response = "We can't register you. Try again later".getBytes();
                        com="Error";
                    }
                break;
                case "login":
                    int result = db.executeLogin(username, password);
                    if (result == 0) {
                        response = "Logged in".getBytes();
                        com="Logged in";
                    } else if (result == 1) {
                        response = "First things first, you need to register";
                        com="Error";
                    } else if (result == 2) {
                        com="Error";
                        response = "Wrong password, try again".getBytes();
                    } else {
                        com="Error";
                        response = "Can't log in, try again later".getBytes();
                    }
                break;

                default:
                    response="WrongCommand".getBytes();
                    com="Wrong command";
            }
            starthashMap=db.reload2();

            return new Response(response,com);

        }else {
            String commando;
            Object responso;
            switch (command.toLowerCase()){
                case "connecting":
                    commando="connected";
                    responso="connected".getBytes();
                    break;

                    default:
                        commando="Don't authorized";
                        responso="Don't authorized";

                    break;
            }
            return new Response(responso,commando);
        }

    }
    public  void SendNewClient(){

        try {
            DatagramSocket udpSocket = new DatagramSocket();
            Response response1 = new Response(db.reload2(),"Changes");
            ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
            ObjectOutputStream oos1 = new ObjectOutputStream(baos1);
            oos1.writeObject(response1);
            oos1.flush();
            datagramPacket.setData(baos1.toByteArray());
            System.out.println("Sending collection to new client " +response1.getCommand()+response1.getResponse());
            udpSocket.send(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public byte[] save(ConcurrentHashMap<String,Inflammable> storage) {
        if (storage != null) {
            db.savEssence(storage);
            return "Ok".getBytes();
        } else return "We can't save nothing, sorry(we tried)".getBytes();
    }

    private void SendToAll(int notthis){
        try {
            DatagramSocket udpSocket = new DatagramSocket();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            Response response = new Response(db.reload2(),"Changes");

            if (response.getResponse() != null) {
                oos.writeObject(response);
                oos.flush();
                for (int i=0;i<connections.size();i++) {
                    datagramPacket.setData(baos.toByteArray());
                    datagramPacket.setPort(connections.get(i));
                    System.out.println(datagramPacket+" packet to client N-"+i);
                    udpSocket.send(datagramPacket);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean add(ConcurrentHashMap<Integer,Inflammable> storage, Inflammable inflam, DataBaseConnection db, String username) {
        synchronized (storage) {
            try {
                inflam.setOwner(username);
                if (storage.put(inflam.getId(),inflam) == null) {
                    db.AddtoDatabase(inflam, username);
                    System.out.println("A Inflammable " + inflam.toString()+ " added.");
                    return true;
                } else {
                    storage.put(inflam.getId(),inflam);
                    db.AddtoDatabase(inflam, username);
                    return true;
                }
            } catch (NullPointerException e) {
                return false;
            }
        }
    }


    public int add_if_max(ConcurrentHashMap<Integer,Inflammable> storage, Inflammable inflam, DataBaseConnection db, String username) {
        if (storage.size() > 0) {
            synchronized (storage.entrySet()) {
                Inflammable max = storage
                        .values()
                        .stream()
                        .max(Inflammable::compareTo)
                        .get();
                if (inflam.compareTo(max) > 0) {
                    boolean s= add(storage, inflam, db, username);
                    if (s)
                        return 1;
                    else
                        return 0;
                } else {
                    return -1;
                }
            }
        } else {
             add(storage, inflam, db, username);
            return 1;

        }
    }

    public int import1(ConcurrentHashMap<Integer,Inflammable> storage, ConcurrentHashMap<String,Inflammable> importing, DataBaseConnection db, String username) {
        long start = storage.entrySet().stream().count();
        Collection<Inflammable>imp = importing.values();
        for (Inflammable in: imp) {
            add(storage, in, db, username);
        }
        long end = storage.entrySet().stream().count();
        System.out.println("Imported "+ (end-start) + " objects.");
        return 1;
    }

    public byte[] info(ConcurrentHashMap<String,Inflammable> storage) {
        return ("Collection:" + storage.getClass() + " type.\n" +
                "contains " + storage.size() + " objects.").getBytes();
    }

    public byte[] remove(ConcurrentHashMap<String,Inflammable> storage, Inflammable inflam, DataBaseConnection db, String username){
        inflam.setOwner(username);

        Iterator<Map.Entry<String,Inflammable>> iter = storage.entrySet().iterator();
        int i =0;
        while (iter.hasNext()){
            Inflammable in = iter.next().getValue();
            if (in.getBurningPower()==inflam.getBurningPower()&&in.getSize()==inflam.getSize()&&in.getName().equals(inflam.getName())
                &&in.getOwner().equals(inflam.getOwner())){

                iter.remove();
                if (db.removeInflammable(username,inflam)>0) {
                    i++;
                    break;
                }
            }
        }
        if (i>0){
            return "We deleted this Inflammable".getBytes();
        }else
            return "We can't delete this. You can't change it, or this object does not exist".getBytes();

    }

    public byte[] remove_lower(ConcurrentHashMap<Integer,Inflammable> storage, Inflammable endObject, DataBaseConnection db, String username) {
        endObject.setOwner(username);
        long start = storage.entrySet().stream().count();
        synchronized (storage) {
            storage.entrySet().stream()
                    .filter(x -> (username.equals(x.getValue().getOwner()) || x.getValue().getOwner().equals("all")))
                    .filter(x -> x.getValue().compareTo(endObject) < 0)
                    .forEach(x -> db.removeInflammable(username, x.getValue()));
            storage.entrySet().removeIf(item -> item.getValue().compareTo(endObject) < 0 && (username.equals(item.getValue().getOwner()) || item.getValue().getOwner().equals("all")));
        }
        long end = storage.entrySet().stream().count();
        System.out.println("Deleted " + (start - end) + " objects.");
        return ("Deleted " + (start - end) + " objects. :(").getBytes();
    }

    public boolean clear(DataBaseConnection db,String username){
        db.clear(username);
        return true;
    }

    public byte[] help() {
        String s =(
                "For exit type: q!\n"+
                        "insert {Element} - insert a new object\n"+
                        "show - show elements of collection\n" +
                        "save - saving collection\n"+
                        "import {path} - adds all of the elements to a collection from a file\n"+
                        "info - show information about collection\n" +
                        "remove (Element)- remove element from collection(removing by Elemnt, just write it like in Example)\n" +
                        "remove_lower {Element} - remove all elements greater than given\n" +
                        "clear - Just type this command and all collection will be erased\n"+
                        "add_if_max {Element} - add element in collection if value more than in other elements\n" +
                        "help - list of commands\n"+

                        "\t example for insert command: \n"+"{\n" +
                        "  \"thing1\": {\n" +
                        "    \"burningPower\": 5.0,\n" +
                        "    \"size\": 15.0,\n" +
                        "    \"thing\": {\n" +
                        "      \"name\": \"ssss\"\n" +
                        "    },\n" +
                        "    \"name\": \"Example\"\n" +
                        "  }\n" +
                        "}"


        );

        return s.getBytes();
    }

    public byte[] show(ConcurrentHashMap<String,Inflammable> storage) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(outputStream)){
            ArrayList<Inflammable> list = new ArrayList<>(storage.values());
            Collections.sort(list);
            oos.writeObject(list);
            oos.flush();
            return outputStream.toByteArray();
        } catch (IOException e) {
            System.err.println("Aliens snatched the collection! Can't show it.");
        }

        return null;

    }
    private Inflammable createinflam(String data){
        data = data.replaceAll(" ","");
        data = data.replaceAll("\t","");
        data = data.replaceAll("\n","");
        data = data.replaceAll("\r","");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.enableComplexMapKeySerialization();
        Gson gson = gsonBuilder.create();
        Type type = new TypeToken<ConcurrentHashMap<String, Inflammable>>(){}.getType();
        System.out.println(data);
        ConcurrentHashMap<String,Inflammable> map = gson.fromJson(data,type);
        ConcurrentHashMap<String,Inflammable> concurrentHashMap = null;
        Inflammable inflam = map.entrySet().iterator().next().getValue();
        return inflam;
    }


}
