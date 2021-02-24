package JETS.ui.helpers;

import java.io.*;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryNotEmptyException;

public class FileManager {
//    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    private FileManager() {
    }

    public static byte[] readFile(File file) {
        byte[] fileData = null;
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))) {
            fileData = bufferedInputStream.readAllBytes();
        } catch (FileNotFoundException e){
            appNotifications.getInstance().errorBox("Unable to open the specified file.", "File Error");
        } catch (IOException e) {
            appNotifications.getInstance().errorBox("Unable to operate on the specified file.", "File Error");
        }
        return fileData;
    }

    public static void writeFile(File file, byte[] data) {
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            bufferedOutputStream.write(data);
        } catch (IOException e){
            appNotifications.getInstance().errorBox("Unable to operate on the specified file.", "File Error");
        }
    }

//    public static void close() {
//        EXECUTOR_SERVICE.shutdown();
//    }
}
