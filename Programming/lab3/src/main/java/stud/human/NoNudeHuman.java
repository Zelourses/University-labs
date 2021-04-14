package stud.human;

import stud.place.LockablePlace;
import stud.place.Place;
import stud.thing.Clothes;
import stud.thing.Thing;

public class NoNudeHuman extends  Human{
    private Human human;
    private Clothes clothes;
    private Thing inHand;

    public NoNudeHuman(Clothes clothes, Thing thing, String name){
        super(name);
        this.human = new Human(name);
        this.clothes = clothes;
        this.inHand = thing;
    }

    public void getDescription(){
        System.out.println(getName() + " одета в " + clothes.getColor() + " " + clothes.getName() + " а в руке держит " + inHand.getName());
    }

    public Clothes getClothes(){
        return clothes;
    }

    public Thing getInHand(){
        return inHand;
    }
}
