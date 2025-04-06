package group3.p3network;

import java.io.*;

public class Video {
    private File videoFile;
    private long size;
    private String filename;
    private Directory directory;

    public Video(String filename) {
        this.videoFile = new File(filename);

        try {
            this.size = this.videoFile.length();
        } catch (SecurityException e) {
            System.err.println(e.getMessage());
        }

        this.filename = this.videoFile.getName();
        this.directory = new Directory(this.videoFile.getParent());

        // System.out.println(this);
    }

    public File getVideoFile() {
        return videoFile;
    }

    public long getSize() {
        return size;
    }

    public String getFilename() {
        return filename;
    }

    public Directory getDirectory() {
        return directory;
    }

    @Override
    public String toString() {
        return "Video{" +
            "directory=" + directory.getCurrentDirectory() +
            "videoFile=" + videoFile +
            ", size=" + size +
            ", filename=" + filename +
            ", path=" + videoFile.getPath() +
            '}';
    }
}
