package ru.parsing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Parser {
    public static void main(String[] args) throws IOException {
        List<String> file = Files.readAllLines(Path.of("result.txt"));
        HashMap<String, ArrayList<StringStruct>> funcs = new HashMap<>();
        funcs.put("sin",new ArrayList<>());
        funcs.put("cos",new ArrayList<>());
        funcs.put("tan",new ArrayList<>());
        funcs.put("cot",new ArrayList<>());
        funcs.put("sec",new ArrayList<>());
        funcs.put("csc",new ArrayList<>());
        funcs.put("log_10",new ArrayList<>());
        funcs.put("log_5",new ArrayList<>());
        funcs.put("log_2",new ArrayList<>());
        funcs.put("log_3",new ArrayList<>());
        funcs.put("ln",new ArrayList<>());

        for (int i =0; i < file.size()-1;){
            String tmpLine =file.get(i);
            if (!tmpLine.contains("x:")){
                //System.out.println(file.get(i));
                i++;
                continue;
            }

            int finalI = i;
            //System.out.println(file.get(finalI)+"\n"+file.get(finalI+1)+"\n"+file.get(finalI+2)+"\n");
            funcs.forEach((k, v)->{
                if (file.get(finalI +1).contains(k)){
                    v.add(new StringStruct(file.get(finalI),file.get(finalI+1),file.get(finalI+2)));
                }
            });
            //System.out.println("Ended iteration");
            i+=3;
        }



        funcs.forEach((k,v)->{
            //System.out.println(k);
            ArrayList<String> answer = new ArrayList<>();
            ArrayList<String> question = new ArrayList<>();
            for (int i=0;i<v.size();i++){
                if (!question.contains(v.get(i).x)) {
                    question.add(v.get(i).x);
                    answer.add(v.get(i).lib);
                }
            }
            //System.out.println("SIZE = "+question.size());
            System.out.print("double[] "+k+"_q = {");
            createArray(question);
            //System.out.println("SIZE = "+answer.size());
            System.out.print("double[] "+k+"_a = {");
            createArray(answer);

            System.out.println("");
        });

    }

    private static void createArray(ArrayList<String> array) {
        for (int i=0;i<array.size();i++){
            String result = array.get(i).split(":")[1];
            System.out.print(result.contains("Infinity")?"Double.POSITIVE_INFINITY":result);
            if (i+1<array.size())
                System.out.print(", ");
        }
        System.out.println("};");
    }
}
