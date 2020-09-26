package code.PointHandling;

import code.PointHandling.Pair;

public abstract class Utils {
    public static Pair<Double, Double> getTwoRoots(Double[][] koefs) {
        double det = koefs[0][0] * koefs[1][1] - koefs[0][1] * koefs[1][0];
        double detA = koefs[0][2] * koefs[1][1] - koefs[0][1] * koefs[1][2];
        double detB = koefs[0][0] * koefs[1][2] - koefs[0][2] * koefs[1][0];
        double result1 = detA / det;
        double result2 = detB / det;
        return new Pair<>(result1,result2);
    }
}
