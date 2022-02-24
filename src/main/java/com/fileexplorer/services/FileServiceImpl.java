package com.fileexplorer.services;

import com.fileexplorer.entity.FileDetails;
import com.fileexplorer.services.interfaces.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    private static final String USER_HOME_DIRECTORY = System.getProperty("user.home");

    @Override
    public boolean createFile(FileDetails fileDetails) throws IOException {

        Path fileToCreatePath = Paths.get(fileDetails.getFilePath());

        try {
            if (!Files.exists(fileToCreatePath)) {
                Files.createFile(fileToCreatePath);
                log.info("New file created: " + fileToCreatePath);
                return true;
            }

            log.info("File already exits: " + Files.exists(fileToCreatePath));
        } catch (IOException exception) {
            log.info("Creation of file went wrong. Please try again.");
        }

        return false;
    }

    @Override
    public String readFile(String path) {

        String fileContent = "";
        Path filePath = null;
        if (path != null) {
            filePath = Paths.get(path);
        } else filePath = Paths.get(USER_HOME_DIRECTORY);

        try {
            fileContent = Files.readString(filePath);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return fileContent;
    }

    @Override
    public boolean updateFile(FileDetails fileDetails) throws IOException {

        Path filePath = Paths.get(fileDetails.getFilePath());

        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
            writer.write(fileDetails.getContent());
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean deleteFile(FileDetails fileDetails) {

        try {
            Files.delete(Paths.get(fileDetails.getFilePath()));
            log.info("File is deleted.");
            return true;
        } catch (NoSuchFileException ex) {
            log.info("The file you want to delete doesn't exist");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
