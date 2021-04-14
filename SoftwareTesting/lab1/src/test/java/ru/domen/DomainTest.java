package ru.domen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DomainTest {

    Thing fish = new Thing("Флакончик с рыбкой");
    Thing calmingThing = new Thing("Пакет кукурузных хлопьев");
    Human ford;
    Human arthur;
    Room room;

    @BeforeEach
    void init(){
        ford = new Human("Форд", fish, MentalHealth.NORMAL,
                new Thing("Грызлодёрское пиво"));
        arthur = new Human("Артур", Thing.NOTHING, MentalHealth.WORRIED,
                new Thing("Пакет кукурузных хлопьев"));
        room = new Room("дентрассийская комната",
                new Thing[]{new Thing("нижнее бельё дентрассийцев"),
                        new Thing("скворншельский матрац 1"),
                        new Thing("скворншельский матрац 2"),
                        new Thing("скворншельский матрац 3")
                });
    }

    @Test
    void testDefaultText(){
        room.teleportInside(arthur);
        room.teleportInside(ford);
        Assertions.assertEquals(MentalHealth.WORRIED,arthur.checkMentalHealth());
        Assertions.assertFalse(arthur.tryToCheerUp(room));
    }
    @Test
    void testInvalidWays(){
        room.teleportInside(calmingThing);
        room.teleportInside(arthur);
        room.teleportInside(ford);

        Assertions.assertEquals(MentalHealth.WORRIED,arthur.checkMentalHealth());
        Assertions.assertTrue(arthur.tryToCheerUp(room));
        Assertions.assertEquals(MentalHealth.NORMAL,arthur.checkMentalHealth());
        Assertions.assertFalse(arthur.tryToCheerUp(room));
        Assertions.assertEquals(Thing.NOTHING, arthur.holds());
        Assertions.assertEquals(fish, ford.holds());
    }
}
