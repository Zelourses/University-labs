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
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Listener extends Thread {

    private InetAddress inetAddress;
    DatagramPacket datagramPacket;
    private String username = null;
    private String password = null;
    private String mail = null;
    private int port;
    public static String fileName;
    public static String FILEPATH = "karlson.json";
    private Object response;
    private ConcurrentHashMap<String, Inflammable> starthashMap;
    private SerializeTo serializeTo;
    private DataBaseConnection db;

    public Listener(InetAddress inetAddress, int port) {
        this.inetAddress = inetAddress;
        this.port = port;
    }

    public Listener() {

    }

    public void setStart(SerializeTo ser, ConcurrentHashMap<String,Inflammable> map, DatagramPacket datagramPacket, DataBaseConnection db){
        this.serializeTo = ser;
        this.starthashMap = map;
        this.datagramPacket = datagramPacket;
        this.db = db;

    }

    @Override
    public void run() {
        try {
            DatagramSocket udpSocket = new DatagramSocket();
            this.response = handleCommand(serializeTo, starthashMap, db);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            Response response = new Response(this.getResponse());

            if (response.getResponse() != null) {
                oos.writeObject(response);
                oos.flush();
                datagramPacket.setData(baos.toByteArray());
                System.out.println(datagramPacket);
                udpSocket.send(datagramPacket);
            }
        }
        catch (Exception e)
        {e.printStackTrace();}
    }

    public Object getResponse() {
        return response;
    }

    public Object handleCommand(SerializeTo cmd, ConcurrentHashMap<String,Inflammable> storage, DataBaseConnection db) {

        Object authority = cmd.getAuthority();
        if (cmd.getAuthority() != null && ((String) authority).split(" ").length > 2) {
            username = ((String) authority).split(" ")[0];
            mail = ((String) authority).split(" ")[1];
            password = ((String) authority).split(" ")[2];
        } else if (authority != null && !authority.equals(" ")&& !Objects.equals(cmd.getCommand(), "connecting") ) {
            username = ((String) authority).split(" ")[0];
            password = ((String) authority).split(" ")[1];
        }else if( !Objects.equals(cmd.getCommand(), "connecting")){
            return "Don't authorized".getBytes();
        }

            String command = cmd.getCommand();
            Object data = cmd.getData();

            byte[] buffer;
            int l=0;

            switch (command.toLowerCase()) {
                case "connecting":
                    buffer = "connected".getBytes();
                    break;
                case "insert":
                    if (data != null) {
                        buffer = add(storage, createinflam((String) data), db, username);
                    } else {buffer = help();}
                    break;
                case "add_if_max":
                    if (data != null) {
                        buffer = add_if_max(storage, createinflam((String) data), db, username);
                    } else {buffer = help();}
                    break;
                case "remove":
                    if (data != null) {
                        buffer = remove(storage, createinflam((String) data), db, username);
                    } else {buffer = help();}
                    break;
                case "remove_lower":
                    if (data != null) {
                        buffer = remove_lower(storage, createinflam((String) data), db, username);
                    } else {buffer = help();}
                    break;
                case "show":
                    buffer = show(storage);
                    break;
                case "save":
                    buffer = save(storage);
                    break;
                case "import":
                    buffer = import1(storage, (ConcurrentHashMap<String,Inflammable>) data, db, username);
                    break;
                case "info":
                    buffer = info(storage);
                    break;
                case "help":
                    buffer = help();
                    break;
                case "clear":
                    buffer = clear(db,username,starthashMap);
                    break;
                case "register":
                    int resultR = db.executeRegister(username, mail, password);
                    if (resultR == 1) {
                        buffer = "Email was registered".getBytes();
                    } else if (resultR == 0) {
                        buffer = "This Email is already registered".getBytes();
                    } else if (resultR==-1){
                        buffer = "We can't register you. Try again later".getBytes();
                    }else {
                        buffer = "Some problems with your email, try another".getBytes();
                    }
                    break;
                case "login":
                    int result = db.executeLogin(username, password);
                    if (result == 0) {
                        buffer = "Logged in".getBytes();
                    } else if (result == 1) {
                        buffer = "First things first, you need to register".getBytes();
                    } else if (result == 2) {
                        buffer = "Wrong password, try again".getBytes();
                    } else {
                        buffer = "Can't log in, try again later".getBytes();
                    }
                    break;
                default:
                    l++;
                    if (l==5) {
                        buffer = "Wrong command: Type \"help\" for a list of available commands".getBytes();
                        l=0;
                    }else
                        buffer ="".getBytes();
            }
            return buffer;

    }
    public byte[] save(ConcurrentHashMap<String,Inflammable> storage) {
        if (storage != null) {
            db.savEssence(storage);
            return "new Inflammables arrived to our cozy databse".getBytes();
        } else return "We can't save nothing, sorry(we tried)".getBytes();
    }






    public byte[] add(ConcurrentHashMap<String,Inflammable> storage, Inflammable inflam, DataBaseConnection db, String username) {
        synchronized (storage) {
            try {
                inflam.setOwner(username);
                if (storage.put(inflam.getThing(),inflam) == null) {
                    db.AddtoDatabase(inflam, username);
                    System.out.println("A Inflammable " + inflam.toString()+ " added.");
                    return ("Inflammanle added.").getBytes();
                } else {
                    storage.put(inflam.getThing(),inflam);
                    db.AddtoDatabase(inflam, username);
                    return ("Inflammable replaced").getBytes();
                }
            } catch (NullPointerException e) {
                return "wrong type of inflammable".getBytes();
            }
        }
    }


    public byte[] add_if_max(ConcurrentHashMap<String,Inflammable> storage, Inflammable inflam, DataBaseConnection db, String username) {
        if (storage.size() > 0) {
            synchronized (storage.entrySet()) {
                Inflammable max = storage
                        .values()
                        .stream()
                        .max(Inflammable::compareTo)
                        .get();
                if (inflam.compareTo(max) > 0) {
                    return add(storage, inflam, db, username);
                } else {
                    return (inflam.getName()+ "'s name isn't the biggest: Can't add to a collection!").getBytes();
                }
            }
        } else {
            return add(storage, inflam, db, username);
        }
    }

    public byte[] import1(ConcurrentHashMap<String,Inflammable> storage, ConcurrentHashMap<String,Inflammable> importing, DataBaseConnection db, String username) {
        long start = storage.entrySet().stream().count();
        Collection<Inflammable>imp = importing.values();
        for (Inflammable in: imp) {
            add(storage, in, db, username);
        }
        long end = storage.entrySet().stream().count();
        System.out.println("Imported "+ (end-start) + " objects.");
        return ("+++++ Imported "+ (end-start) + " objects +++++").getBytes();
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
                if (db.removeInflammable(username,inflam)) {
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

    public byte[] remove_lower(ConcurrentHashMap<String,Inflammable> storage, Inflammable endObject, DataBaseConnection db, String username) {
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

    public byte[] clear(DataBaseConnection db,String username,ConcurrentHashMap<String,Inflammable> base){
        db.clear(username);
        return "deleted".getBytes();
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
        try {
            starthashMap = db.reload();

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(outputStream)){
            ArrayList<Inflammable> list = new ArrayList<>(storage.values());
            Collections.sort(list);
            oos.writeObject(list);
            oos.flush();
            return outputStream.toByteArray();
        } catch (IOException e) {
            System.err.println("Can't show collection, it's not working");
        }

        } catch (SQLException e) {
            System.out.println("something went wrong while reloading");
            e.printStackTrace();
            return null;
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