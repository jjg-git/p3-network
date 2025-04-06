package group3.p3network;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

public class VideoSearch {
    Directory path;
    public VideoSearch(Directory path) {
        this.path = path;
        System.out.println("path = " + path.getCurrentDirectory());
    }

    public ArrayList<Video> gatherVideos() {
        ArrayList<Video> videos = new ArrayList<>();

        ArrayList<Path> gatheredVideoFiles = new ArrayList<>();

        for (String result : new File(this.path.getCurrentDirectory()).list()) {
            videos.add(new Video(result));
        }

        return videos;
    }
}
