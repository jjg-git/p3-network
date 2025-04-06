package group3.p3network;

import io.grpc.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class Consumer {
    private final SendingVideoServiceGrpc.SendingVideoServiceBlockingStub
        blockingStub;

    public Consumer(ManagedChannel channel) {
        this.blockingStub = SendingVideoServiceGrpc.newBlockingStub(channel);
    }

    public void getFiles(ArrayList<String> videos) {
        Iterator<VideoData> streamedData;
        for (String videoFilename : videos) {
            VideoInfo info = VideoInfo.newBuilder().setFilename(videoFilename).build();

            try {
                FileOutputStream os = new FileOutputStream(videoFilename);

                streamedData = blockingStub.sendVideo(info);
                for (int i = 0; streamedData.hasNext(); i++) {
                    VideoData data = streamedData.next();
                    os.write(data.getData().toByteArray());
                }

                os.close();
            } catch (StatusRuntimeException e) {
                System.err.println(e.getMessage());
            } catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public ArrayList<String> getVideos() {
        ArrayList<String> listFilenames = new ArrayList();

        blockingStub.listVideo(Commands.ListVideos).forEachRemaining(
            videoInfo -> {
                listFilenames.add(videoInfo.getFilename());
            }
        );

        return listFilenames;
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

        System.out.println("test");
        System.out.println(FileSystems.getDefault().getPath(""));

        ManagedChannel channel = Grpc.newChannelBuilder(
            target,
            InsecureChannelCredentials.create()
        ).build();

        try {
            Consumer consumer = new Consumer(channel);
            // consumer.getVideos().forEach(System.out::println);
            consumer.getFiles(consumer.getVideos());
        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
