package ru.zelourses.lab1.Readers;

import ru.zelourses.lab1.MatrixUtils.IMatrixBuilder;
import ru.zelourses.lab1.MatrixUtils.Matrix;
import ru.zelourses.lab1.MatrixUtils.MatrixFromRandomBuilder;

import java.util.Random;
import java.util.Scanner;

public class RandomReader implements IReader<IMatrixBuilder<Matrix>> {
    @Override
    public IMatrixBuilder<Matrix> read() {
        Random random = new Random();
        random.nextDouble();
        random.nextDouble();
        random.nextDouble();
        random.nextDouble();
        random.nextDouble();
        random.nextDouble();
        random.nextDouble();
        random.nextDouble();
        random.nextDouble();
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.print("Размер матрицы: ");
            try {
                String line = scanner.nextLine().trim();
                if (line.equals("exit"))
                    return null;
                int size = Integer.parseInt(line);
                if (size>20 || size<2)
                    System.out.println("Неправильное значение размера матрицы. \nОно должно находиться в пределах от 2 до 20");

                double[][] matrix = new double[size][size];
                double[] freeParts = new double[size];
                for (int i = 0; i < size; i++){
                    for (int j = 0; j < size; j++){
                        matrix[i][j] = 100+(random.nextDouble()*200);
                    }
                    freeParts[i] = 100+(random.nextDouble()*200);
                }
                return new MatrixFromRandomBuilder(matrix,freeParts,size);
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат числа");
            }

        }
    }
}
