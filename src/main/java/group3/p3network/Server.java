package group3.p3network;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;

public class Server {
    public static void main(String[] args){
        new VideoSearch(new Directory("server-dir/")).gatherVideos();
    }

    private static class SendingVideoService
        extends SendingVideoServiceGrpc.SendingVideoServiceImplBase {
        @Override
        public void sendVideo(
            VideoInfo request,
            StreamObserver<VideoData> responseObserver
        ) {

            responseObserver.onCompleted();
        }
    }
}
