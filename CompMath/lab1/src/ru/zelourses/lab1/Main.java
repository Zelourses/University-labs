package ru.zelourses.lab1;

import ru.zelourses.lab1.MatrixUtils.IMatrixBuilder;
import ru.zelourses.lab1.MatrixUtils.Matrix;
import ru.zelourses.lab1.Readers.RandomReader;
import ru.zelourses.lab1.Readers.UserReader;

public class Main {
    public static void main(String[] args) {
        System.out.println("Лаба1 - Метод Гаусса");
        System.out.println("Для подробностей введите help.");
        System.out.println("Программа поддерживает следущие команды:\n" +
                "\thelp - вывод справки помощи.\n" +
                "\twrite - написать матрицу вручную.\n" +
                "\treadfile - прочитать матрицу из файла.\n" +
                "\trandom - создать случайную матрицу, с последующим её решением\n" +
                "Если вы хотите выйти из выбранного вами режима, напишите exit");
        while (true) {
            UserReader userReader = new UserReader();
            Solver solver = new Solver();
            solver.solveMatrix(userReader.read().buildMatrix());
        }
    }
}
