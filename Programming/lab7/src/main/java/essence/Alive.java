package essence;


abstract public class Alive extends Essence implements toActionAble{

    public Alive(String name){
        super(name);
    }

    @Override
    public void _do(ActionAble actionAble) {
        System.out.print(this.getName() + " ");
        actionAble.invoke();
    }

    @Override
    public void enter(HaveAble haveAble) {
        System.out.print(this.getName() + " вошел в ");

        haveAble.in(this);

        this.setHaveAble(haveAble);
    }
}
