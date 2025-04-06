package group3.p3network;

import java.io.File;
import java.util.HashMap;

public class VideoSearch {
    Directory path;
    public VideoSearch(Directory path) {
        this.path = path;
        // System.out.println("path = " + path.getCurrentDirectory());
    }

    public HashMap<String, Video> gatherVideos() {
        HashMap<String, Video> videos = new HashMap<>();

        for (String result : new File(this.path.getCurrentDirectory()).list()) {
            videos.put(
                result,
                new Video(path.getCurrentDirectory() + result)
            );
        }

        return videos;
    }
}
