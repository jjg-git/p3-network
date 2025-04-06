package group3.p3network;

import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;

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
        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
