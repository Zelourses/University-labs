package server;

import essence.Inflammable;
import essence.Thing;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;


public class DataBaseConnection {
    private String url = "jdbc:postgresql://localhost:5432/lab7";
    private String name = "iljab";
    private String pass = "iljab";
    private Connection connection = null;
    private Listener command;

    {
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Installed Driver");
            connection = DriverManager.getConnection(url, name, pass);
            System.out.println("The Connection is successfully established\n");
            command = new Listener();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Can't connect to the database");
        }
    }



    public void clear(String username){
        try {
            PreparedStatement st = connection.prepareStatement("SELECT thing_id FROM \"Inflammables\" where username=?;");
            st.setString(1,username);
            ResultSet res = st.executeQuery();
            while (res.next()){
                PreparedStatement deleting = connection.prepareStatement("delete from Thing WHERE id=?");
                deleting.setInt(1,res.getInt("thing_id"));
                deleting.executeUpdate();
                PreparedStatement delete = connection.prepareStatement("delete from \"Inflammables\" where username=?");
                delete.setString(1,username);
                delete.executeUpdate();
            }
            st = connection.prepareStatement("delete from \"Inflammables\" where username=?");
            st.setString(1,username);
            st.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("something went wrong when we are tried to delete all");
        }
    }

    public Inflammable ChangeInflam(Inflammable inflammable){
        try {
            PreparedStatement st = connection.prepareStatement("select * from \"Inflammables\" where id=?");
            st.setInt(1,inflammable.getId());
            ResultSet res = st.executeQuery();
            int thingname=0;
            int i=0;
            while (res.next()){
                if (!res.getString("username").equals(inflammable.getOwner())){
                    return null;
                }
                thingname=res.getInt("thing_id");

                i++;
            }
            if (i==0)return null;
            else {
                //int ThingId=res.getInt("thing_id");
                PreparedStatement thingstate=connection.prepareStatement("update thing set name=? where id=?");
                thingstate.setString(1,inflammable.getThing());
                thingstate.setInt(2,thingname);
                thingstate.executeUpdate();
                PreparedStatement statement = connection.prepareStatement("update \"Inflammables\" set name=?,burningpower=?" +
                        ",size=?,sizeui=? where id=?");
                statement.setString(1,inflammable.getName());
                statement.setFloat(2,inflammable.getBurningPower());
                statement.setFloat(3,inflammable.getSize());
                statement.setInt(4,inflammable.getSizeUI());
                statement.setInt(5,inflammable.getId());
                statement.executeUpdate();
                return inflammable;
            }

        } catch (SQLException e) {

            System.out.println("Some error while changing");
            e.printStackTrace();
            return null;
        }
    }

    public HashMap<Integer,Inflammable> getInflammable(float burningpower,float size,String username,String ThingName,int sizeui,String name){
        HashMap<Integer,Inflammable> one=new HashMap<>();
        try {
            PreparedStatement st = connection.prepareStatement("select * from \"Inflammables\" where username=? and size=? " +
                    "and burningpower=? and sizeui=? and name=?");
            st.setString(1,username);
            st.setFloat(2,size);
            st.setFloat(3,burningpower);
            st.setInt(4,sizeui);
            st.setString(5,name);
            ResultSet result=st.executeQuery();
            if (result!=null){
                result.next();
                int id = result.getInt("id");
                String UserName = result.getString("username");
                String Name = result.getString("name");
                float Burningpower = result.getFloat("burningpower");
                float Size = result.getFloat("size");
                int thingid = result.getInt("thing_id");
                int Sizeui=result.getInt("sizeui");
                String date = result.getString("date");
                Inflammable inflam = new Inflammable(Name,Burningpower,Size,id);
                inflam.setOffsettime(OffsetDateTime.parse(date));
                inflam.setOwner(UserName);
                inflam.setSizeUI(sizeui);
                inflam.setSizeUI(Sizeui);
                PreparedStatement  getThing = connection.prepareStatement("SELECT * FROM thing WHERE id=?");
                getThing.setInt(1,thingid);
                ResultSet resThing = getThing.executeQuery();
                if (resThing.next()) {
                    inflam.setThing(new Thing(resThing.getString("name")));
                }
                one.put(id,inflam);

                return one;

            }

            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }


    }

    public int loadEssences(ConcurrentHashMap<String, Inflammable> map) {
        try {
            int i = 0;
            String time = new java.util.Date().toString();
            PreparedStatement st = connection.prepareStatement("SELECT * FROM \"Inflammables\";");
            PreparedStatement getThing;
            ResultSet result = st.executeQuery();
            while (result.next()) {
                int id = result.getInt("id");
                String username = result.getString("username");
                String name = result.getString("name");
                float burningpower = result.getFloat("burningpower");
                float size = result.getFloat("size");
                int thingid = result.getInt("thing_id");
                int sizeui=result.getInt("sizeui");
                String date = result.getString("date");
                if (date != null) {
                    time = result.getString("date");

                }
                Inflammable inflam = new Inflammable(name,burningpower,size,id);
                inflam.setOffsettime(OffsetDateTime.parse(time));
                inflam.setOwner(username);
                inflam.setSizeUI(sizeui);

                getThing = connection.prepareStatement("SELECT * FROM thing WHERE id=?");
                getThing.setInt(1,thingid);
                ResultSet resThing = getThing.executeQuery();
                if (resThing.next()) {
                    inflam.setThing(new Thing(resThing.getString("name")));
                }
                map.put(inflam.getThing(),inflam);
                i++;
            }
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Wow, some error.");
            return -1;
        }
    }

    public boolean AddtoDatabase(Inflammable in, String username) {
        try {
            return addInflammable(in, username);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("wow, some error");
            return false;
        }
    }

    private boolean addInflammable(Inflammable inflam, String username) {
        try {
            int thingid = ZonedDateTime.now().getNano();
            PreparedStatement st = connection.prepareStatement("INSERT INTO \"Inflammables\" (name, burningpower, size, username, date, thing_id, sizeui)VALUES (?, ?, ?, ?, ?, ?,?);");
            //st.setLong(1,inflam.getThing().hashCode());
            st.setString(1,inflam.getName());
            st.setFloat(2,inflam.getBurningPower());
            st.setFloat(3,inflam.getSize());
            st.setString(4,username);
            st.setString(5,OffsetDateTime.now().toString());
            st.setInt(6,thingid);
            st.setInt(7,inflam.getSizeUI()>0?inflam.getSizeUI():1);
            st.executeUpdate();
            if (inflam.getThing() != null) {
                PreparedStatement Thingst = connection.prepareStatement("INSERT INTO \"thing\" VALUES (?, ?);");
                Thingst.setInt(1, thingid);
                Thingst.setString(2, inflam.getThing());
                Thingst.executeUpdate();
            }
        } catch (SQLException|NullPointerException e) {
            return false;
        }
        return true;
    }
    public ConcurrentHashMap<String,Inflammable> reload()throws SQLException{
        ConcurrentHashMap<String,Inflammable> map=new ConcurrentHashMap<>();
        String time = new java.util.Date().toString();
        PreparedStatement st = connection.prepareStatement("SELECT * FROM \"Inflammables\";");
        PreparedStatement getThing;
        ResultSet result = st.executeQuery();
        while (result.next()) {
            int id = result.getInt("id");
            String username = result.getString("username");
            String name = result.getString("name");
            float burningpower = result.getFloat("burningpower");
            float size = result.getFloat("size");
            int thingid = result.getInt("thing_id");
            String date = result.getString("date");
            if (date != null) {
                time = result.getString("date");
            }
            Inflammable inflam = new Inflammable(name,burningpower,size,id);
            inflam.setOffsettime(OffsetDateTime.parse(time));
            inflam.setOwner(username);

            getThing = connection.prepareStatement("SELECT * FROM thing WHERE id=?");
            getThing.setInt(1,thingid);
            ResultSet resThing = getThing.executeQuery();
            if (resThing.next()) {
                inflam.setThing(new Thing(resThing.getString("name")));
            }
            map.put(inflam.getThing(),inflam);
        }
        return map;
    }
    public ConcurrentHashMap<Integer,Inflammable> reload2(){
        try {
            ConcurrentHashMap<Integer,Inflammable> map=new ConcurrentHashMap<>();
            String time = new java.util.Date().toString();
            PreparedStatement st = connection.prepareStatement("SELECT * FROM \"Inflammables\";");
            PreparedStatement getThing;
            ResultSet result = st.executeQuery();
            while (result.next()) {
                Integer id = result.getInt("id");
                String username = result.getString("username");
                String name = result.getString("name");
                float burningpower = result.getFloat("burningpower");
                float size = result.getFloat("size");
                int thingid = result.getInt("thing_id");
                int sizeUI = result.getInt("sizeui");
                String date = result.getString("date");
                if (date != null) {
                    time = result.getString("date");
                }
                Inflammable inflam = new Inflammable(name,burningpower,size,id);
                inflam.setOffsettime(OffsetDateTime.parse(time));
                inflam.setOwner(username);
                inflam.setSizeUI(sizeUI);

                getThing = connection.prepareStatement("SELECT * FROM thing WHERE id=?");
                getThing.setInt(1,thingid);
                ResultSet resThing = getThing.executeQuery();
                if (resThing.next()) {
                    inflam.setThing(new Thing(resThing.getString("name")));
                }
                map.put(id,inflam);
            }
            return map;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void savEssence(ConcurrentHashMap<String,Inflammable>  map) {
        try {
            if (map != null) {
                Iterator<Inflammable> iterator = map.values().iterator();

                while (iterator.hasNext()) {
                    Inflammable in = iterator.next();
                    addInflammable(in, in.getOwner());
                }
                System.out.println("All data was load");
            } else {
                System.out.println("Collection is empty");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong");
        }
    }

    public int executeLogin(String login, String pass) {
        try {
            PreparedStatement preStatement = connection.prepareStatement("SELECT * FROM users WHERE username=? and password=?;");
            preStatement.setString(1,login);
            preStatement.setString(2,pass);
            ResultSet result = preStatement.executeQuery();
            if (result.next()) return 0;
            else {
                PreparedStatement preStatement2 = connection.prepareStatement("SELECT * FROM users WHERE username=?;");
                preStatement2.setString(1,login);
                ResultSet result2 = preStatement2.executeQuery();
                if (result2.next()) return 2;
                else return 1;
            }
        } catch (Exception e) {
            System.out.println("Some error, wow");
            return -1;
        }
    }


    public int removeInflammable(String username, Inflammable inflam) {
        try {
            PreparedStatement pr =connection.prepareStatement("SELECT thing_id,id from \"Inflammables\" where name=? and burningpower=?" +
                    "and size=? and username=?");

            pr.setString(1,inflam.getName());
            pr.setFloat(2, inflam.getBurningPower());
            pr.setFloat(3,inflam.getSize());
            pr.setString(4,username);

            ResultSet rawset = pr.executeQuery();

            if (rawset.next()){
                int thing_id = rawset.getInt("thing_id");
                int id = rawset.getInt("id");

                pr = connection.prepareStatement("delete from \"Inflammables\" where id=?");
                pr.setInt(1,id);

                pr.executeUpdate();

                pr = connection.prepareStatement("DELETE from thing where id=?");
                pr.setInt(1,thing_id);

                pr.executeUpdate();

                pr.close();

                return id;
            }else {
                return 0;
            }


        }catch (Exception e) {
            return 0;
        }
    }


    public int executeRegister(String login, String mail, String pass) {
        try {

            PreparedStatement ifLog = connection.prepareStatement("SELECT * FROM users WHERE username=?;");
            ifLog.setString(1,login);
            ResultSet result = ifLog.executeQuery();
            if (result.next()){return 0;}
            PreparedStatement iflog2 = connection.prepareStatement("select * from users where mail=?");
            iflog2.setString(1,mail);
            ResultSet result2 = iflog2.executeQuery();
            if (result2.next()){return 0;}
            String hash = DataBaseConnection.encryptString(pass);
            int i= JavaMail.registration(mail,pass);

            if (i==1) {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO users VALUES (?, ?, ?);");
                statement.setString(1, login);
                statement.setString(2, mail);
                statement.setString(3, hash);
                statement.executeUpdate();
                //new Thread(() -> JavaMail.registration(mail, pass)).start();
                return 1;
            } else {
                System.out.println("Some problems with email");
                return -2;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error whilst registration");
            return -1;
        }
    }


    public static String getPass() {
        try {
            Random r = new Random();
            char[] password = new char[7];

            for (int i = 0; i < 7; i++) {

                int nextChar = (int) (Math.random() * 62);

                if (nextChar < 10) {
                    int temp = r.nextInt(37);
                    temp+=30;
                    nextChar += temp;
                }
                else if (nextChar < 36) {
                    int temp = r.nextInt(60);
                    temp+=50;
                    nextChar += temp;
                }
                else
                    nextChar += 61;

                password[i] = (char) nextChar;
            }
            return new String(password);

        } catch (Exception e) {
            return null;
        }
    }

    public static String encryptString(String input)
    {
        try {
            // getInstance() method is called with algorithm MD2
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into sign representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            StringBuilder hashText = new StringBuilder(no.toString(32));


            // return the HashText
            return hashText.toString();
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            System.out.println("This encrypt does not exist");
            e.getMessage();
            return null;
        }
    }

}