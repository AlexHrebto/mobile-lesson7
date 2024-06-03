package ru.mirea.khrebtovsky.mireaproject;

public class FileItem {

    private String fileName;
    private String fileHash;

    public FileItem(String fileName, String fileHash) {
        this.fileName = fileName;
        this.fileHash = fileHash;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileHash() {
        return fileHash;
    }
}

