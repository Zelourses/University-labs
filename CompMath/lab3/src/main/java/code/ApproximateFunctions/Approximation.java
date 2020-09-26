package code.ApproximateFunctions;

import code.PointHandling.Pair;
import code.PointHandling.Point;

import java.util.List;

public interface Approximation {
    Pair<Pair<Double, Double>, Point> approximateReturnWorstPoint(List<Point> points);

    Pair<Double, Double> approximate(List<Point> points);
}
