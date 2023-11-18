package com.example.trixiapp;

import com.example.trixiapp.parser.MunicipalityParser;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@AllArgsConstructor
@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
    private final MunicipalityParser municipalityParser;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Program started...");
        String fileUrl = "https://www.smartform.cz/download/kopidlno.xml.zip";
        String tempDir = Files.createTempDirectory("TempDirectory").toString();

        String downloadedFile = FileHandler.downloadFile(fileUrl, tempDir);

        List<String> unzippedFiles = FileHandler.unzipFile(downloadedFile, tempDir);

        for (String file: unzippedFiles){
            municipalityParser.parseMunicipality(file);
            municipalityParser.parsePartOfMunicipality(file);
        }

        FileSystemUtils.deleteRecursively(Paths.get(tempDir));
        System.out.println("Program finished.");


    }
}
