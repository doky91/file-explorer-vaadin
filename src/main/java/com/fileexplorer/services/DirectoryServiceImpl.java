package com.fileexplorer.services;

import com.fileexplorer.entity.Directory;
import com.fileexplorer.services.interfaces.DirectoryService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;

@Service
public class DirectoryServiceImpl implements DirectoryService {

    private static final String USER_HOME_DIRECTORY = System.getProperty("user.home");

    @Override
    public ArrayList<Directory> listAll() {

        File file = new File(USER_HOME_DIRECTORY);
        String[] fileList = file.list();
        ArrayList<Directory> allFiles = new ArrayList<>();

        for (String str : fileList) {
            allFiles.add(new Directory(str, USER_HOME_DIRECTORY));
        }

        return allFiles;
    }
}
