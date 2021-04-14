package essence;

public class Thing extends NotAlive implements toActionAble{
    public Thing(String name){
        super(name);
    }


    @Override
    public void _do(ActionAble actionAble) {
        actionAble.invoke();
    }
}
