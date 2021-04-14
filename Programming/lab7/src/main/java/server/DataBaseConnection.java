package server;

import essence.Inflammable;
import essence.Thing;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
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

    public int loadEssences(ConcurrentHashMap<String, Inflammable> map) {
        try {
            int i = 0;
            //TODO сделать множественные варианты
            String time = new java.util.Date().toString();
            PreparedStatement st = connection.prepareStatement("SELECT * FROM \"Inflammables\";");
            PreparedStatement getThing;
            ResultSet result = st.executeQuery();
            while (result.next()) {
                String username = result.getString("username");
                String name = result.getString("name");
                float burningpower = result.getFloat("burningpower");
                float size = result.getFloat("size");
                int thingid = result.getInt("thing_id");
                String date = result.getString("date");
                if (date != null) {
                    time = result.getString("date");


                }
                Inflammable inflam = new Inflammable(name,burningpower,size);
                inflam.setOffsettime(OffsetDateTime.parse(time));
                inflam.setOwner(username);

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

    public void AddtoDatabase(Inflammable in, String username) {
        try {
            addInflammable(in, username);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("wow, some error");
        }
    }

    private void addInflammable(Inflammable inflam, String username) throws SQLException {
        int thingid = ZonedDateTime.now().getNano();
        PreparedStatement st = connection.prepareStatement("INSERT INTO \"Inflammables\" VALUES (?, ?, ?, ?, ?, ?,?);");
        st.setLong(1,inflam.getThing().hashCode());
        st.setString(2,inflam.getName());
        st.setFloat(3,inflam.getBurningPower());
        st.setFloat(4,inflam.getSize());
        st.setString(5,username);
        st.setString(6,OffsetDateTime.now().toString());
        st.setInt(7,thingid);
        st.executeUpdate();
        if (inflam.getThing() != null) {
            PreparedStatement Thingst = connection.prepareStatement("INSERT INTO \"thing\" VALUES (?, ?);");
            Thingst.setInt(1, thingid);
            Thingst.setString(2, inflam.getThing());
            Thingst.executeUpdate();
        }
    }
    public ConcurrentHashMap<String,Inflammable> reload()throws SQLException{
        ConcurrentHashMap<String,Inflammable> map=new ConcurrentHashMap<>();
        String time = new java.util.Date().toString();
        PreparedStatement st = connection.prepareStatement("SELECT * FROM \"Inflammables\";");
        PreparedStatement getThing;
        ResultSet result = st.executeQuery();
        while (result.next()) {
            String username = result.getString("username");
            String name = result.getString("name");
            float burningpower = result.getFloat("burningpower");
            float size = result.getFloat("size");
            int thingid = result.getInt("thing_id");
            String date = result.getString("date");
            if (date != null) {
                time = result.getString("date");


            }
            Inflammable inflam = new Inflammable(name,burningpower,size);
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
            System.out.println("password-"+pass);
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


    public boolean removeInflammable(String username, Inflammable inflam) {
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

                    return true;
            }else {
                return false;
            }


        }catch (Exception e) {
            return false;
        }
    }


    public int executeRegister(String login, String mail, String pass) {
        try {

            PreparedStatement ifLog = connection.prepareStatement("SELECT * FROM users WHERE username=?;");
            ifLog.setString(1,login);
            ResultSet result = ifLog.executeQuery();
            if (result.next()){return 0;}
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
            int leftLimit = 97; //a
            int rightLimit = 122; // z
            int targetStringLength = 8;
            Random random = new Random();
            StringBuilder buffer = new StringBuilder(targetStringLength);
            for (int i = 0; i < targetStringLength; i++) {
                int randomLimitedInt = leftLimit + (int)
                        (random.nextFloat() * (rightLimit - leftLimit + 1));
                buffer.append((char) randomLimitedInt);
            }

            return buffer.toString();

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