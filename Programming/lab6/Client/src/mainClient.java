import ClientWorkUDP.ClientWorker;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class mainClient {
    public static void main(String[] args) {
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        }catch (UnsupportedEncodingException ignored){}

        if (args.length<1){
            System.out.println("You don't putted needed arguments,\n" +
                    "For example, localhost:25565");
            System.exit(0);
        }

        try {
            ClientWorker client = new ClientWorker( args[0].split(":")[0], Integer.valueOf(args[0].split(":")[1]));
            client.connect();

        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("You don't putted needed arguments.\n" +
                    "For example, localhost:25565");
            System.exit(0);
        }catch (IOException e){
            System.out.println("Wow, IOException");
        }

    }
}
