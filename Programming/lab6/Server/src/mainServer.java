import ServerWorkUDP.ServerWork;
import ServerWorkUDP.WorkWithCollection;
import Tools.DualStream;
import Tools.WorkWithFiles;

import java.io.*;
import java.util.Scanner;

public class mainServer {
    public static void main(String[] args) {

        try {
            //System.setOut(new PrintStream(System.out, true, "UTF-8"));
            File file = new File("logs.log");
            if (file.exists())
                file.delete();
            file.createNewFile();
            PrintStream err = new PrintStream(new FileOutputStream("logs.log"));
            System.setErr(err);
            PrintStream dual = new DualStream(System.out, System.err);
            System.setOut(dual);

        }catch (IOException ignored){}


        if (args.length<1){
            System.out.println("You don't putted port.\n" +
                    "For example, 25565");
            System.exit(0);
        }
        try {
            if (Integer.valueOf(args[0]) <0||Integer.valueOf(args[0])>65535){
                System.out.println("Port can be only between 0 and 65535");
                System.exit(0);
            }


            int port = Integer.valueOf(args[0]);

            ServerWork server = new ServerWork(port);
            server.setDaemon(true);


            server.SetFileName("file.json");


            server.loadCollection();
            server.start();


            Scanner in  = new Scanner(System.in);
            String nextLine;
            nextLine = in.nextLine();

            while (!nextLine.equals("exit")){
                nextLine =in.nextLine();
            }
            System.out.println("typed \"exit\" turning off...");

        }catch (NumberFormatException e){
            System.out.println("Port can be only number, try again");
            System.exit(0);
        }catch (IOException e){
            System.out.println("Wow, IOException");
            e.printStackTrace();
        }catch (NullPointerException e){
            System.out.println("Wow, file is empty");
            e.printStackTrace();
        }
    }
}
