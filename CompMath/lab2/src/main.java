import Functions.HyperbolicFunction;
import Functions.IFunction;
import Functions.ParabolicFunction;
import Functions.SimpleFunction;

import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        System.out.println("Лабораторная работа №2");

        System.out.println("Введите цифру графика:\n" +
                            " 1 - 1/|x|\n" +
                            "2 - x\n" +
                            "3 - x*x\n");
        Scanner scanner = new Scanner(System.in);
        IFunction func = null;
        while (true){
            System.out.print(">> ");

            try {
                int i = Integer.parseInt(scanner.nextLine().trim());
                switch (i){
                    case 1:
                       func = new HyperbolicFunction();
                       break;
                    case 2:
                        func = new SimpleFunction();
                        break;
                    case 3:
                        func = new ParabolicFunction();
                        break;
                }
            } catch (NumberFormatException ignored) {
            }
            if(func != null){
                break;
            }
        }

        Solver solver = new Solver();
        solver.solve(func);
    }
}
