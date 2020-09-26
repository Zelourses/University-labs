package code.ApproximateFunctions;


import code.PointHandling.Pair;
import code.PointHandling.Point;
import code.PointHandling.Utils;

import java.util.List;

//function ax+b
public class LinearApproximation implements Approximation{
    @Override
    public Pair<Pair<Double, Double>, Point> approximateReturnWorstPoint(List<Point> points){
        Pair<Double, Double> koefs = approximate(points);
        double a = koefs.getFirst();
        double b = koefs.getSecond();
        double maxDeviation = -1;
        int indexMaxDeviation = -1;
        for (int i = 0; i < points.size(); ++i) {
            double x = points.get(i).getX();
            double deviation = Math.abs(a * x + b - points.get(i).getY());
            if (deviation > maxDeviation) {
                maxDeviation = deviation;
                indexMaxDeviation = i;
            }
        }
        return new Pair<>(koefs, points.get(indexMaxDeviation));
    }

    @Override
    public Pair<Double, Double> approximate(List<Point> points) {
        double sumXX = points.stream().mapToDouble(Point::getX).map(e -> e * e).sum();
        double sumX = points.stream().mapToDouble(Point::getX).sum();
        double sumXY = points.stream().mapToDouble(e -> e.getX() * e.getY()).sum();
        double sumY = points.stream().mapToDouble(Point::getY).sum();
        double n = points.size();
        return Utils.getTwoRoots(new Double[][]{{sumXX, sumX, sumXY}, {sumX, n, sumY}});
    }
}
