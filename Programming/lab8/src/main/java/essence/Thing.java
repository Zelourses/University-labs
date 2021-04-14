package essence;

import java.io.Serializable;

public class Thing extends NotAlive implements toActionAble, Serializable {
    public Thing(String name){
        super(name);
    }


    @Override
    public void _do(ActionAble actionAble) {
        actionAble.invoke();
    }
}
