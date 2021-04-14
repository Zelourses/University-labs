package essence;

abstract public class Essence {
    private String name;
    private HaveAble haveAble;

    public Essence(String name){
        this.name = name;
    }

    abstract public void enter(HaveAble haveAble);

    public void setHaveAble(HaveAble haveAble) {
        this.haveAble = haveAble;
    }

    public HaveAble getHaveAble() {
        return haveAble;
    }

    public String getName(){return this.name;}
}
