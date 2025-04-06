package group3.p3network;

import io.grpc.*;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class Consumer {
    private final SendingVideoServiceGrpc.SendingVideoServiceBlockingStub
        blockingStub;

    public Consumer(ManagedChannel channel) {
        this.blockingStub = SendingVideoServiceGrpc.newBlockingStub(channel);
    }

    public void test() {
        VideoInfo info = VideoInfo.newBuilder()
            .setFilesize(69)
            .setFilename("gege_akutami.txt")
            .build();

        Iterator<VideoData> streamedData;
        try {
            streamedData = blockingStub.sendVideo(info);
            for (int i = 0; streamedData.hasNext(); i++) {
                VideoData data = streamedData.next();
                System.out.println("[" + i + "]: " + data.getData());
            }
        } catch (StatusRuntimeException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String target = "localhost:50051";
        if (args.length > 0) {
            if (args.length == 1 && args[0].equals("--help")) {
                System.err.println("Syntax: hostname:port");
                System.err.println("");
                System.err.println("   hostname  localhost or ip address in " +
                    "the format of XXX.XXX.XXX.XXX");
                System.err.println("   port      port number");
                System.exit(1);
            }
            target = args[0];
        }

        ManagedChannel channel = Grpc.newChannelBuilder(
            target,
            InsecureChannelCredentials.create()
        ).build();

        try {
            Consumer consumer = new Consumer(channel);
            consumer.test();
        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
