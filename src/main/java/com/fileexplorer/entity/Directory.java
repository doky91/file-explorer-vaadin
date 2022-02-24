package com.fileexplorer.entity;

import javax.persistence.Entity;

@Entity
public class Directory {

    private String name;
    private String path;

    public Directory(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

