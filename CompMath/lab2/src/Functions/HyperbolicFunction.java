package Functions;

public class HyperbolicFunction implements IFunction{


    @Override
    public double getY(double x) {
       return 1/Math.abs(x);
    }
}
