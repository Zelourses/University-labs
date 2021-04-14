package ru.domen;

import javax.naming.InvalidNameException;

public class Human implements Something, Holding{
    private final String name;
    private Thing thing;
    private MentalHealth mentalHealth;
    private final Thing somethingThatWillCheerUp;

    public Human(String name, Thing holding, MentalHealth mentalHealth, Thing cheeringUp){
        if (mentalHealth == null)
            throw new RuntimeException("Human must have valid mental health");
        if (name.equals(""))
            throw new RuntimeException("Human must have valid name");
        this.name = name;
        this.thing = holding;
        this.mentalHealth = mentalHealth;
        somethingThatWillCheerUp = cheeringUp;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Thing holds() {
        return thing;
    }
    public MentalHealth checkMentalHealth(){
        System.out.println(name + " сейчас " + mentalHealth);
        if (mentalHealth != MentalHealth.NORMAL && somethingThatWillCheerUp != null){
            System.out.println(name + " сейчас бы увидеть " + somethingThatWillCheerUp+ " чтобы успокоится");
        }
        if ( somethingThatWillCheerUp != null){
            System.out.println("Но нет ничего, что бы улучшило настроение "+name);
        }

        return mentalHealth;
    }
    public boolean tryToCheerUp(Room room){
        if (mentalHealth != null && mentalHealth == MentalHealth.NORMAL){
            System.out.println(name + " уже в "+MentalHealth.NORMAL);
            return false;
        }
        if (room == null)
            return false;
        System.out.println(name+" смотрит вокруг");
        if (somethingThatWillCheerUp != null && room.roomContains(somethingThatWillCheerUp)){
            mentalHealth = MentalHealth.NORMAL;
            System.out.println("Глазами "+ name + " увидел "+somethingThatWillCheerUp+" и теперь спокоен");
            return true;
        }
        System.out.println("Ничего, что улучшит состояние "+name+" здесь нет");
        return false;
    }
}
