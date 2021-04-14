package ru;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static ru.MathThings.*;

public class MathFunction {
     MathThings t;
    MathFunction(MathThings t){
         this.t = t;
    }

    public double lab2_func(double x){

        if (x <=0){
            return (((Math.pow((((((Math.pow((Math.pow(t.sec(x) / t.csc(x),2)) + t.cos(x),2)) + (t.sec(x) + t.sec(x))) + t.sec(x)) - t.cot(x)) * (t.sin(x) / (((t.cos(x) + t.csc(x)) * t.csc(x)) / t.sec(x)))) / (Math.pow(t.sec(x) * (t.sec(x) / t.csc(x)),3)),2)) + (t.sec(x) - (t.csc(x) * t.cot(x)))) / (Math.pow(((t.tan(x) / t.sec(x)) + (t.cos(x) + ((t.cot(x) + t.sec(x)) + t.sin(x)))) * t.cos(x),2)));
        }else {
            return Math.pow((((Math.pow(t.log_10(x), 3) - t.ln(x)) - t.log_5(x)) / (Math.pow(Math.pow(t.log_2(x), 2), 3) * t.log_3(x))), 3);
        }
    }

    /*public static void main(String[] args) {
        MathFunction func = new MathFunction(new MathThings(new BasicMath()));
        double PERIOD = Math.PI*2;
        double DELTA = 0.00001;
        ArrayList<Double> array = Arrays.
                stream(new Double[]{PERIOD*(-1), -1.393, -2.314, -3.528, -3.838, -4.016, -5.626,-6.15})
                                            .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<Double> addTo = new ArrayList<>();
        for (Double aDouble : array) {
            addTo.add(aDouble + DELTA);
            addTo.add(aDouble - DELTA);
            addTo.add(aDouble - PERIOD);
        }
        addTo.add(0.0-DELTA);
        array.addAll(addTo);
        array.addAll(Arrays.stream(new Double[]{1.0, 2.1, 4.0, 5.0, 10.0}).collect(Collectors.toCollection(ArrayList::new)));
        //double[] array = {0,-1.393,-2.314,-3.528,-3.838,-4.016,-5.626,-6.15};
        for (double v : array) {
            System.out.println("Math function, x = " + v);
            func.lab2_func(v);
            System.out.println("===================END===================");
        }
        //System.out.println("result"+func.lab2_func(Double.MAX_VALUE));

    }*/
}
