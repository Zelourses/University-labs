package ru.zelourses.lab1.Readers;

import ru.zelourses.lab1.MatrixUtils.IMatrixBuilder;
import ru.zelourses.lab1.MatrixUtils.Matrix;
import ru.zelourses.lab1.MatrixUtils.MatrixFromStringBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileReader implements IReader<IMatrixBuilder<Matrix>> {
    @Override
    public IMatrixBuilder<Matrix> read() {
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.print("Имя файла: ");
            File file = new File(scanner.nextLine());
            if (file.exists() && file.canRead()) {
                String[] fileResult = readFile(file);
                if (fileResult == null)
                    System.out.println("Неверный формат файла " + file.getName());
                return new MatrixFromStringBuilder(fileResult);
            }else if (file.getName().equals("exit")){
                return null;
            }else{
                System.out.println("Файл не существует или у программы нету доступа к нему");
            }

        }
    }
    private static String[] readFile(File file){
        List<String> result = new ArrayList<>();

        try {
            Files.lines(file.toPath()).forEachOrdered(result::add);
            int size = Integer.parseInt(result.get(0));
            if (result.size()-1 != size || size<2)
                return null;
            for (int i=1;i<result.size();i++)
                if (result.get(i).split(" ").length-1 != size)
                    return null;
            String[] array_result = new String[result.size()];
            return array_result = result.toArray(array_result);
        } catch (IOException e) {
            System.out.println("Хм, похоже что-то сломалось. Это произошло в "+FileReader.class);
            return null;
        }
    }
}
