package client;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Client {
    public static void main(String[] args) {
        /*try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        }catch (UnsupportedEncodingException ignored){}*/

        if (args.length<1){
            System.out.println("You don't putted needed arguments,\n" +
                    "For example, localhost:25565");
            System.exit(0);
        }

        ByteBuffer f = ByteBuffer.allocate(8192);
        f.clear();
        try {
            f.put(ClientWorkWithString.createRequest("show",null," ").toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        f.flip();


        try {
            ClientWorker client = new ClientWorker( args[0].split(":")[0], Integer.valueOf(args[0].split(":")[1]));
            //client.send(f);
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
