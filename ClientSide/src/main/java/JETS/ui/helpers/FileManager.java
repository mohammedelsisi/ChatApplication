package JETS.ui.helpers;

import java.io.*;

public class FileManager {
//    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    private FileManager() {
    }

    public static byte[] readFile(File file) throws IOException {
        byte[] fileData = null;
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))) {
            fileData = bufferedInputStream.readAllBytes();
        }
        return fileData;
    }

    public static void writeFile(File file, byte[] data) throws IOException {
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            bufferedOutputStream.write(data);
        }
    }

//    public static void close() {
//        EXECUTOR_SERVICE.shutdown();
//    }
}
