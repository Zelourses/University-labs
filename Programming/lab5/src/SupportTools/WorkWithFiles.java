package SupportTools;

import com.google.gson.*;
import essence.Inflammable;
import essence.Thing;
import sun.applet.Main;


import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;

public class WorkWithFiles {
        public static ArrayList<Inflammable> ReadFile(String path) throws IOException {

                path = CheckForJsonInEnd(path);
                File f = new File(path);
                if (f.createNewFile()){
                    System.out.println("File Doesn't exist, creating new one");
                }

                InputStreamReader reader = new InputStreamReader(new FileInputStream(path),"UTF-8");



            JsonStreamParser parser = new JsonStreamParser(reader);


            return preparedInfo(parser, path); //preparedInfo-другой метод(внизу)
        }

        public static void WriteInFile(String JSONdata, String path) throws IOException, ParserConfigurationException {
        FileOutputStream write = new FileOutputStream(path,false);

        byte[] buffer = JSONdata.getBytes();
        write.write(buffer, 0 ,buffer.length);
        write.close();

    }

        public static Inflammable toEssence(String file){
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            file = file.replaceAll(" ","");
            file = file.replaceAll("\t","");
            file = file.replaceAll("\n","");
            file = file.replaceAll("\r","");
             Inflammable raw = gson.fromJson(file,Inflammable.class);


             return  raw;
        }

        private static ArrayList<Inflammable> preparedInfo(JsonStreamParser s, String path) throws EOFException{
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            ArrayList<Inflammable> test = new ArrayList<>();

            try {

                if (!s.hasNext()){
                    throw new JsonIOException("");
                }

            }catch (JsonIOException e){
                System.err.println("Doesn't found any object in file, creating one.");
                CreateExample(path);
            }finally {
                while (s.hasNext()) {
                    JsonElement e = s.next();
                    if (e.isJsonObject()) {
                        Inflammable m = gson.fromJson(e, Inflammable.class);
                        test.add(m);

                    }
                }
            }
            return test;
        }

        public static String TransformToJSON(LinkedHashMap<String,Inflammable> map){
            Iterator<Map.Entry<String,Inflammable>> iter = map.entrySet().iterator();
            StringBuilder builder = new StringBuilder();
            Gson jsonString = new GsonBuilder().setPrettyPrinting().create();
            while (iter.hasNext()){
                Inflammable rawinflam = iter.next().getValue();
                String hren = jsonString.toJson(rawinflam,Inflammable.class);
                builder.append(hren);
            }
            return builder.toString();
        }

    public static void CreateExample(String path){
        Thing th = new Thing("thing1");
        Date d = new Date();

        Inflammable test = new Inflammable("Example", 5, 15, th,d);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String ss = gson.toJson(test);
        File file = new File(path);
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write(ss);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();

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
                        StartRead = true;
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




}
