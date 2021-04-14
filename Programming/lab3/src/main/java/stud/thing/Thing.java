package stud.thing;

public abstract class Thing {
    protected String name;

    public Thing(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
