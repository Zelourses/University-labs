package ru.zelourses.lab1.Readers;

import ru.zelourses.lab1.MatrixUtils.IMatrixBuilder;
import ru.zelourses.lab1.MatrixUtils.Matrix;
import ru.zelourses.lab1.MatrixUtils.MatrixFromStringBuilder;

import javax.sound.sampled.Line;
import java.util.Scanner;

public class InputReader implements IReader<IMatrixBuilder<Matrix>> {
    @Override
    public IMatrixBuilder<Matrix> read() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Размер матрицы: ");

        String line;
        String[] result;

        while (true) {
            try {
                line = scanner.nextLine().trim();
                if (line.equals("exit"))
                    return null;
                int size = Integer.parseInt(line);
                if (size <= 20 && size >= 2){
                    result = new String[size+1];
                    result[0] = String.valueOf(size);
                    System.out.println("Введите матрицу. \nОдна строка ввода-одна строка матрицы(включая свободный член)");
                    for (int i = 0; i < size; i++) {
                        String inputLine = scanner.nextLine();
                        if (inputLine.trim().split(" ").length-1 != size && inputLine.trim().split(" ").length !=1){
                            System.out.println("Неправильно задана строка "+ i+1+"\nПоппробуйте ещё раз");
                            i--;
                        }else if (inputLine.trim().split(" ")[0].equals("exit")){
                            return null;
                        }else{
                            result[i+1] = inputLine.toString();
                        }
                    }
                        return new MatrixFromStringBuilder(result);
                }else {
                    System.out.println("Неправильное значение размера матрицы. \nОно должно находиться в пределах от 2 до 20");
                }
            } catch (NumberFormatException e) {
                System.out.println("Неправильный формат размера");
            }
        }
    }
}
