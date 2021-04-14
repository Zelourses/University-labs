package Tools;

import com.google.gson.reflect.TypeToken;
import essence.Inflammable;
import com.google.gson.*;
import essence.Thing;

import javax.net.ssl.SSLServerSocket;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.AccessDeniedException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class WorkWithFiles {

    public static ArrayList<Inflammable> ReadFile(String path) throws IOException {

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
        String string = stringBuilder.toString().replaceAll("\t","").replaceAll("\n","");

        return preparedInfo(string); //preparedInfo-другой метод(внизу)
    }

    private static ArrayList<Inflammable> preparedInfo(String data){
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

    public static boolean CheckIfInflammableIn(String raw){
        Gson r = new Gson();
        //Type type = new TypeToken<Inflammable>(){}.getType();
        raw = raw.replaceAll(" ","");
        raw = raw.replaceAll("\t","");
        raw = raw.replaceAll("\n","");
        raw = raw.replaceAll("\r","");
        char[] b =raw.toCharArray();
//        for (int i=0;i<b.length;i++){
//            if (b.equals("\"")){
//
//            }
//        }
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.enableComplexMapKeySerialization();
        Gson gson = gsonBuilder.create();
        Type type = new TypeToken<ConcurrentHashMap<String, Inflammable>>(){}.getType();
        try {
            ConcurrentHashMap<String,Inflammable> map = gson.fromJson(raw,type);
            Inflammable inflam =map.entrySet().iterator().next().getValue();


        ConcurrentHashMap<String,Inflammable> concurrentHashMap = null;





        try {
            //Inflammable inflam =map.entrySet().iterator().next().getValue()
        //Inflammable in = r.fromJson(raw,Inflammable.class);


//            System.out.println("burningpower:"+in.getBurningPower()+
//                    "size: "+in.getSize()+
//                    "thing: "+in.getThing()+
//                    "date: "+in.getDate()+
//                    "name: "+in.getName());


        if (inflam.getThing() !=null &&inflam.getName() !=null){
            return true;
        }else{
            System.err.println("Wrong format of Inflammable");
            return false;
        }
        }catch (Exception e){
            System.err.println("Wrong format of Inflammable");
            return false;
        }
        }catch (Exception e){
            System.err.println("Wrong format of Inflammable");
            return false;
        }
    }



    public static String CheckForJsonInEnd(String path){
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
public static void ShutDown(ConcurrentHashMap<String, Inflammable>col,String path){
    try {
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(path));
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String s = gson.toJson(col);
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


}
