package client;

import Tools.Response;
import essence.Inflammable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;

public class ClientWorker {
    private InetSocketAddress address;
    private DatagramChannel udpChanel;
//    private String username;
//    private String password;
    private boolean isAuth = false;
    String command="";

    public ClientWorker(String add,int port)throws IOException {
        this.address = new InetSocketAddress(add,port);
        this.udpChanel = DatagramChannel.open();
    }


    public void connect()throws IOException{
        ClientWorkWithString control = new ClientWorkWithString();
        isConnected();
        System.out.println("For information, type help");
        System.out.print("~$ ");



        boolean nextString=false;

        ByteArrayOutputStream request;

        while (!nextString) {

            request =control.startwork();
            nextString = control.isExit;
            command=control.CommandToSend;
            if (request !=null){
                ByteBuffer buff =ByteBuffer.allocate(8192);
                buff.clear();
                buff.put(request.toByteArray());
                buff.flip();

                ByteBuffer buffer  = send(buff);


                try (ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array());
                     ObjectInputStream ois = new ObjectInputStream(bais)) {
                    Response res = (Response) ois.readObject();
                    ois.close();
                    String output = new String(decodeResponse(command,res));
                    if (!output.equals("show")) {
                        System.out.println(output);
                    }
                } catch (IOException e) {
                    System.out.println("Потеря потока данных");
                    // e.printStackTrace();
                } catch (ClassNotFoundException ignored) {

                }

                System.out.print("~$");

            }else if (control.CommandisEnded){
                //System.out.println("Wrong command");
                System.out.print("~$ ");
            }

        }

    }
    private ByteBuffer send(ByteBuffer buff)throws IOException{
        this.udpChanel.send(buff,address);

        ByteBuffer buffer = ByteBuffer.allocate(8192);
        buffer.clear();

        Thread current = Thread.currentThread();
        Thread s =new Thread(()->{try {
            Thread.sleep(3000);
            current.interrupt();
        }catch (InterruptedException ignore ){}});
        s.start();
        try {
            udpChanel.receive(buffer);
        }catch (ClosedByInterruptException e){

            System.out.println("Disconnected from host...");
            isConnected();

        }
        s.interrupt();
        return buffer;
    }

    public void isConnected() throws IOException{
        System.out.println("Trying to reach a remote host...");
        ByteArrayOutputStream testRequest = ClientWorkWithString.createRequest("connecting", null,null);
        ByteBuffer buffer = ByteBuffer.allocate(8192);
        buffer.clear();
        buffer.put(testRequest.toByteArray());
        buffer.flip();

        String connectString = "";


        udpChanel.socket().setSoTimeout(1000);

        for (int i = 1; i <= 10; i++) {
//
            System.out.println("\t* Trying to connect..");
            try {
                if (!udpChanel.isOpen()){
                    udpChanel = DatagramChannel.open();
                    udpChanel.socket().setSoTimeout(1000);
                }
                udpChanel.send(buffer,address);
            } catch (ClosedChannelException e) {

            }
            buffer.clear();

            Thread currentThread = Thread.currentThread();
            Thread s =new Thread(()->{try {
                Thread.sleep(3000);
                currentThread.interrupt();
            }catch (InterruptedException ignore ){}});
            s.start();
            try {
                udpChanel.receive(buffer);
            }catch (ClosedByInterruptException e){
                connectString = "Not connected";
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignore) {

                }
                continue;
            }catch (IOException e){
                //e.printStackTrace();
            }
            s.interrupt();

            try (ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array());
                 ObjectInputStream ois = new ObjectInputStream(bais)) {
                Response Res = (Response) ois.readObject();
                connectString = new String(decodeResponse("connection",Res));
            } catch (Exception e) {
                connectString = "Not connected";
                try {

                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    //ex.printStackTrace();
                }
            }
            if (connectString.equals("connected")){
                break;
            }
        }
        if (connectString.equals("connected")){
            System.out.println("Connection with the server is established");


        }else {
            System.err.println("Server is unreachable at this moment");
            System.exit(1);
        }
    }

    private byte[] decodeResponse(String command, Response response) {
        if (command.equals("show")) {
            try (ByteArrayInputStream bais = new ByteArrayInputStream((byte[])response.getResponse());
                 ObjectInputStream ois = new ObjectInputStream(bais)) {
                ArrayList<Inflammable> storage = (ArrayList<Inflammable>) ois.readObject();
                int i=1;
                for (Inflammable in : storage) {
                    System.out.println("----------------------------------------------------------------");
                    System.out.println("Inflammable № "+i);
                    System.out.println("Key: " + in.getThing() + "\n" +
                            "burningpower: " + in.getBurningPower() + "\n" +
                            "size: " + in.getSize() + "\n" +
                            "name: " + in.getName() + "\n" +
                            "owner: " + in.getOwner());
                    System.out.println("----------------------------------------------------------------");
                    i++;

                }
            } catch (IOException | ClassNotFoundException e) {
                //e.printStackTrace();
            }}else if (response.getResponse().equals("Email was registered")){
            setIsAuth(true);
            return ("On your Email we sent password for your account.\n" +
                    "When it arrives, type: login").getBytes();
        } else if (command.equals("login")) {
            if ((new String((byte[])response.getResponse())).equals("Logged in")){
                setIsAuth(true);
                return "Logged in".getBytes();}
            else {return (byte[])response.getResponse();}
        } else {
            return (byte[])response.getResponse();
        }
        return "show".getBytes();
    }

    public void setIsAuth(boolean isAuth) {
        this.isAuth = isAuth;
    }


}
