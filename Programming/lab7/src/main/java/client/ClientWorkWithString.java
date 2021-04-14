package client;

import Tools.Response;
import Tools.SerializeTo;
import Tools.WorkWithFiles;
import essence.Inflammable;
import server.DataBaseConnection;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientWorkWithString {
    private java.io.Console console = System.console();

    private HashSet<String> CommandsWithoutManyLinesIn = new HashSet<>();
    private HashSet<String> CommandsWithManyLinesIn = new HashSet<>();
    private HashSet<String> CommandWithOneArgument = new HashSet<>();
    private HashSet<String> Registration = new HashSet<>();

    public boolean CommandisEnded = true;
    private boolean IsBracketOpened;

    private short LeaveCounter = 1;
    private short Leave = 5;
    private byte BracketsCounter =0;

    public String CommandToSend;

    private String resultsOfArg;


    private StringBuilder builder = new StringBuilder();
    private ByteArrayOutputStream out;

    private String username="";
    private String password="";

    public boolean isExit=false;
    private ClientWorker worker;



    public ClientWorkWithString(ClientWorker worker){
        this.worker = worker;
        CommandsWithoutManyLinesIn.add("show");
        CommandsWithoutManyLinesIn.add("info");
        CommandsWithoutManyLinesIn.add("clear");
        CommandsWithoutManyLinesIn.add("help");
        CommandsWithoutManyLinesIn.add("save");


        CommandWithOneArgument.add("load");
        CommandWithOneArgument.add("import");

        CommandsWithManyLinesIn.add("remove");
        CommandsWithManyLinesIn.add("remove_lower");
        CommandsWithManyLinesIn.add("insert");
        CommandsWithManyLinesIn.add("add_if_max");

        Registration.add("login");
        Registration.add("register");
    }
    public ByteArrayOutputStream startwork()throws IOException{
        ByteArrayOutputStream out;
        Scanner consolein = new Scanner(System.in);
        String nextString = "";
        do {
            nextString = consolein.nextLine();
            out=ConsoleWork(nextString);
            if (out==null)
                System.out.print("~$ ");
        }while (out ==null);
        return out;
    }

    public ByteArrayOutputStream ConsoleWork(String command)throws IOException{
        if (command!=null){
            command = DeleteLastSpaces(command);

            if (command.equals("exit"))
                isExit = true;

            if ((!password.equals("")|| !username.equals(""))) {
                if(CommandsWithoutManyLinesIn.contains(command)) {
                    out= StartWorkWithOneLineCommand(command);
                    CommandToSend=command;
                }else{
                    if (CommandisEnded){
                        String[] rawcouple = split(command);
                        CommandToSend=command;
                        if (Registration.contains(rawcouple[0]) && rawcouple[1].equals("")){
                            out = StartWorkWithRegistration(rawcouple[0]);

                        }

                        if (CommandsWithManyLinesIn.contains(rawcouple[0])){
                            CommandisEnded = false;
                            IsBracketOpened = false;

                            resultsOfArg = rawcouple[0];
                            LeaveCounter = 1;

                            CommandToSend=command;


                            if (rawcouple[1] != null){
                                builder.append(rawcouple[1]);
                                CountBrackets(rawcouple[1]);
                            }
                        }else if (CommandWithOneArgument.contains(rawcouple[0])){
                            out =StartWorkWithOneArgumentCommand(rawcouple[0],rawcouple[1]);
                            CommandToSend=command;

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
            }else if (Registration.contains(command)){
                out =StartWorkWithRegistration(command);
                CommandToSend=command;
            }else {
                System.out.println("You need to be authorized");
                System.out.println("Type login, or register");
                //System.out.print("~$ ");
            }
        }
        if (CommandisEnded) {
            //System.out.print("~$ ");
            CommandToSend=command;
            return out;
        }else {
            //System.out.print("~$ ");
            return null;
        }


    }

    private ByteArrayOutputStream StartWorkWithRegistration(String command)throws IOException, NoSuchElementException {

        ByteArrayOutputStream s;
        Scanner scan = new Scanner(System.in);
        switch (command){
            case "register":
                System.out.println("Enter your username without spaces");
                System.out.print("~$/Username: ");

                String username = scan.nextLine().trim().replaceAll("\\s","");

                while (username.equals("")){
                    System.out.println("Name must be 1 char or more");
                    System.out.print("~$/Username: ");
                    username = scan.nextLine().trim().replaceAll("\\s+", "");
                }

                this.username = username;

                System.out.println("Enter your email:");
                System.out.print("~$/Email: ");
                String email = scan.nextLine();

                s= createRequest("register",null,username+" "+email+ " "+ DataBaseConnection.getPass());
                break;

            case "login":
                System.out.println("Enter your username without spaces");
                System.out.print("~$/Username: ");
                this.username = scan.nextLine().trim().replaceAll("\\s","");

                while (this.username.equals("")) {
                    System.out.println("Your username must contain at least 1 char");
                    System.out.print("~$/Username: ");
                    this.username = scan.nextLine().trim();
                }

                System.out.println("Enter your password");
                System.out.print("~$/password: ");
                String test;

                if (console !=null){
                    password = new String(console.readPassword()).trim();
                }else
                    password = scan.nextLine().trim();

                this.password =DataBaseConnection.encryptString(password);

                s= createRequest("login",null,this.username+" "+this.password);
                if (!testLogin(s)){
                    this.username="";
                    this.password="";
                    System.out.println("We don't find this user");
                }
                break;
                default:
                    return null;
        }

        return s;
    }
    private boolean testLogin(ByteArrayOutputStream stream){
        try {
        ByteBuffer buff =ByteBuffer.allocate(8192);
        buff.clear();
        buff.put(stream.toByteArray());
        buff.flip();

            buff =worker.send(buff);
            try (ByteArrayInputStream bais = new ByteArrayInputStream(buff.array());
                 ObjectInputStream ois = new ObjectInputStream(bais)) {
                Response res = (Response) ois.readObject();
                ois.close();
                String output = new String(new String((byte[])res.getResponse()));
                if (!output.equals("show")) {
                    System.out.println(output);
                }

            if (output.equals("Logged in")){
                return true;
            }else{
                return false;
            }
            } catch (IOException |ClassNotFoundException e) {
                return false;
            }

        } catch (IOException e) {
            return false;
        }

    }

    private ByteArrayOutputStream StartWorkWithOneArgumentCommand(String command,String argument)throws IOException{
        try {
            if (argument.equals("")) {
                System.out.println("You don't wrote the argument. Please, write it");
                return null;
            }else{
                return createRequest(command,argument,this.username+" "+this.password);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
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
        Inflammable inflam =WorkWithFiles.checkinflaminside(args);

        builder = new StringBuilder();
        if (inflam !=null) {
            return createRequest(resultsOfArg,args, username + " " + password);
        }else {
            return null;
        }
    }

    private ByteArrayOutputStream StartWorkWithOneLineCommand(String command)throws IOException{ //show, info, clear, help, remove, example
        return createRequest(command,null,username+" "+password);
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
            raw[1]="";
        }

        return raw;
    }

    public static ByteArrayOutputStream createRequest(String command, String data, String authority) throws IOException {
        SerializeTo c = new SerializeTo(command, data, authority);

        if (command.equals("import")) {
            c.setData(WorkWithFiles.generate(data));
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
