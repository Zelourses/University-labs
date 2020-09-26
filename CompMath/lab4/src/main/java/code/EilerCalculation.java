package code;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class EilerCalculation {
    public static List<Point> calculate(DoubleFunction function, double x0, double y0, double Xn, double step){
        List<Point> p = new ArrayList<>();
        p.add(new Point(x0,y0));
        while (x0<Xn){
            double y = y0 + function.apply(x0,y0)*step;
            double x = x0 + step;
            p.add(new Point(x,y));
            y0 = y;
            x0 = x;
        }
        System.out.println("ssssss");
        return p;
    }
}
