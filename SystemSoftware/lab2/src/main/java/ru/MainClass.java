package ru;

import ru.JNI.Filesystem;

public class MainClass {
    private static final String programName = "lab";

    public static void main(String[] args) {
        Filesystem filesystem = new Filesystem();
        if (args.length == 0){
            filesystem.printExitText("lab",programName);
        }
        if (args[0].equals("-h") || args[0].equals("--help")){
            filesystem.printHelp();
        }else if (args[0].equals("-w") || args[0].equals("--work")){
            if (args.length  == 1){
                filesystem.printExitText("-w",programName);
                return;
            }
            filesystem.startWork(args[1]);
            return;
        }else if (args[0].equals("-l") || args[0].equals("--list")){
            filesystem.listAllPartitions();
            return;
        }
        filesystem.printExitText(programName, programName);
    }

}