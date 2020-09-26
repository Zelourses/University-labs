import Functions.IFunction;

import java.util.Scanner;

public class Solver {
    private  static final double EPSILON = 1e-9;
    public void solve(IFunction function){
        Scanner scanner = new Scanner(System.in);

        Double lowerLimit,upperLimit,accuracy;
        lowerLimit=upperLimit=accuracy = null;
        do {
            System.out.println("Введите нижний предел");
            try {

                lowerLimit = Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Введите число");
            }
        }while (lowerLimit == null);
        do {
            System.out.println("Введите верхний предел");
            try {

                upperLimit = Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Введите число");
            }
        }while (upperLimit == null);
        do {
            System.out.println("Введите точность");
            try {

                accuracy = Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Введите число");
            }
        }while (accuracy == null);
        upperLimit = Math.max(lowerLimit,upperLimit);
        lowerLimit = Math.min(lowerLimit,upperLimit);

            double[] result =calculate(function,lowerLimit,upperLimit,accuracy);
            if (Double.isFinite(result[0]) && Double.isFinite(result[1]) && Double.isFinite(result[2])) {
                System.out.printf("В результате мы получили:\n" +
                        "результат = %7.15f\n" +
                        "количество делений: %-10d\n" +
                        "ошибка: %7.15f", result[0], (int) (result[1]), result[2]);
            }else {
                System.out.println("Интеграл нельзя посчитать");
            }
    }
    private  double[] calculate(IFunction function, double lowerLimit, double upperLimit, double accuracy) {
        int amountOfDivisions = 2;
        double error = 10000;
        double step = (upperLimit - lowerLimit)/amountOfDivisions;

        double lowValue = calculatePart(function,Cases.PLUS,lowerLimit);
        double upValue = calculatePart(function,Cases.MINUS,lowerLimit);
        double value = CalculateValWithMultiplier(function,lowerLimit,step,1);

        double previousValue = (step / 3)*(lowValue + value + upValue);
        double currentValue = 0;

        while (error > accuracy) {

            amountOfDivisions *= 2;

            step = (upperLimit - lowerLimit)/amountOfDivisions;

            currentValue = (step / 3)*(calculateSum(function,amountOfDivisions, step, lowerLimit));

            error = (Math.abs(currentValue - previousValue)/15);

            previousValue = currentValue;
        }

        return new double[]{ currentValue, amountOfDivisions, error};
    }

    private  double calculateSum(IFunction function, double stepCounter, double step, double lowerLimit){
        double result = 0;

        double value;
        for (int i = 1; i < stepCounter; i+=2){

            double tmp = 0;

            value = function.getY(lowerLimit + step*(i-1));
            if (!Double.isFinite(value))
                if (i==0)
                    value= CalculateValWithMultiplier(function,lowerLimit,0,1);
                else
                value = CalculateValWithMultiplier(function,lowerLimit,step*(i-1),1);
            tmp += value;

            value = function.getY(lowerLimit + step*i);
            if (!Double.isFinite(value))
                value = CalculateValWithMultiplier(function, lowerLimit,step*i,4);
            tmp += value;

            value = function.getY(lowerLimit + step*(i+1));
            if (!Double.isFinite(value))
                if (i==stepCounter)
                    value = CalculateValWithMultiplier(function,lowerLimit,0,1);
                else
                    value = CalculateValWithMultiplier(function,lowerLimit,step*(i+1),1);
            tmp += value;

            result += tmp;
        }
        return result;
    }
    private double calculatePart(IFunction func, Cases cases, double x){
        double val = func.getY(x);
        if (!Double.isFinite(val)){
            if (cases == Cases.PLUS)
                return func.getY(x+EPSILON);
            else if (cases == Cases.MINUS)
                return func.getY(x-EPSILON);
        }
        return val;
    }
    private double CalculateValWithMultiplier(IFunction func, double x, double step, double multiplier){
        double value = multiplier * func.getY(x + step);
        if (!Double.isFinite(value))
            return multiplier *(func.getY((x + step + EPSILON)) + func.getY(x + step - EPSILON))/2;
        return value;
    }

}
