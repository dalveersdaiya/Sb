package in.ajm.sb.helper;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipHelper {
    private static final int BUFFER = 80000;
    private static final int BUFFER_SIZE = 1024;

    public static boolean zip(File[] files, String destinationPath, String zipFileName) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(zipFileName);
            ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(fileOutputStream));
            for (File file : files) {
                try {
                    compressFile(file, destinationPath, zipOutputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            zipOutputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void compressFile(File file, String path, ZipOutputStream zos) throws IOException {
        if (!file.isDirectory()) {
            byte[] buf = new byte[BUFFER_SIZE];
            int len;
            FileInputStream in = new FileInputStream(file);
            if (path.length() > 0)
                zos.putNextEntry(new ZipEntry(path + "/" + file.getName()));
            else
                zos.putNextEntry(new ZipEntry(file.getName()));
            while ((len = in.read(buf)) > 0) {
                zos.write(buf, 0, len);
            }
            in.close();
            return;
        }
        if (file.list() == null) {
            return;
        }
        for (String fileName : file.list()) {
            File f = new File(file.getAbsolutePath() + File.separator + fileName);
            compressFile(f, path + File.separator + file.getName(), zos);
        }
    }

    public static boolean unzip(Context context, File zipFile, String destinationPath) {
        //create target location folder if not exist
        FileHelper.createDirectory(context, null, destinationPath, true);

        try {
            ZipFile zipfile = new ZipFile(zipFile);
            int fileCount = zipfile.size();
            for (Enumeration e = zipfile.entries(); e.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) e.nextElement();
                unzipEntry(zipfile, entry, destinationPath);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void unzipEntry(ZipFile zipfile, ZipEntry entry, String outputDir) throws IOException {
        if (entry.isDirectory()) {
            createDir(new File(outputDir, entry.getName()));
            return;
        }
        File outputFile = new File(outputDir, entry.getName());
        if (!outputFile.getParentFile().exists()) {
            createDir(outputFile.getParentFile());
        }
        BufferedInputStream inputStream = null;
        BufferedOutputStream outputStream = null;
        try {
            inputStream = new BufferedInputStream(zipfile.getInputStream(entry));
            outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
            int len;
            byte buf[] = new byte[BUFFER_SIZE];
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
        } finally {
            if (inputStream != null)
                inputStream.close();
            if (outputStream != null)
                outputStream.close();
        }
    }

    private static void createDir(File dir) {
        if (dir.exists()) {
            return;
        }
        if (!dir.mkdirs()) {
            throw new RuntimeException("Can not create dir " + dir);
        }
    }
}