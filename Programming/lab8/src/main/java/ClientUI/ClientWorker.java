package ClientUI;

import Tools.Response;
import client.ClientWorkWithString;
import essence.Inflammable;
import server.DataBaseConnection;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

;

public class ClientWorker {

    private InetSocketAddress address;
    private DatagramChannel udpChanel;
    private Locale locale = new Locale("ru");
    private ResourceBundle bundle = ResourceBundle.getBundle("resources.resources", locale);
             String login;
             String password;
    private int port;
    private String host;
    private boolean isSetttingPort;
    private PortUI portUI;
    private Registration registration;
    private SendEmail sendEmail;
    private ArrayList<Inflammable> list;
    private WorkingPlate plate;

    public String getLogAndPass(){
        return this.login+" "+this.password;
    }


    public ClientWorker() throws IOException {
        this.udpChanel = DatagramChannel.open();
        startPortWork();
    }

    public ResourceBundle getResource() {
        return bundle;
    }

    public void StartRegistration() {
        registration.dispose();
        sendEmail = new SendEmail(this);


    }
    public void Rethink(){
        sendEmail.dispose();
        registration = new Registration(this);
    }
    public void SendEmail(String nickname,String email){
        sendEmail.dispose();
        Response s=null;
        try {
            s=SendAndReceive("register",null,nickname+" "+email+" "+ DataBaseConnection.getPass());
        } catch (IOException e) {
            sendEmail = new SendEmail(this,"ErrorWhileEmail");
        }
        if (s==null){
            //sendEmail = new SendEmail(this,"ErrorWhileEmail");
            registration=new Registration(this);
        }
        String result=new String( (byte[])s.getResponse());
        if (!result.equals("")&&result.equals("Email was registered")){
            registration = new Registration(this);
        }else if (result.equals("This Email is already registered")){
            sendEmail = new SendEmail(this,"ThisPersonAlreadyExist");
        }else {
            sendEmail = new SendEmail(this,"ErrorWhileEmail");
        }
    }
    public void Import(File file){
        
        String s=file.getAbsolutePath();
        Response ss=null;
        try {
            Sender("import", s, this.login + " " + this.password);
        }catch (IOException e){
            JFrame sd= new JFrame();
            sd.setVisible(true);
            JOptionPane.showMessageDialog(sd,
                    new java.lang.String[] {this.getResource().getString("SomeError")},
                    this.getResource().getString("ErrorTitle"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void Login(String nickname,String password){
            registration.dispose();
        try {
            Response asd=SendAndReceive("login",null,nickname+" "+DataBaseConnection.encryptString(password));
            String result=new String( (byte[])asd.getResponse());
            if (!result.equals("")&&result.equals("Logged in")){
                this.login=nickname;
                this.password=DataBaseConnection.encryptString(password);
                System.out.println("We are in");
                startPlate();
            }else {
                registration = new Registration(this,"WrongLoginOrPassword");
            }
        } catch (IOException e) {
            registration=new Registration(this,"SomeErrorLogin");
        }

    }
    private void startPlate(){
        this.plate=new WorkingPlate(this);
                ByteBuffer buffer = ByteBuffer.allocate(8192);
                buffer.clear();
                try {
                    udpChanel.receive(buffer);
                    System.out.println("We get something, yay");
                } catch (IOException e) {
                    System.out.println("Some error occurred");
                }Response res=null;
                try (ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array());
                     ObjectInputStream ois = new ObjectInputStream(bais)) {
                    res = (Response) ois.readObject();
                    System.out.println(res.getCommand());


                }catch (Exception ignored){

                }
                if (res!=null && res.getCommand().equals("Changes")){
                    plate.initiate((ConcurrentHashMap<Integer, Inflammable>) res.getResponse());
                }else {
                    System.out.println("We recieved something, but we can't recognize it");
                    System.out.println(res.getCommand());
                    System.out.println(res.getResponse());
                }

    }
    public void receive(){
        while (true){
            try {
                ByteBuffer buffer = ByteBuffer.wrap(new byte[8192]);
                udpChanel.receive(buffer);
                try (ObjectInputStream receivedstream = new ObjectInputStream(new ByteArrayInputStream(buffer.array()))) {
                    Response response = (Response) receivedstream.readObject();
                    System.out.println(response.getCommand()+" -command");
                    //System.out.println(new String((byte[])response.getResponse())+" -response");
                    switch (response.getCommand()){
                        case "Changes":
                            System.out.println("we changed collection");
                            plate.initiate((ConcurrentHashMap<Integer, Inflammable>) response.getResponse());
                        break;

                        case "AddInfammables":
                            new Thread(()->{
                                plate.addInflams((ConcurrentHashMap<Integer, Inflammable>)response.getResponse());
                            }).start();
                        break;

                        case "RemoveInflammable":
                            new Thread(()->{
                                plate.removeInflam((Integer) response.getResponse());
                            }).start();
                        break;

                        case "RemoveInflammables":
                            new Thread(()->{
                                plate.removeInflams((ArrayList<Integer>) response.getResponse());
                            }).start();
                        break;

                        case "change":
                            plate.ChangeInflam((Inflammable)response.getResponse());

                        break;


                        case "Info":

                        case "Error":
                            plate.ShowMessage(this.getResource().getString(new String((byte[]) response.getResponse())),this.getResource().getString("ErrorTitle"));
                        break;

                        default:
                            System.out.println("WTF");
                            System.out.println(response.getCommand());
                            System.out.println(response.getResponse());
                            break;


                    }
                }catch (Exception ignored){

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void endPortWork(String host, int port) {
        if (isSetttingPort) {
            portUI.dispose();
            isSetttingPort = false;
        }

        this.host = host;
        this.port = port;

        address = new InetSocketAddress(this.host, this.port);
        try {
            boolean isconnected =isConnected();

        if (isconnected){
            registration = new Registration(this);
        }else {
            portUI = new PortUI(this,"WrongPortAndHost");
        }
        } catch (IOException e) {
            portUI = new PortUI(this,"WrongPortAndHost");
        }

    }

    private void startPortWork() {
        isSetttingPort = true;
        portUI = new PortUI(this);
    }

    public int getLocale() {
        switch (locale.getLanguage()) {
            case "en":
                return 1;
            case "uk":
                return 2;
            case "jp":
                return 3;
            default:
                return 0;
        }
    }

    public void setLocale(String lang) {
        this.locale = new Locale(lang);
        bundle = ResourceBundle.getBundle("resources.resources", this.locale);
    }
    public void setLocaleAnd(String lang,String J) {
        this.locale = new Locale(lang,J);
        bundle = ResourceBundle.getBundle("resources.resources", this.locale);
    }

    public Response SendAndReceive(String command,String data,String authority) throws IOException {
        ByteArrayOutputStream request = ClientWorkWithString.createRequest(command,data,authority);
        ByteBuffer buff = ByteBuffer.allocate(8192);
        buff.clear();
        buff.put(request.toByteArray());
        buff.flip();

        ByteBuffer buffer = send(buff);

        try (ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array());
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            Response res= (Response) ois.readObject();


        return res;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public void Sender(String command,Object data,String authority)throws IOException{
        ByteArrayOutputStream request = ClientWorkWithString.createRequest(command,data,authority);
        ByteBuffer buff = ByteBuffer.allocate(8192);
        buff.clear();
        buff.put(request.toByteArray());
        buff.flip();
        this.udpChanel.send(buff,address);


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


    public boolean isConnected() throws IOException{
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
                connectString = new String((byte[]) Res.getResponse());
                System.out.println( Res.getResponse());
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
            return true;


        }else {
            return false;
        }
    }


}
