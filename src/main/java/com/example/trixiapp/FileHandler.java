package com.example.trixiapp;


import java.io.*;
import java.net.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileHandler {
    public static String downloadFile(String fileUrl, String targetDir)  {
        Path targetDirPath = Paths.get(targetDir);

        if (!Files.exists(targetDirPath))
           throw new IllegalArgumentException("Directory does not exist");

        try{
            URL url = new URI(fileUrl).toURL();

            String targetFile = "Download_" + System.currentTimeMillis();
            String targetPath = targetDirPath.resolve(targetFile).toString();

            try (InputStream inputStream = url.openStream();
                 ReadableByteChannel readableByteChannel = Channels.newChannel(inputStream);
                 FileOutputStream fileOutputStream = new FileOutputStream(targetPath)) {
                fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

                return targetPath;
            }
            catch (IOException e) {
                throw new RuntimeException("Error downloading file",e);
            }
        } catch (MalformedURLException | URISyntaxException e){
            throw new RuntimeException("Invalid URL", e);
        }
    }
    public static List<String> unzipFile(String zippedFile, String destDir) {
        Path targetDirPath = Paths.get(destDir);
        Path zippedFilePath = Paths.get(zippedFile);

        if (!Files.exists(targetDirPath))
            throw new IllegalArgumentException("Directory does not exist");

        if (!Files.exists(zippedFilePath))
            throw new IllegalArgumentException("Zip File does not exist");

        List<String> extractedFilePaths = new ArrayList<>();
        try{
        byte[] buffer = new byte[2048];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(zippedFile));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            if (!zipEntry.isDirectory()) {
                String newFile = Paths.get(destDir, zipEntry.getName()).toString();;
                FileOutputStream fos = new FileOutputStream(newFile);
                int bytesRead;
                while ((bytesRead = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, bytesRead);
                }
                fos.close();
                extractedFilePaths.add(newFile);
            }
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
        } catch (IOException e){
            throw new RuntimeException("Error during unzipping file",e);
        }
        return extractedFilePaths;
    }



}
