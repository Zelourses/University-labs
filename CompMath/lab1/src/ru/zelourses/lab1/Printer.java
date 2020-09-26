package ru.zelourses.lab1;

import ru.zelourses.lab1.MatrixUtils.Matrix;

import java.math.BigDecimal;

public class Printer {
    public static void matrixPrinter(Matrix matrix){
        for (int i = 0; i<matrix.getSize();i++) {
            for (int j = 0; j < matrix.getSize(); j++) {
                double result = matrix.getMatrix()[i][j];
                result = (Math.round(result *1000))/1000.0;
                if (result == -0.00 || result == -0d || result == -0)
                    result = 0.0;

                System.out.printf(" %8.3f ",result);
                if (j == matrix.getSize()-1)
                    System.out.printf(" | %8.3f %n", matrix.getFreeParts()[i]);
            }
        }
    }
    public static void resultPrinter(double[] result,String line){
        for (int i = 0; i < result.length;i++){
            System.out.printf(line+"%02d = %7.10f\n",i+1,result[i]);
        }
    }
    public static void deltaPrinter(BigDecimal[] result, String line){
        for (int i = 0; i < result.length;i++){
            System.out.printf(line+"%02d = %33.30f\n",i+1,result[i]);
        }
    }
}
