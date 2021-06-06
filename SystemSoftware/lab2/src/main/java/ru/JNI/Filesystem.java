package ru.JNI;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.Objects;

public class Filesystem {
    static {
        Filesystem.loadLibrary("lib/linux/NTFS.so");
    }

    public native void printHelp();
    public native void startWork(String nameOfPartition);
    public native void listAllPartitions();
    public native void print();
    public native void printExitText(String argumentName, String programName);


    //Helper function to right loading of library
    private static void loadLibrary(String fileName) {
        try {
            URL url = Filesystem.class.getResource("/" + fileName);
            File tmpDir = Files.createTempDirectory("libs").toFile();
            tmpDir.deleteOnExit();
            File nativeLibTmpFile = new File(tmpDir, fileName);
            nativeLibTmpFile.deleteOnExit();
            try (InputStream in = Objects.requireNonNull(url).openStream()) {
                Files.createDirectories(nativeLibTmpFile.toPath().getParent());
                Files.copy(in, nativeLibTmpFile.toPath());
            }
            System.load(nativeLibTmpFile.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
