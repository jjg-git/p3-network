package group3.p3network;

import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
                try {
                    Producer.this.stop();
                } catch (InterruptedException e) {
                    if (server != null) {
                        server.shutdownNow();
                    }
                } finally {
                    executor.shutdown();
                }
            }
        });
    }

    private void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args)
        throws IOException, InterruptedException {
        final Producer producer = new Producer();
        producer.start(2);
        producer.blockUntilShutdown();
    }
}
