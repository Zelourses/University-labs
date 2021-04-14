package essence;

abstract public class NotAlive extends Essence{
    public NotAlive(String name){
        super(name);
    }

    @Override
    public void enter(HaveAble haveAble) {
        System.out.print(this.getName() + " помещен в ");

        haveAble.in(this);

        this.setHaveAble(haveAble);
    }
}
