package com.fileexplorer.entity;

import javax.persistence.Entity;

@Entity
public class FileDetails {

    private String path;
    private String name;
    private String content;

    public FileDetails(String path, String name, String content) {
        this.path = path;
        this.name = name;
        this.content = content;
    }

    public FileDetails() {
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFilePath() {
        return path + System.getProperty("file.separator") + name;
    }
}
