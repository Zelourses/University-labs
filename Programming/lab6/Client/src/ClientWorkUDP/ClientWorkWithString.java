package ClientWorkUDP;


import Tools.SerializeTo;
import Tools.StringEntity;
import Tools.WorkWithFiles;
import essence.Inflammable;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

public class ClientWorkWithString {

    private HashSet<String> CommandsWithoutManyLinesIn = new HashSet<>();
    private HashSet<String> CommandsWithManyLinesIn = new HashSet<>();
    private HashSet<String> CommandWithOneArgument = new HashSet<>();

    public boolean CommandisEnded = true;
    private boolean IsBracketOpened;

    private short LeaveCounter = 1;
    private short Leave = 5;
    private byte BracketsCounter =0;

    private String resultsOfArg;


    private StringBuilder builder = new StringBuilder();
    private ByteArrayOutputStream out;



    public ClientWorkWithString(){
        CommandsWithoutManyLinesIn.add("show");
        CommandsWithoutManyLinesIn.add("info");
        CommandsWithoutManyLinesIn.add("clear");
        CommandsWithoutManyLinesIn.add("help");
        CommandsWithoutManyLinesIn.add("save");

        CommandWithOneArgument.add("remove");
        CommandWithOneArgument.add("load");

        CommandsWithManyLinesIn.add("remove_greater");
        CommandsWithManyLinesIn.add("insert");
        CommandsWithManyLinesIn.add("add_if_max");
    }

    public ByteArrayOutputStream ConsoleWork(String command)throws IOException{
        if (command!=null){
            command = DeleteLastSpaces(command);

            if(CommandsWithoutManyLinesIn.contains(command)) {
                out= StartWorkWithOneLineCommand(command);
            }else{
                if (CommandisEnded){
                    String[] rawcouple = split(command);

                    if (CommandsWithManyLinesIn.contains(rawcouple[0])){
                        CommandisEnded = false;
                        IsBracketOpened = false;

                        resultsOfArg = rawcouple[0];
                        LeaveCounter = 1;

                        if (rawcouple[1] != null){
                            builder.append(rawcouple[1]);
                            CountBrackets(rawcouple[1]);
                        }
                    }else if (CommandWithOneArgument.contains(rawcouple[0])){
                        out =StartWorkWithOneArgumentCommand(rawcouple[0],rawcouple[1]);
                    }else {
                        out =null;
                    }


                }else{
                    if (command.equals(""))
                        LeaveCounter++;
                    else
                        builder.append(command);

                    CountBrackets(command);
                }
            }
        }
        if (CommandisEnded) {
            //System.out.print("~$ ");
            return out;
        }else {
            //System.out.print("~$ ");
            return null;
        }


    }


    private ByteArrayOutputStream StartWorkWithOneArgumentCommand(String command,String argument)throws IOException{
        if (argument.equals("")) {
            System.out.println("You don't wrote the argument. Please, write it");
            return null;
        }else{
            return createRequest(command,argument);
        }
    }

    private void CountBrackets(String command){

        for (char ch : command.toCharArray()){
            if (ch == '{') {
                BracketsCounter++;
                IsBracketOpened = true;
            }
            else if (ch == '}')
                BracketsCounter--;
        }

        if (!IsBracketOpened && BracketsCounter < 0){
            CommandisEnded = true;

        }

        if (LeaveCounter >= Leave){
            CommandisEnded = true;

            try {
               out = StartWorkWithMultilineCommand();
            } catch (IOException e) {
                System.out.println("Wow, IOException");
            }
        }

        if (BracketsCounter == 0) {
            if (IsBracketOpened) {
                CommandisEnded = true;

                try {
                   out = StartWorkWithMultilineCommand();
                } catch (IOException e) {
                    System.out.println("wow, IOException");
                }
            }
            else {
                LeaveCounter++;
            }

        }
    }

    private ByteArrayOutputStream StartWorkWithMultilineCommand()throws IOException{
        String args = builder.toString();

        for (int i=0;i<args.length();i++){
            if (args.charAt(i) =='{'){
                args = args.substring(i);

                break;
            }
        }
            boolean s =WorkWithFiles.CheckIfInflammableIn(args);
            builder = new StringBuilder();
            if (s) {

                return createRequest(resultsOfArg, args);
            }
            else {
                return null;
            }


    }

    private ByteArrayOutputStream StartWorkWithOneLineCommand(String command)throws IOException{ //show, info, clear, help, remove, example
        return createRequest(command,null);
    }



    private String DeleteLastSpaces(String str){
        int i = str.length() - 1;

        while (i >= 0 && str.charAt(i) == ' ')
            i--;

        return str.substring(0, i + 1);
    }

    private String[] split(String command){
        String[] raw =new String[2];

        for (int i=0;i<command.length();i++){
            if (command.charAt(i) ==' '){
                raw[0] = command.substring(0,i);
                raw[1] = command.substring(i+1);

                break;
            }
        }
        if(raw[0]==null){
            raw[0] = command;
        }

        return raw;
    }

    public static  ByteArrayOutputStream createRequest(String command, String data)throws IOException{
        if (data !=null)
        data.replaceAll(" ","");

        SerializeTo c = new SerializeTo(command, data);

        if (command.equals("insert") || command.equals("add_if_min")
                || command.equals("remove") || command.equals("add_if_max")
                || command.equals("remove_lower")) {
            try {
                c.setData(data);
            } catch (Exception e) {
                return null;
            }
        } else if (command.equals("import")) {
            ArrayList<Inflammable> raw = WorkWithFiles.ReadFile(data);
            ConcurrentHashMap<String,Inflammable> rawmap = new ConcurrentHashMap<>();
            for (int i =0;i<raw.size();i++){
                rawmap.put(raw.get(i).getThing(),raw.get(i));
            }
            c.setData(rawmap);
            if (c.getData() == null) {
                return null;
            }
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(outputStream)){
            oos.writeObject(c);
            oos.flush();
            return outputStream;
        } catch (IOException e) {
            throw new IOException();
        }

    }

}