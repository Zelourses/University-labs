package stud.human;

import stud.place.LockablePlace;
import stud.place.Place;

public class Human implements ActionAble, MoveAble, ToLockAble{
    private String name;

    public Human(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    @Override
    public void turnUp(Place place, VerbPart... verbParts) {
        System.out.print(getName());

        for (VerbPart value : verbParts)
            System.out.print(" " + value);
        System.out.println(" оказался в " + place.getName());
    }

    @Override
    public void turnUp(Place place) {
        System.out.println(getName() + " оказался в " + place.getName());
    }

    @Override
    public void roll(Place place, VerbPart... verbParts) {
        System.out.print(getName());

        for (VerbPart value : verbParts)
            System.out.print(" " + value);
        System.out.println(" перекатился в" + place.getName());
    }

    @Override
    public void roll(VerbPart... verbParts) {
        System.out.print(getName() + " перекатился ");

        for (VerbPart value : verbParts)
            System.out.print(" " + value);

        System.out.println();
    }

    @Override
    public void go(Place place, VerbPart... verbParts) {
        System.out.print(getName());

        for (VerbPart value : verbParts)
            System.out.print(" " + value);
        System.out.println(" двинулся к" + place.getName());

    }

    @Override
    public void go(Place place) {
        System.out.println(getName() + " двинулся к " + place.getName());
    }

    @Override
    public void crawl(Place place) {
        System.out.println(getName() + " прополз под " + place.getName());
    }

    @Override
    public void crawl(Place place, VerbPart... verbParts) {
        System.out.print(getName());

        for (VerbPart value : verbParts)
            System.out.print(" " + value);
        System.out.println(" прополз под " + place.getName());
    }

    @Override
    public void toComeIn(Place place) {
        System.out.println(getName() + " вошла в " + place.getName());
    }

    @Override
    public void lookOut() {
        System.out.println(getName() + " оглянуться");
    }

    @Override
    public void lookOut(VerbPart verbPart) {
        System.out.println(getName() + " " + verbPart + " оглянуться");
    }

    @Override
    public void ohivitsa() {
        System.out.println(getName() + " охивился");
    }

    @Override
    public void ohivitsa(VerbPart verbPart) {
        System.out.println(getName() + " охивился " + verbPart);
    }

    @Override
    public void lock(LockablePlace place) {
        System.out.println(getName() + " запер " + place.getName());

        place.changeLock();
    }

    @Override
    public void unlock(LockablePlace place) {
        System.out.println(getName() + " открыл " + place.getName());

        place.changeLock();
    }
}
