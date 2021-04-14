package ServerWorkUDP;

import Tools.WorkWithFiles;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import essence.Inflammable;
import essence.Thing;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WorkWithCollection {
    private ConcurrentHashMap<String,Inflammable> collection;

    public WorkWithCollection(){
        collection = new ConcurrentHashMap<>();
    }

    public void setCollection(ConcurrentHashMap<String,Inflammable> coll){
        collection.putAll(coll);
    }


    public ConcurrentHashMap<String,Inflammable> getCollection(){
        return collection;
    }

    public String removeGreater(String string){

        boolean c =collection.entrySet().removeIf(entry -> entry.getValue().compareTo(string) < 0);

        if (!c)
            return "No one object was removed";
        else return "Some objects was removed";
    }

    public String remove(String key){
        String s;
        if (!key.equals("")){
            try {
                if (collection.remove(key)!=null)
                    s ="Inflammable with name "+key+" was removed";
                else if (!collection.containsKey(key))
                    s = "Wasn't removed, this object does not exist in collection";
                else
                    s= "Wasn't removed";
            }catch (NullPointerException e){
                System.err.println("Wrong format of JSON");
                s = "Wrong format of JSON";
            }

        }else s = "key is empty";
        return s;
    }

    public String show(){
        StringBuilder res = new StringBuilder();
        int i = 1;
        for (Object object: collection.entrySet()) {
            Map.Entry mapEntry = (Map.Entry) object;
            Inflammable in = (Inflammable) mapEntry.getValue();
            res.append("------------------------------------------------------------------------\n"+
                    "Object number "+ i + "\n"+
                    "Key: "+mapEntry.getKey()+"\n"+
                    "Burning power: " + in.getBurningPower()+"\n"+
                    "size: "+in.getSize()+"\n"+
                    "thing:{ name: " + in.getThing()+"}\n"+
                    "Name: "+ in.getName()+"\n"+
                    //"Date: "+in.getDate()+"\n"+
                    "------------------------------------------------------------------------\n"

            );
            ++i;
        }
        if (res.toString().equals("")){
            return "Collection is empty";
        }else
            return res.toString();
    }

    public  String info(){
        StringBuilder result = new StringBuilder();
        Iterator<Map.Entry<String,Inflammable>> e = collection.entrySet().iterator();

        result.append("Размер коллекции: ").append(collection.size()).append("\n");
        result.append("Тип коллекции: ").append(collection.getClass().getName()).append("\n");
        Date d = new Date();
        result.append("Дата инцициализации: ").append(d.toString()).append("\n");
        if (!e.hasNext()){
            Thing th = new Thing("thing1");
            Inflammable test = new Inflammable("Example", 5, 15, th );
            result.append("Key: ").append(test.getThing().getClass().getName()).append("\n");
            result.append("Value: ").append(test.getClass().getName()).append("\n");
        }else {
            Inflammable raw = e.next().getValue();
            result.append("Key: ").append(raw.getThing().getClass().getName()).append("\n");
            result.append("Value: ").append(raw.getClass().getName());
        }


        return  result.toString();
    }

    public String add(Inflammable in,boolean print ){
        String s = "";

        if (!collection.containsKey(in) & in !=null) {
            collection.put(in.getThing(), in);
            if (print) {
                s = "Object Added";
            }

        } else if (print){
            s ="This object already exist";
        }
        return s;

    }

    public String AddIfMax(Inflammable inflam){
        String s;
        boolean hz = true;
        for (Map.Entry<String,Inflammable> entry: collection.entrySet() ) {

            if (inflam.compareTo(entry.getValue().toString()) <= 0){
                hz = false;
                break;
            }
        }
        if (hz){
            collection.put(inflam.getThing(),inflam);
            s = "Object with key "+inflam.getThing()+" was added";
        }else{
            s ="We cant put this object, key "+inflam.getThing()+" not bigger than all objects";
        }

        return s;

    }

    public String clear(){
        if (collection.isEmpty())
            return "Collection already empty, but we clean it";
        collection.clear();
        return "Collection was erased";

    }

    public String imprt(String path)throws IOException {
        ArrayList<Inflammable> raw = WorkWithFiles.ReadFile(path);

        for (Inflammable inflam:raw){
            add(inflam,false);
        }
        return "Import was successful";
    }

    public byte[] save(String path){
        return saved(collection,path);
    }

    public static byte[] saved(ConcurrentHashMap<String,Inflammable> collection,String path){
        try {
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(path));
            Gson gson = new GsonBuilder().create();
            String s = gson.toJson(collection);
            outputStream.write(s.getBytes());
            outputStream.close();
            return "File was saved".getBytes();
        }
        catch (AccessDeniedException e){
            e.printStackTrace();
            return ("We don't have access to this file").getBytes();
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            return ("File was not found").getBytes();
        }
        catch (IOException e){
            e.printStackTrace();
            return ("How you did it???????").getBytes();
        }


    }

    public String help(){
        String s =(
                "For exit type: q!\n"+
                        "insert (String key) {Element} - insert a new object\n"+
                        "show - show elements of collection\n" +
                        "save - saving collection"+
                        "import {path} - adds all of the elements to a collection from a file, path - path to the .csv file"+
                        "info - show information about collection\n" +
                        "remove (String key)- remove element from collection(removing by Elemnt, just write it like in Example)\n" +
                        "remove_greater {Element} - remove all elements greater than given\n" +
                        "clear - Just type this command and all collection will be erased\n"+
                        "add_if_max {Element} - add element in collection if value more than in other elements\n"+

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

        return s;
    }


}
