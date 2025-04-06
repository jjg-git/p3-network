package group3.p3network;

import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Producer {
    private Server server;

    private void start(int producerThreads) throws IOException {
        final int port = 50051;

        ExecutorService executor =
            Executors.newFixedThreadPool(producerThreads);

        server = Grpc.newServerBuilderForPort(
            port,
            InsecureServerCredentials.create()
        )
            .executor(executor)
            .addService(new SendingVideoService())
            .build()
            .start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                super.run();
            }
        });
    }

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
