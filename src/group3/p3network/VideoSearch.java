package group3.p3network;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

public class VideoSearch {
    public ArrayList<Video> gatherVideos(Directory path) {
        ArrayList<Video> videos = new ArrayList<>();

        ArrayList<Path> gatheredVideoFiles = new ArrayList<>();

        for (String result : new File(path.getCurrentDirectory()).list()) {
            System.out.println(result);
        }


        return videos;
    }
}
