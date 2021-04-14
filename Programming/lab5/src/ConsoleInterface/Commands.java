package ConsoleInterface;

import Collection.ControlOfCollection;
import essence.Inflammable;
import essence.Thing;

import java.util.Date;
import java.util.Random;

public class Commands {
    private ControlOfCollection collection;

    public Commands(ControlOfCollection coll){
        this.collection = coll;
    }

    public void AddIfMax(Inflammable inflams){
        collection.AddIfMax(inflams);
    }

    public void remove(String key){
        collection.remove(key);
    }

    public void removeGreater(String in){
        collection.removeGreater(in);
    }

    public void add(String name,Inflammable in,boolean print ){
        collection.add(name,in,print);
    }


    public void clear(){
        collection.clear();
    }

    public void info(){
        System.out.println(collection.info());
    }

    public void show(){
        System.out.println(collection.show());
    }

    public void example(){
        System.out.println("example: insert Example \n" +
                "{\n" +
                "\"burningPower\": 5.0,\n" +
                "\"size\": 15.0,\n" +
                "\"thing\": {\n" +
                "  \"name\": \"thing\"\n" +
                "},\n" +
                "\"name\": \"Example\"\n" +
                "}"
        );
    }

    public void help(){
        System.out.println(
                "For exit type: q!\n"+
                        "insert (String key) {Element} - insert a new object\n"+
                        "show - show elements of collection\n" +
                        "info - show information about collection\n" +
                        "remove (String key)- remove element from collection(removing by Elemnt, just write it like in Example)\n" +
                        "remove_greater {Element} - remove all elements greater than given\n" +
                        "clear - Just type this command and all collection will be erased\n"+
                        "add_if_max {Element} - add element in collection if value more than in other elements\n"+

                        "\t if you want to see example, just type: example "

        );


    }

    public void CreateExample(){
        Random rand = new Random();
        Date d = new Date();
        d.getTime();
        Thing th = new Thing("ExampleThing"+rand.nextGaussian()*20);
        Inflammable in  = new Inflammable("Example",(float) rand.nextGaussian()*15,(float) rand.nextGaussian()*13,th,d);
        collection.add(in.getThing(),in,true);
        // System.out.println("One Inflammable created");

    }
}
