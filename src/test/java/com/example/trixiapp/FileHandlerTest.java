package com.example.trixiapp;

import net.lingala.zip4j.ZipFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.FileSystemUtils;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileHandlerTest {
    private String testDir;

    @BeforeEach
    void setUp() throws IOException {
        testDir = Files.createTempDirectory("TestDirectory").toString();

    }

    @AfterEach
    void tearDown() throws IOException {
        FileSystemUtils.deleteRecursively(Paths.get(testDir));

    }

    @Test
    void downloadFileInvalidUrl() {
        String invalidUrl = "Invalid Url";

        assertThrows(RuntimeException.class, () -> FileHandler.downloadFile(invalidUrl, testDir));
    }

    @Test
    void downloadFileInvalidTargetPath() {
        String fileUrl = "https://www.smartform.cz/download/kopidlno.xml.zip";
        String invalidPath = "Invalid Path";

        assertThrows(IllegalArgumentException.class, () -> FileHandler.downloadFile(fileUrl, invalidPath));

    }

    @Test
    void downloadFile() {
        String fileUrl = "https://www.smartform.cz/download/kopidlno.xml.zip";
        String downloadedFilePath = FileHandler.downloadFile(fileUrl, testDir);

        assertTrue(Files.exists(Path.of(downloadedFilePath)));
    }

    @Test
    void unzipFileInvalidFile() {
        String filePath = "Invalid File";
        assertThrows(IllegalArgumentException.class, () -> FileHandler.unzipFile(filePath, testDir));


    }
    @Test
    void unzipFileInvalidDirectory() throws IOException {
        Path testDirPath = Paths.get(testDir);
        String fileName = "filename.txt";
        String filePath = testDirPath.resolve(fileName).toString();

        PrintWriter writer = new PrintWriter(filePath);
        writer.println("Hello World");
        writer.close();

        String fileToZip = "zippedfile.zip";
        String zipPath = testDirPath.resolve(fileToZip).toString();

        String invalidDir = "Invalid Directory" ;

        try (ZipFile zipFile = new ZipFile(zipPath)) {
            zipFile.addFile(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }


        assertThrows(IllegalArgumentException.class, () -> FileHandler.unzipFile(zipPath, invalidDir));
    }

    @Test
    void unzipFile() throws IOException {
        Path testDirPath = Paths.get(testDir);
        String fileName = "filename.txt";
        String filePath = testDirPath.resolve(fileName).toString();

        PrintWriter writer = new PrintWriter(filePath);
        String textInFile = "Hello World";
        writer.println(textInFile);
        writer.close();

        String fileToZip = "zippedfile.zip";
        String zipPath = testDirPath.resolve(fileToZip).toString();

        try (ZipFile zipFile = new ZipFile(zipPath)) {
            zipFile.addFile(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> unzippedFiles = FileHandler.unzipFile(zipPath, testDir);
        Path unzippedFile = Paths.get(unzippedFiles.get(0));

        char[] fileContent = new char[textInFile.length()];
        FileReader reader = new FileReader(unzippedFile.toString());
        reader.read(fileContent);
        reader.close();

        assertEquals(1,unzippedFiles.size());
        assertTrue(Files.exists(unzippedFile));
        assertEquals(textInFile, new String(fileContent));



    }
}