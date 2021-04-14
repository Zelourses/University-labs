package ru;

public class BasicMath {
    private static final double EPSILON = 1e-9;
    private static final int MAX_STEPS = 25;

    public  double innerSin(double x){
        double result = 0;
        for (int i =MAX_STEPS; i >= 1; i-=2){
            result += ((i-1) % 4 == 0 ? 1 : -1) * Math.pow(x,i)/ factorial(i);
        }
        //System.out.printf("x: %10.30f\nmy_sin: %10.30f\nlib_sin: %10.30f\n",x,result,Math.sin(x));
        return result;
    }

    private double factorial (int x){
        double result = 1.0;
        while (x > 0) {
            result *= x;
            x--;
        }
        return result;
    }

    public   double innerLn(double x){
        double result = 0.0;
        double monomial;
        double monomialIndex = 0;
        do {
            monomial = 1.0/(2*monomialIndex + 1) * Math.pow(((x-1)/(x+1)),(2*monomialIndex + 1));
            result += monomial;
            monomialIndex++;
        } while (Math.abs(monomial) > EPSILON);
        result *= 2;
        //System.out.printf("x: %10.15f\nmy_ln: %10.15f\nlib_ln: %10.15f\n",x,result,Math.log(x));
        return result;
    }
}
