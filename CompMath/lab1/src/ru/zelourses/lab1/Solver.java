package ru.zelourses.lab1;

import ru.zelourses.lab1.MatrixUtils.Matrix;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Objects;

public class Solver {
    Matrix matrix;
    double[][] data,data_copy;
    double[] freeParts,freeParts_copy;
    public void solveMatrix(Matrix matrix){
        this.matrix = matrix;
            this.data_copy = new double[matrix.getSize()][matrix.getSize()];
            for (int i=0; i < matrix.getSize(); i++){
                System.arraycopy(matrix.getMatrix()[i],0,this.data_copy[i],0,matrix.getSize());
            }
            this.data = matrix.getMatrix();
            this.freeParts = matrix.getFreeParts();
            this.freeParts_copy = new double[matrix.getSize()];
            System.arraycopy(matrix.getFreeParts(),0,this.freeParts_copy,0,matrix.getSize());
            System.out.println("Полученная матрица: ");
            Printer.matrixPrinter(this.matrix);
                int swaps = makeTriangular();

            double det = calculateDet() * (swaps % 2==0 ?1:-1);
            if ((det)!=0d){
                System.out.printf("Детерминант: %7.3f\n",det);
                Matrix half_way_result = new Matrix(matrix.getSize());
                half_way_result.setMatrix(data);
                half_way_result.setFreeParts(freeParts);
                System.out.println("Созданная треугольная матрица: ");
                Printer.matrixPrinter(half_way_result);
               double[] result =  backStep();
                System.out.println("Неизвестные: ");
                Printer.resultPrinter(result,"X");
                System.out.println("Невязка: ");
                BigDecimal[] delta = countDelta(result);
                Printer.deltaPrinter(delta,"Δ");
            }else {
                System.out.println("Определитель равен 0, завершаем подсчёт...");
            }
    }
    private BigDecimal[] countDelta(double[] result){
        double[][] clear_matrix = this.data_copy;
        BigDecimal[] delta = new BigDecimal[this.matrix.getSize()];
        for (int i=0; i<this.matrix.getSize();i++){
            double tmp_result_delta = 0;
            for (int j = 0; j < this.matrix.getSize(); j++){
                tmp_result_delta = tmp_result_delta+ clear_matrix[i][j]*result[j];
            }
            delta[i] = BigDecimal.valueOf(tmp_result_delta - freeParts_copy[i]);
        }
        return delta;
    }

    private double[] backStep(){
        double[] result = new double[matrix.getSize()];
        for (int i = matrix.getSize()-1; i >= 0;i--){
                if (i+1<matrix.getSize()){
                    double allKnownResults = 0;
                        for (int j = matrix.getSize()-1;j>i && j>0;j--){
                            allKnownResults = allKnownResults+data[i][j]*result[j];
                        }
                    result[i]=(freeParts[i]-allKnownResults)/data[i][i];
                }else {
                    result[i]=freeParts[i] /data[i][i];
                }
        }
        return result;
    }
    private double calculateDet(){
        double det = 1.0d;
        for(int i = 0; i<matrix.getSize();i++){
            det *= data[i][i];
        }
        if (det ==-0.0d)
            det = 0d;
        return det;
    }
    private int makeTriangular(){
        int swapped = 0;
        for (int i = 0; i < matrix.getSize(); i++){
            if (data[i][i] == 0 || data[i][i] == 0.000d){
                 int tmp = swapLine(i);
                 if (tmp == 0){
                    swapped+=1;
                     break;
                 }
                 swapped+=tmp;
            }
            for (int j = i+1; j < matrix.getSize();j++){
                double multiplier = -data[j][i] / data[i][i];
                freeParts[j] = freeParts[j]+freeParts[i]*multiplier;
                for (int k = i; k< matrix.getSize(); k++){
                    data[j][k] = data[j][k]+data[i][k]*multiplier;
                }
            }
        }
        return swapped;
    }
    private int swapLine(int line){
        int swappedTimes =0;
        for (int i = line+1; i < matrix.getSize();i++){
            if (data[i][line] != 0 || data[i][line] != 0.0){
                swappedTimes++;
                double[] tmp = data[line];
                data[line] = data[i];
                data[i] = tmp;
                double tmp1 = freeParts[line];
                freeParts[line] = freeParts[i];
                freeParts[i] = tmp1;
            }
        }
        return swappedTimes;
    }
    private boolean isLowerTriangular(){
        if (matrix.getSize() < 2)
            return false;

        for (int i = 0;i < matrix.getSize();i++){
            for (int j = 0; i > j;j++){
                if (matrix.getMatrix()[j][i] != 0)
                    return false;
            }
        }
        return true;
    }
    private boolean isUpperTriangular(){
        if (matrix.getSize() < 2)
            return false;

        for (int i =0; i < matrix.getSize(); i++){
            for (int j = 0; j < i;j++){
                if (matrix.getMatrix()[i][j] != 0)
                    return false;
            }
        }
        return true;
    }
}
