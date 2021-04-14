package essence;

import java.util.ArrayList;

public class Place extends NotAlive implements HaveAble<Essence>{
    private String name;
    private ArrayList<Essence> smthIn;


    public Place(String name){
        super(name);
        this.smthIn = new ArrayList<>();
    }

    public void in(Essence essence){
        System.out.println(this.getName());
        smthIn.add(essence);
    }

    public void out(Essence essence){
        smthIn.remove(essence);
    }
}
