package ru.zelourses.lab1.MatrixUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class MatrixFromStringBuilder implements IMatrixBuilder<Matrix> {
    private String[] text;
    @Override
    public Matrix buildMatrix() {
        if (text == null)
            return null;
        int size = Integer.parseInt(text[0]);
        double[][] data = new double[size][size];
        double[] freeParts = new double[size];
        //Этот адский цикл for преобразует из полученного массива String
        // готовый массив элемментов матрицы+свободных членов
        for (int i = 1;i<=size;i++){
            double[] tmp_data = Arrays.stream(text[i].split(" ")).mapToDouble(Double::parseDouble).toArray();
            double freePart = tmp_data[tmp_data.length-1];
            freeParts[i-1] = freePart;
            double[] data_part = new double[size];
            for(int j=0; j < size ;j++){
                data_part[j] = tmp_data[j];
            }
            data[i-1] = data_part;
        }
        Matrix matrix = new Matrix(size);
        matrix.setMatrix(data);
        matrix.setFreeParts(freeParts);
        return matrix;
    }
    public MatrixFromStringBuilder(String[] text){
        this.text = text;
    }

}
