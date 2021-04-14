package ru.parsing;

public class StringStruct {
    public String x, my, lib;

    public StringStruct(String x, String my, String lib){
        this.x = x;
        this.my = my;
        this.lib = lib;
    }

    @Override
    public String toString() {
        return this.x+";"+lib;
    }
}
