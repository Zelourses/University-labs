package stud.place;

import stud.human.Human;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;

public class Place {
    protected String name;
    protected ArrayList<Human> humanIn;


    public Place(String name){
        this.name = name;
        this.humanIn = new ArrayList<>();
    }

    public String getName(){
        return name;
    }

    public void in(Human... humans){
            humanIn.addAll(Arrays.asList(humans));
    }

    public void out(Human... humans){
        humanIn.removeAll(Arrays.asList(humans));
    }
}
