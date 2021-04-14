package ru;

public class MathThings {
    private static final double EPSILON = 1e-9;
    private static final int MAX_STEPS = 25;
    private final BasicMath basic;

    public MathThings(BasicMath math){
        basic = math;
    }

    private double factorial (int x){
        double result = 1.0;
        while (x > 0) {
            result *= x;
            x--;
        }
        return result;
    }

    private  double innerCos(double x){
        return basic.innerSin(x+Math.PI/2);
    }

    public  double sin(double x){
        if (!Double.isFinite(x)) {
            //System.out.printf("x: %10.15f\nmy_sin: NaN\nlib_sin: NaN\n",x);
            return Double.NaN;
        }
        //System.out.printf("x: %10.15f\nmy_sin: %10.15f\nlib_sin: %10.15f\n",x,innerSin(x),Math.sin(x));
        return basic.innerSin(x);
    }

    public  double cos(double x){
        if (!Double.isFinite(x)) {
            //System.out.printf("x: %10.15f\nmy_cos: NaN\nlib_cos: NaN\n",x);
            return Double.NaN;
        }
        //System.out.printf("x: %10.15f\nmy_cos: %10.15f\nlib_cos: %10.15f\n",x,innerCos(x),Math.cos(x));
        return innerCos(x);
    }

    public  double tan(double x){
        if (!Double.isFinite(x)) {
            //System.out.printf("x: %10.15f\nmy_tan: NaN\nlib_tan: NaN\n",x);
            return Double.NaN;
        }
        //System.out.printf("x: %10.15f\nmy_tan: %10.15f\nlib_tan: %10.15f\n",x,innerSin(x)/innerCos(x),Math.tan(x));
        return basic.innerSin(x)/innerCos(x);
    }

    public  double cot(double x){
        if (!Double.isFinite(x)) {
            //System.out.printf("x: %10.15f\nmy_cot: NaN\nlib_cot: NaN\n",x);
            return Double.NaN;
        }
        //System.out.printf("x: %10.15f\nmy_cot: %10.15f\nlib_cot: %10.15f\n",x,innerCos(x)/innerSin(x),Math.cos(x)/Math.sin(x));
        return innerCos(x)/basic.innerSin(x);
    }

    public  double sec(double x){
        if (!Double.isFinite(x)) {
            //System.out.printf("x: %10.15f\nmy_sec: NaN\nlib_sec: NaN\n",x);
            return Double.NaN;
        }
        //System.out.printf("x: %10.15f\nmy_sec: %10.15f\nlib_sec: %10.15f\n",x,1/innerCos(x),1/Math.cos(x));
        return 1/innerCos(x);
    }

    public  double csc(double x){
        if (!Double.isFinite(x)) {
            //System.out.printf("x: %10.15f\nmy_csc: NaN\nlib_csc: NaN\n",x);
            return Double.NaN;
        }
        //System.out.printf("x: %10.15f\nmy_csc: %10.15f\nlib_csc: %10.15f\n",x,1/innerSin(x),1/Math.sin(x));
        return 1/basic.innerSin(x);
    }

    public  double ln(double x){
        if (x <=0 || !Double.isFinite(x)) {
            //System.out.printf("x: %10.15f\nmy_ln: NaN\nlib_ln: NaN\n",x);
            return Double.NaN;
        }
        //System.out.printf("x: %10.15f\nmy_ln: %10.15f\nlib_ln: %10.15f\n",x,innerLn(x),Math.log(x));
        return basic.innerLn(x);
    }

    public  double log_5(double x){
        if (x <=0 || !Double.isFinite(x)) {
            //System.out.printf("x: %10.15f\nmy_log_5: NaN\nlib_log_5: NaN\n", x);
            return Double.NaN;
        }
        //System.out.printf("x: %10.15f\nmy_log_5: %10.15f\nlib_log_5: %10.15f\n",x,innerLn(x)/innerLn(5),Math.log(x)/Math.log(5));
        return basic.innerLn(x)/basic.innerLn(5);
    }

    public  double log_2(double x){
        if (x <=0 || !Double.isFinite(x)) {
            //System.out.printf("x: %10.15f\nmy_log_2: NaN\nlib_log_2: NaN\n",x);
            return Double.NaN;
        }
        //System.out.printf("x: %10.15f\nmy_log_2: %10.15f\nlib_log_2: %10.15f\n",x,innerLn(x)/innerLn(2),Math.log(x)/Math.log(2));
        return basic.innerLn(x)/basic.innerLn(2);
    }

    public  double log_3(double x){
        if (x <=0 || !Double.isFinite(x)) {
            //System.out.printf("x: %10.15f\nmy_log_3: NaN\nlib_log_3: NaN\n",x);
            return Double.NaN;
        }
        //System.out.printf("x: %10.15f\nmy_log_3: %10.15f\nlib_log_3: %10.15f\n",x,innerLn(x)/innerLn(3),Math.log(x)/Math.log(3));
        return basic.innerLn(x)/basic.innerLn(3);
    }

    public  double log_10(double x){
        if (x <=0 || !Double.isFinite(x)) {
            //System.out.printf("x: %10.15f\nmy_log_10: NaN\nlib_log_10: NaN\n",x);
            return Double.NaN;
        }
        //System.out.printf("x: %10.15f\nmy_log_10: %10.15f\nlib_log_10: %10.15f\n",x,innerLn(x)/innerLn(10),Math.log(x)/Math.log(10));
        return basic.innerLn(x)/basic.innerLn(10);
    }


}
