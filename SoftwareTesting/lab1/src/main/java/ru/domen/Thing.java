package ru.domen;

import java.util.Objects;

public class Thing implements Something{
    private String name;
    public Thing(String name){
        if (name.equals(""))
            throw new RuntimeException("Thing must have name");
        this.name = name;
    }
    public static Thing NOTHING = new Thing("Ничего");

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Thing thing = (Thing) o;
        return Objects.equals(name, thing.name);
    }

    @Override
    public String toString() {
        return name;
    }
}
