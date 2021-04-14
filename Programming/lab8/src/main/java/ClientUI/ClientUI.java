package ClientUI;

import java.io.IOException;

public class ClientUI {
    public static void main(String[] args) {
        //now, work
        try {
            new ClientWorker();
        } catch (IOException e) {
            System.out.println("Some exception, while we are tried to work in ClientWorker,\n" +
                    "Here it is: "+e.getMessage());
        }
    }
}
