package group3.p3network;

import java.io.*;

public class Video {
    File videoFile;
    long size;
    String filename;
    Directory directory;

    public Video(String filename) {
        this.videoFile = new File(filename);
        try {
            this.size = this.videoFile.length();
        } catch (SecurityException e) {
            System.err.println(e.getMessage());
        }
        this.filename = this.videoFile.getName();
        this.directory = new Directory();

        try {
            readVideoFile();
        }catch (IOException e) {
            System.err.println(e.getMessage());
        }

        System.out.println(this);
    }

    private void readVideoFile() throws IOException {
        FileInputStream inputStream = new FileInputStream(videoFile);
        inputStream.close();
    }

    @Override
    public String toString() {
        return "Video{" +
            "videoFile=" + videoFile +
            ", size=" + size +
            '}';
    }
}
