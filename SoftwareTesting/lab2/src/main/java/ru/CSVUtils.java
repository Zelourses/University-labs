package ru;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class CSVUtils {
    public void generateCsv(double from, double to, double step){
        MathThings thing = new MathThings(new BasicMath());
        MathFunction func = new MathFunction(thing);
        try {
            PrintWriter writer = new PrintWriter("out.csv");
            double x = from;
            writer.println("x,f(x),sin(x),cos(x),tan(x),cot(x),sec(x),csc(x),log(x),log2(x),log3(x),log5(x),log10(x)");
            while (x < to){
                writer.print(x+","+func.lab2_func(x)+","+thing.sin(x)+","+thing.cos(x)+
                    thing.tan(x)+","+thing.cot(x)+","+thing.sec(x)+","+thing.csc(x));
                if (x > 0) {
                    writer.println(thing.ln(x)+","+thing.log_2(x)+","+thing.log_3(x)+","+thing.log_5(x)+","+thing.log_10(x));
                }else{
                    writer.println("NaN,NaN,NaN,NaN,NaN");
                }
                x+=step;
                if (Math.abs(x) <=1e-6)
                    x = 0.0;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CSVUtils m = new CSVUtils();
        m.generateCsv(-10.0,10.0,0.1);
    }
}
