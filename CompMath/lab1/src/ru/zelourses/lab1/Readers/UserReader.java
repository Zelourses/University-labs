package ru.zelourses.lab1.Readers;

import ru.zelourses.lab1.MatrixUtils.IMatrixBuilder;
import ru.zelourses.lab1.MatrixUtils.Matrix;

import java.util.Scanner;

public class UserReader implements IReader<IMatrixBuilder<Matrix>> {
    @Override
    public IMatrixBuilder<Matrix> read() {
        Scanner scanner = new Scanner(System.in);

        IReader<IMatrixBuilder<Matrix>> reader = null;
        while (true){
            System.out.print(">> ");
            String input = scanner.nextLine();
            switch (input.trim()){
                case ("help"):
                    System.out.println("Программа поддерживает следущие команды:\n" +
                                        "\thelp - вывод справки помощи.\n" +
                                        "\twrite - написать матрицу вручную.\n" +
                                        "\treadfile - прочитать матрицу из файла.\n" +
                                        "\trandom - создать случайную матрицу, с последующим её решением\n" +
                                        "Если вы хотите выйти из выбранного вами режима, напишите exit");
                break;

                case ("write"):
                    reader = new InputReader();
                break;

                case ("readfile"):
                    reader = new FileReader();
                break;

                case ("random"):
                    reader = new RandomReader();
                break;
            }
            if (reader != null){
                IMatrixBuilder<Matrix> builder = reader.read();
                if (builder != null)
                    return builder;
            }
        }
    }
}
