import Collection.ControlOfCollection;
import ConsoleInterface.*;
import SupportTools.WorkWithFiles;

import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;
import essence.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.CacheRequest;
import java.nio.file.AccessDeniedException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;


public class Main {


    public static void main(String[] args) {

        
        ConsoleInterface control = new ConsoleInterface();

        Scanner consolein = new Scanner(System.in);



        try{

            String s = args[0];

        }catch (ArrayIndexOutOfBoundsException e){
            String[] ss = new String[1];
            ss[0] = "file.json";
            args = ss;

            System.out.println("You don't choose the path to file, choosed standart path(example: file.json )");

        }finally {
            args[0] = WorkWithFiles.CheckForJsonInEnd(args[0]);

                control.addAutoSave(args[0]);
            try {
                ArrayList<Inflammable> raw = WorkWithFiles.ReadFile(args[0]);
                for (int i=0;i<raw.size();i++){
                    Inflammable rawinflam = raw.get(i);
                    Commands d = new Commands(control.getControlOfCollection());
                    d.add(rawinflam.getThing(),rawinflam,false);
                }

            }catch (AccessDeniedException e){
                System.out.println("Access to file denied, cant reach him");
            }catch (NoSuchElementException e){
                System.out.println("This file does not exist");
            }catch (FileNotFoundException e){
                System.out.println("We can't find file, please help us improve and fix all bugs");

            }catch (IOException e){
                System.out.println("Something Went wrong");
            }




        }
        System.out.println("For information, type help");
        System.out.print("~$ ");


        String nextString = "";

        while (!(nextString.equals("q!"))){

            try {
                nextString = consolein.nextLine();

                control.ConsoleWork(nextString);

            }catch (NoSuchElementException e) {

                Runtime.getRuntime().exit(0);

            }catch (NullPointerException e){
                System.err.println("Wrong Fromat of JSON");
            }catch (JsonSyntaxException e){
                System.out.println("Wrong Syntax for JSON");
            }
        }
    }
}