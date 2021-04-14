package essence;

import java.io.Serializable;

abstract public class NotAlive extends Essence implements Serializable {
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
