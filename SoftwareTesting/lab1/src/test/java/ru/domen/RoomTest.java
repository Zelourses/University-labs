package ru.domen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RoomTest {

    private Room room;

    @BeforeEach
    void initRoom(){
        room = new Room("testRom",null);
    }

    @Test
    void testExceptions(){
        Assertions.assertThrows(RuntimeException.class,()->new Room(null,null));
        Assertions.assertDoesNotThrow(()->new Room("test",null));
    }

    @Test
    void teleportInside() {
        Assertions.assertDoesNotThrow(()->room.teleportInside(null));
    }

    @Test
    void roomContains() {
        Assertions.assertFalse(room.roomContains(new Thing("something")));
        room.teleportInside(new Thing("something"));
        Assertions.assertTrue(room.roomContains(new Thing("something")));
    }
}
