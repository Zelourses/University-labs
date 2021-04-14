package Tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.org.apache.xpath.internal.operations.String;
import essence.Inflammable;
import essence.Thing;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.AccessDeniedException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class WorkWithFiles {

    public static ArrayList<Inflammable> ReadFile(java.lang.String path) throws IOException {

        path = CheckForJsonInEnd(path);
        File f = new File(path);
        if (f.createNewFile()){
            System.out.println("File Doesn't exist, creating new one");
        }
        Scanner sc = new Scanner(f);
        StringBuilder stringBuilder = new StringBuilder();
        while (sc.hasNextLine()) {
            stringBuilder.append(sc.nextLine());
        }
        sc.close();
        java.lang.String string = stringBuilder.toString().replaceAll("\t","").replaceAll("\n","");

        return preparedInfo(string); //preparedInfo-другой метод(внизу)
    }


    public static  Inflammable checkinflaminside(java.lang.String raw){
        raw = raw.replaceAll(" ","");
        raw = raw.replaceAll("\t","");
        raw = raw.replaceAll("\n","");
        raw = raw.replaceAll("\r","");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.enableComplexMapKeySerialization();
        Gson gson = gsonBuilder.create();
        Type type = new TypeToken<ConcurrentHashMap<java.lang.String, Inflammable>>(){}.getType();
        try {
            ConcurrentHashMap<java.lang.String,Inflammable> map = gson.fromJson(raw,type);
            Inflammable inflam =map.entrySet().iterator().next().getValue();

            try {
                if (inflam.getThing() !=null &&inflam.getName() !=null){

                    return inflam;
                }else{
                    System.err.println("Wrong format of Inflammable");
                    return null;
                }
            }catch (Exception e){
                System.err.println("Wrong format of Inflammable");
                return null;
            }
        }catch (Exception e){
            System.err.println("Wrong format of Inflammable");
            return null;
        }
    }

    private static ArrayList<Inflammable> preparedInfo(java.lang.String data){
        ArrayList<Inflammable> raw = new ArrayList<>();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.enableComplexMapKeySerialization();
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        Type type = new TypeToken<ConcurrentHashMap<String,Inflammable>>(){}.getType();
        if (!data.equals("")) {
            ConcurrentHashMap<String, Inflammable> map = gson.fromJson(data, type);
            Iterator<Map.Entry<String, Inflammable>> ss = map.entrySet().iterator();
            while (ss.hasNext()) {
                raw.add(ss.next().getValue());
            }
        }else{
            raw.add(CreateExample());
            System.out.println("File is empty, creating Example object...");
        }

        return raw;
    }

    public static Inflammable CreateExample(){
        Thing th = new Thing("thing1");
        Inflammable test = new Inflammable("Example", 5, 15, th);
        return test;
    }

    public static ConcurrentHashMap<java.lang.String,Inflammable> parseInflammable(java.lang.String s)
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.enableComplexMapKeySerialization();
        gsonBuilder.setLenient();
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        Type type = new TypeToken<ConcurrentHashMap<java.lang.String, Inflammable>>(){}.getType();
        ConcurrentHashMap<java.lang.String,Inflammable> map = gson.fromJson(s,type);
        return map;
    }

    public static Inflammable CheckInflam(ConcurrentHashMap<java.lang.String,Inflammable> map){
        try {
            Inflammable in = (Inflammable) map.values().toArray()[0];
            in.setThing(new Thing((java.lang.String) map.keySet().toArray()[0]));
            in.setZonedtime(ZonedDateTime.now());
            return in;
        }
        catch(Exception e)
        {
            System.out.println("It's not Inflammable");
        }
        return null;
    }



    public static java.lang.String CheckForJsonInEnd(java.lang.String path){
        boolean StartRead = false;

        boolean AllOk = false;

        for (int i = 0;i<path.length();i++){
            if (path.charAt(i) == '.' && !StartRead){
                StartRead = true;
                StringBuilder build = new StringBuilder();
                build.append(path.charAt(i));
                for ( int j = i+1; j<path.length();j++){
                    if (path.charAt(j) !='.'){
                        build.append(path.charAt(j));
                    }else {
                        break;
                    }
                }
                if (build.toString().equals(".json")){
                    AllOk = true;
                    break;
                }
            }
        }

        if (AllOk){
            return path;
        }else {
            return path+".json";
        }

    }
    public static void ShutDown(ConcurrentHashMap<java.lang.String, Inflammable>col, java.lang.String path){
        try {
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(path));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            java.lang.String s = gson.toJson(col);
            outputStream.write(s.getBytes());
            outputStream.close();
            System.out.println("File is saved");
        }
        catch (AccessDeniedException e){
            System.out.println("We don't have access to this file");
            e.printStackTrace();
        }
        catch (FileNotFoundException e){
            System.out.println("We can't find this file");
            e.printStackTrace();
        }
        catch (IOException e){
            System.out.println("Wow, IOException");
            e.printStackTrace();
        }

    }

    public static ConcurrentHashMap<java.lang.String,Inflammable> generate(java.lang.String path) {

        File file;

        if (path == null) { System.out.println("path is null"); return null;}


        else {

            try {
                file = new File(path);
                Scanner sc = new Scanner(file);
                StringBuilder stringBuilder = new StringBuilder();
                while (sc.hasNextLine()) {
                    stringBuilder.append(sc.nextLine());
                }
                sc.close();
                java.lang.String string = stringBuilder.toString().replaceAll("\t","").replaceAll("\n","").replaceAll(" ","");
                return WorkWithFiles.parseInflammable(string);

            }
            catch (Exception e) {
                System.out.println("File is empty or does not exist");
                //e.printStackTrace();
                return null;
            }
        }
    }


}
