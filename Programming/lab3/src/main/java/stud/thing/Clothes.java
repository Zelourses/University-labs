package stud.thing;

public abstract class Clothes extends Thing {
    protected Colors color;

    public Clothes(String name, Colors color){
        super(name);
        this.color = color;
    }

    public Colors getColor(){
        return color;
    }
}
