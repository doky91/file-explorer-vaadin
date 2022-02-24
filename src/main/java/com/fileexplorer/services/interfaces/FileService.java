package com.fileexplorer.services.interfaces;

import com.fileexplorer.entity.FileDetails;

import java.io.IOException;

public interface FileService {

    boolean createFile(FileDetails fileDetails) throws IOException;

    String readFile(String path);

    boolean updateFile(FileDetails fileDetails) throws IOException;

    boolean deleteFile(FileDetails fileDetails);
}
