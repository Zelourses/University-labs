package stud.place;

public class LockablePlace extends Place{

    protected boolean isLocked = false;

    public LockablePlace(String name){
        super(name);
    }

    public void changeLock(){
        isLocked = !isLocked;
    }
}
