package group3.p3network;

public class Server {
    public static void main(String[] args){
        new VideoSearch(new Directory("server-dir/")).gatherVideos();
    }
}
