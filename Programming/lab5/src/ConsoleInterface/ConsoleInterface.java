package ConsoleInterface;

import Collection.ControlOfCollection;
import SupportTools.WorkWithFiles;
import essence.Inflammable;
import essence.Thing;
import org.json.JSONException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;

public class ConsoleInterface {
    private ControlOfCollection control = new ControlOfCollection();

    private HashSet<String> CommandsWithoutManyLinesIn = new HashSet<>();
    private HashSet<String> CommandsWithManyLinesIn = new HashSet<>();
    private HashSet<String> CommandWithOneArgument = new HashSet<>();

    private boolean CommandisEnded = true;
    private boolean IsBracketOpened;

    private short LeaveCounter = 1;
    private short Leave = 5;
    private byte BracketsCounter =0;

    private String resultsOfArg;

    private Commands cmd = new Commands(control);

    private StringBuilder builder = new StringBuilder();

    private StringBuilder telemtry = new StringBuilder();


    public ConsoleInterface(){
        CommandsWithoutManyLinesIn.add("show");
        CommandsWithoutManyLinesIn.add("info");
        CommandsWithoutManyLinesIn.add("clear");
        CommandsWithoutManyLinesIn.add("help");
        CommandsWithoutManyLinesIn.add("example");
        CommandsWithoutManyLinesIn.add("ce");

        CommandWithOneArgument.add("remove");

        CommandsWithManyLinesIn.add("remove_greater");
        CommandsWithManyLinesIn.add("insert");
        CommandsWithManyLinesIn.add("add_if_max");
    }

    public void ConsoleWork(String command){
        telemtry.append(command+"\n");
        if (command!=null){
            command = DeleteLastSpaces(command);

            if(CommandsWithoutManyLinesIn.contains(command)) {
                StartWorkwithOneLinecommand(command);
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
                        StartWorkWithOneArgumentCommand(rawcouple[0],rawcouple[1]);
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
        if (CommandisEnded)
            System.out.print("~$ ");

    }

    public ControlOfCollection getControlOfCollection(){
        return control;
    }

    private void StartWorkWithOneArgumentCommand(String command,String argument){
        switch (command){
            case "remove":
                if (argument.equals(""))
                    System.out.println("You don't wrote the argument. Please, write it");
                else cmd.remove(argument);
                break;
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

            throw new JSONException("Before closing bracket you must put a opening bracket");
        }

        if (LeaveCounter >= Leave){
            CommandisEnded = true;

            StartWorkWithMultilineCommand();
        }

        if (BracketsCounter == 0) {
            if (IsBracketOpened) {
                CommandisEnded = true;

                StartWorkWithMultilineCommand();
            }
            else {
                LeaveCounter++;
            }

        }
    }

    private void StartWorkWithMultilineCommand(){
        String args = builder.toString();

        Inflammable inflam;

        for (int i=0;i<args.length();i++){
            if (args.charAt(i) =='{'){
                args = args.substring(i);

                break;
            }
        }

        try {

            inflam = WorkWithFiles.toEssence(args);

            if(resultsOfArg.equals("add_if_max"))
                cmd.AddIfMax(inflam);
            else if (resultsOfArg.equals("insert")){
                cmd.add(inflam.getThing(),inflam,true);
            }
        } catch (NullPointerException e) {
            System.err.println("Wrong type of JSON");
        }finally {
            builder = new StringBuilder();
        }
    }

    private void StartWorkwithOneLinecommand(String command){ //show, info, clear, help, remove, example
        switch (command){
            case "show":
                cmd.show();
                break;
            case "info":
                cmd.info();
                break;
            case "clear":
                cmd.clear();
                break;
            case "help":
                cmd.help();
                break;
            case "example":
                cmd.example();
                break;
            case "ce":
                cmd.CreateExample();
        }
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

    public void addAutoSave(String path){
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                File file = new File(path);

                if (file.exists()) {
                    file.delete();
                }

                try {
                    String raw = WorkWithFiles.TransformToJSON(control.GetCollection());
                    WorkWithFiles.WriteInFile(raw,path);
                    File file1 = new File("telemtry.json");
                    try (FileWriter writer = new FileWriter(file1, false))
                    {
                        writer.write(telemtry.toString());
                    } catch (IOException ex){
                        ex.printStackTrace();
                    }
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    System.err.println("Something went wrong");
                }
            }
        });

    }

}