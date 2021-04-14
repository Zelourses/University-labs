package ru.domen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ThingTest {

    @Test
    void thingTest(){
        Assertions.assertThrows(RuntimeException.class, ()->new Thing(null));
        Assertions.assertDoesNotThrow(()->new Thing("asdfghjkaadjhdajgsdkjgaasdasdasdasdasdasdasdsdgjasd"));
    }
}
