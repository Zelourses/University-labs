package ru.domen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HumanTest {

    private Human test;
    private final Thing testThing = new Thing("cheer");
    @BeforeEach
    void initHuman(){
        test = new Human("test",Thing.NOTHING,MentalHealth.NORMAL,null);
    }

    @Test
    void testExceptions(){
        Assertions.assertThrows(RuntimeException.class, ()->{
            new Human("",null,null,null);
        });
        Assertions.assertThrows(RuntimeException.class, ()->{
            new Human(null,null,MentalHealth.NORMAL,null);
        });

    }

    @Test
    void holds() {
        Assertions.assertEquals(Thing.NOTHING,test.holds());
    }

    @Test
    void checkMentalHealth() {
        Assertions.assertEquals(MentalHealth.NORMAL,test.checkMentalHealth());
        test = new Human("test",Thing.NOTHING,MentalHealth.WORRIED,testThing);
        Assertions.assertTrue(test.tryToCheerUp(new Room("test",new Thing[]{testThing})));
    }

    @Test
    void tryToCheerUp() {
        Assertions.assertFalse(test.tryToCheerUp(null));
        Assertions.assertFalse(test.tryToCheerUp(new Room("something",null)));
        test = new Human("tt",null,MentalHealth.WORRIED,testThing);
        Assertions.assertTrue(test.tryToCheerUp(new Room("room",new Thing[]{testThing})));
    }
}
