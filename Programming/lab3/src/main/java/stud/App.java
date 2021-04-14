package stud;

import stud.human.Human;
import stud.human.NoNudeHuman;
import stud.human.VerbPart;
import stud.place.LockablePlace;
import stud.place.Place;
import stud.thing.Candle;
import stud.thing.Colors;
import stud.thing.Rubaha;

import java.util.ArrayList;
import java.util.Arrays;

public class App
{
    public static void main( String[] args )
    {
        Human malish = new Human("Малыш");
        malish.lookOut(VerbPart.НеУспел);

        LockablePlace garderob = new LockablePlace("Гардероб");

        int rogueNum = 2;
        Human[] rogue = new Human[rogueNum];

        for (int i = 0; i < rogue.length; i++)
            rogue[i] = new Human("Жулик " + (i + 1));

        for (int i = 0; i < rogue.length; i++)
            rogue[i].turnUp(garderob);

        Human karlson = new Human("Карлсон");

        karlson.ohivitsa(VerbPart.Снова);

        karlson.roll(VerbPart.КакЕжик);

        karlson.go(garderob);

        karlson.lock(garderob);

        Place krovat = new Place("Кровать");

        karlson.crawl(krovat, VerbPart.Ловко, VerbPart.Быстро);

        NoNudeHuman frekenBok = new NoNudeHuman(new Rubaha("Рубаха", Colors.Белый), new Candle("Свеча"), "csdsdsf");

        Place komnata = new Place("Комната");

        frekenBok.toComeIn(komnata);

        frekenBok.getDescription();
    }
}
