package ClientWorkUDP;

import Tools.ClientAnswers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.DatagramChannel;
import java.util.Scanner;

public class ClientWorker{
    private InetSocketAddress address;
    private DatagramChannel udpChanel;
    private DatagramSocket udpChanelSocket;
    private Scanner scanner;

    public ClientWorker(String Address, int port)throws IOException {
        this.address = new InetSocketAddress(Address,port);
        this.udpChanel = DatagramChannel.open();
       // udpChanel.configureBlocking(false); //это говно отключает нормальную работу isConnected
        this.udpChanelSocket = udpChanel.socket();

    }

    public void connect(){
        try {
            ClientWorkWithString control = new ClientWorkWithString();
            IsConnected();

            System.out.println("For information, type help");
            System.out.print("~$ ");

            String nextString = "";
            Scanner consolein = new Scanner(System.in);
            ByteArrayOutputStream request;
            boolean isinfocutted = false;

            while (!(nextString.equals("q!"))) {
                nextString = consolein.nextLine();

                request =control.ConsoleWork(nextString);
                if (request !=null){
                    ByteBuffer buff =ByteBuffer.allocate(8192);
                    buff.clear();
                    buff.put(request.toByteArray());
                    buff.flip();
                    this.udpChanel.send(buff,address);

                    ByteBuffer buffer = ByteBuffer.allocate(8192);
                    buffer.clear();
                    try {
                        this.udpChanel.socket().receive(new DatagramPacket(buffer.array(), buffer.array().length));
                    } catch (SocketTimeoutException e) {
                        System.err.println("Disconnected from host");
                        isinfocutted = true;
                        IsConnected();


                    }
                    if (!isinfocutted) {
                        try (ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array());
                             ObjectInputStream ois = new ObjectInputStream(bais)) {
                            ClientAnswers clientanswer = (ClientAnswers) ois.readObject();
                            ois.close();
                            String output = new String((byte[]) clientanswer.getClientAnswer());
                            if (!output.equals("show")) {
                                System.out.println(output);
                            }
                        } catch (IOException e) {
                            System.out.println("Потеря потока данных");
                            e.printStackTrace();
                        } catch (ClassNotFoundException ignored) {

                        }
                    }else
                        System.out.println("We reconnected us!");

                    System.out.print("~$");

                }else if (control.CommandisEnded){
                    System.out.println("Wrong command");
                    System.out.print("~$");
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void IsConnected ()throws IOException{

        System.out.println("Trying to reach a remote host...");
        ByteArrayOutputStream testRequest = ClientWorkWithString.createRequest("connecting", "");
        ByteBuffer buffer = ByteBuffer.allocate(8192);
        buffer.clear();
        buffer.put(testRequest.toByteArray());
        buffer.flip();

        String connectString = "";


        udpChanel.socket().setSoTimeout(1000);

        for (int i = 1; i <= 10; i++) {
            System.out.println("\t* Trying to connect.." +i);
            udpChanel.send(buffer,address);

            buffer.clear();
            try {

                this.udpChanel.socket().receive(new DatagramPacket(buffer.array(), buffer.array().length));
            }
            catch (Exception e){
                continue;
            }

            try (ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array());
                 ObjectInputStream ois = new ObjectInputStream(bais)) {
                ClientAnswers clientAnswers = (ClientAnswers) ois.readObject();
                connectString = new String((byte[]) clientAnswers.getClientAnswer());
            } catch (Exception e) {
                connectString = "Not connected";
                try {

                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
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

    public void ThrowSomethingInServer(byte[] data){
        try {
            Thread mainThread = Thread.currentThread();
            DatagramChannel channel = DatagramChannel.open();
            Runnable receivingRunnable = () -> {
                try {
                    channel.receive(ByteBuffer.allocate(8192));

                    mainThread.interrupt();
                } catch (IOException ignored) {}
            };

                Thread receivingThread = new Thread(receivingRunnable);
                receivingThread.setDaemon(true);
                DatagramPacket packet = new DatagramPacket(data,data.length);

                channel.send(ByteBuffer.wrap(packet.getData()), address);

                receivingThread.start();

//                receivingThread = new Thread(receivingRunnable);
//                receivingThread.start();


                receivingThread.interrupt();

        } catch (Exception e) {
//            listener.onError("Ошибка отправки запроса: " + e.getMessage());
            e.printStackTrace();
        }
    }


}