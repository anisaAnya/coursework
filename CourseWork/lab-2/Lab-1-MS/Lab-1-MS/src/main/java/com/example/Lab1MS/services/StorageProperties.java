package com.example.Lab1MS.services;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("services")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String location = "upload-dir/test";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
