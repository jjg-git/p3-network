package group3.p3network;

import io.grpc.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class Consumer {
    private final SendingVideoServiceGrpc.SendingVideoServiceBlockingStub
        blockingStub;

    public Consumer(ManagedChannel channel) {
        this.blockingStub = SendingVideoServiceGrpc.newBlockingStub(channel);
    }

    public void getFiles(ConsumerConfig setting, Iterator<VideoInfo> videos) {
        Iterator<VideoData> streamedData;
        while (videos.hasNext()) {
            VideoInfo info = videos.next();

            System.out.println("Reading " + info.getFilename() +
                "(" + info.getFilesize() + ")...");

            try {
                String filename = setting.output() + info.getFilename();
                FileOutputStream os = new FileOutputStream(filename);

                streamedData = blockingStub.sendVideo(info);
                for (int i = 0; streamedData.hasNext(); i++) {
                    VideoData data = streamedData.next();
                    os.write(data.getData().toByteArray());
                }

                os.close();
            } catch (StatusRuntimeException | IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public Iterator<VideoInfo> getVideos() {
        return blockingStub.listVideo(Commands.ListVideos);
    }

    public static void main(String[] args) throws InterruptedException {
        ConsumerConfig setting = new SetupConsumerConfig().setup(args);

        ManagedChannel channel = Grpc.newChannelBuilder(
            setting.target(),
            InsecureChannelCredentials.create()
        ).build();

        try {
            Consumer consumer = new Consumer(channel);
            consumer.getFiles(setting, consumer.getVideos());
        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }

}
