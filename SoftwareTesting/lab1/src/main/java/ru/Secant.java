package ru;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Secant {
    private static final double EPSILON = 0.000000001;
    private static final double MINIMAL_VALUE = -1e15;
    public static double sec(double x) {
        if (!Double.isFinite(x))
            return Double.NaN;
        double result = truncateDecimal(1/ doCos(x));
        if (result < 1e15 && result > MINIMAL_VALUE)
            return result;
        else
            return Double.POSITIVE_INFINITY;
    }

    private static double doCos(double x) {
        double part =Double.MAX_VALUE;
        double result = 0;
        int n = 0;

        while (Math.abs(part) > EPSILON && n < 11) {
            part = truncateDecimal((Math.pow(-1, n)*Math.pow(x,2*n))/ doFact(2*n));
            result += part;
            n++;
        }

        return result;
    }
    private static long doFact(int n) {
        if (n == 0 ||n == 1)
            return 1;
        long result =1;
        for(int i =2;i <=n;i++){
            result *=i;
        }
        return result;
    }

    private static double truncateDecimal(double x)
    {
        if ( x > 0) {
            return new BigDecimal(String.valueOf(x)).setScale(9, RoundingMode.FLOOR).doubleValue();
        } else {
            return new BigDecimal(String.valueOf(x)).setScale(9,RoundingMode.CEILING).doubleValue();
        }
    }
}
