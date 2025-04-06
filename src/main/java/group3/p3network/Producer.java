package group3.p3network;

import io.grpc.Server;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Producer {
    private final Server server;
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
