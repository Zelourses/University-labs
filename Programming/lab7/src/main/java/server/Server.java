package server;

import Tools.WorkWithFiles;

public class Server{


    public static void ExitMessage() {
        System.out.println("To run server, write port, like: 25565");
        System.exit(1);
    }

    public static void main(String[] args) throws Exception {
        int input_port = -1;

        if (args.length == 0) {
            ExitMessage();
        }

        try {
            input_port = Integer.parseInt(args[0]);
        } catch (IllegalArgumentException e) {
            ExitMessage();
        }

        ServerWork server = new ServerWork(input_port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                WorkWithFiles.ShutDown(server.storage,server.filename);
                System.out.println("Server data saved");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Something went wrong");
            }
        }));

        try {
            server.listen();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}