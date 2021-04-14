package stud.human;

import stud.place.LockablePlace;
import stud.place.Place;

public interface ToLockAble {
    void lock(LockablePlace place);
    void unlock(LockablePlace place);
}
