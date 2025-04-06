package group3.p3network;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;

public class Server {
public class Producer {
    public static void main(String[] args){
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
