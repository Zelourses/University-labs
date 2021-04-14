package ru.domen;

import java.util.ArrayList;

public class Room{
    private final String roomName;
    private final ArrayList<Something> roomElements = new ArrayList<>();
    public Room(String name, Something[] startElements){
        if(name.equals(""))
            throw new RuntimeException("Room must have name");
        roomName = name;
        System.out.println(name + " существовала в холодном космосе");
        if (startElements != null && startElements.length !=0) {
            System.out.println("И в этой комнате было:");
            for (Something elem : startElements)
                putIn(elem);
        }
        System.out.println("");
    }
    public void teleportInside(Something element){
        if (element == null)
            return;
        System.out.println(element+" был телепортирован в "+roomName);
        addIn(element);
    }
    private void putIn(Something element){
        System.out.println(element+" помещён в "+roomName);
        addIn(element);
    }
    private void addIn(Something element){
        roomElements.add(element);
    }

    public boolean roomContains(Something element){
        return roomElements.contains(element);
    }

}
