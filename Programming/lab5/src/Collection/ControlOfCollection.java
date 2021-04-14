package Collection;

import SupportTools.WorkWithFiles;
import essence.Inflammable;
import essence.Thing;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.LinkedHashMap;

public class ControlOfCollection {
    private LinkedHashMap<String, Inflammable> controller;

    public ControlOfCollection(){
        this.controller = new LinkedHashMap<>();
    }

    public LinkedHashMap<String,Inflammable> GetCollection(){return controller;}

    public void add(String name,Inflammable in,boolean print ){
        if (!controller.containsKey(in)) {
            controller.put(name, in);
            if (print)
                System.out.println("Object added");

        } else {
            System.out.println("This object already exist");
        }

    }


    /**
     * Removes all elements that less than given
     * @param string
     */

    public void removeGreater(String string){

       for (Iterator<Map.Entry<String,Inflammable>> iter = controller.entrySet().iterator(); iter.hasNext();){
           Map.Entry<String,Inflammable> entry = iter.next();
           if (entry.getValue().compareTo(string)<0){
               iter.remove();
           }
       }
    }

    /**
     * Removing the element of collection what were given to this method
     * @param key
     */

    public void remove(String key){
        if (!key.equals("")){
            try {

                if (controller.remove(key)!=null)
                    System.out.println("Inflammable with name "+key+" was removed");
                else if (!controller.containsKey(key))
                    System.out.println("Wasn't removed, this object does not exist in collection");
                else
                    System.out.println("Wasn't removed");
            }catch (NullPointerException e){
                System.err.println("Wrong format of JSON");
            }

        }
    }

    /**
     *Returns all elements of collections with their keys
     * @return String
     */


    public String show(){
        StringBuilder res = new StringBuilder();
        int i = 1;
        for (Object object: controller.entrySet()) {
            Map.Entry mapEntry = (Map.Entry) object;
           Inflammable in = (Inflammable) mapEntry.getValue();
            res.append("------------------------------------------------------------------------\n"+
            "Object number "+ i + "\n"+
                    "Key: "+mapEntry.getKey()+"\n"+
                    "Burning power: " + in.getBurningPower()+"\n"+
                    "size: "+in.getSize()+"\n"+
                    "thing:{ name: " + in.getThing()+"}\n"+
                    "Name: "+ in.getName()+"\n"+
                    "Date: "+in.getDate()+"\n"+
                    "------------------------------------------------------------------------\n"

            );
            ++i;
        }
        if (res.toString().equals("")){
            return "Collection is empty";
        }else
        return res.toString();
    }

    /**
     * Returns type of LinkedHashMap, type of Key and Value
     * @return String
     */
    public  String info(){
        StringBuilder result = new StringBuilder();
        Iterator<Map.Entry<String,Inflammable>> e = controller.entrySet().iterator();

        result.append("Размер коллекции: "+ controller.size()+ "\n");
        result.append("Тип коллекции: "+ controller.getClass().getName()+"\n");

        if (!e.hasNext()){
            /*Thing th = new Thing("thing1");
            Inflammable test = new Inflammable("Example", 5, 15, th );
            result.append("Key: "+ test.getThing().getClass().getName()+"\n");
            result.append("Value: "+test.getClass().getName()+"\n");*/
            result.append("On this moment, collection is empty");
        }else {
            Inflammable raw = e.next().getValue();
            result.append("Key: "+ raw.getThing().getClass().getName()+"\n");
            result.append("Value: "+raw.getClass().getName());
            result.append("Дата инициализации: "+raw.getDate());
        }


        return  result.toString();
    }

    /**
     * method what creates condition of closing on end of stream(almost)
     * @param path
     */

    public void addAutoSave(String path){
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {


                try {
                    String raw = WorkWithFiles.TransformToJSON(controller);
                    WorkWithFiles.WriteInFile(raw, path);
                }
                catch (ParserConfigurationException e) {
                        e.printStackTrace();
                }catch (IOException e) {
                    System.err.println("Write in file was broken");
                }
            }
        });
    }
    public void AddIfMax(Inflammable inflam){
        boolean hz = true;
        for (Map.Entry<String,Inflammable> entry: controller.entrySet() ) {
            String raw = inflam.getThing();
            if (inflam.compareTo(entry.getValue().toString()) <= 0){
                hz = false;
                break;
            }
        }
        if (hz){
            controller.put(inflam.getThing(),inflam);
            System.out.println("New object is created");
        }else{
            System.out.println("We cant put this object");
        }

    }

    public void clear(){
        controller.clear();
        System.out.println("Collection was erased");
    }


}
