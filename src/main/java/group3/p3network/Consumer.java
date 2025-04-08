package group3.p3network;

import io.grpc.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class Consumer {
    private static String directory = "consumer-dir/";
    private static String target = "localhost:50051";
    private final static String defaultDirectory = directory;

    private final SendingVideoServiceGrpc.SendingVideoServiceBlockingStub
        blockingStub;

    public Consumer(ManagedChannel channel) {
        this.blockingStub = SendingVideoServiceGrpc.newBlockingStub(channel);
    }

    public void getFiles(Iterator<VideoInfo> videos) {
        Iterator<VideoData> streamedData;
        for (Iterator<VideoInfo> it = videos; it.hasNext(); ) {
            VideoInfo info = it.next();
            System.out.println("Reading " + info.getFilename() +
                "(" + info.getFilesize() + ")...");

            try {
                FileOutputStream os = new FileOutputStream(info.getFilename());

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

    public Iterator<VideoInfo> getVideos() {
        return blockingStub.listVideo(Commands.ListVideos);
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length > 0) {
            handleArgs(args);
        }

        if (!checkDirectory()) {
            System.err.println("Cannot find the directory " +
                "named \"" + directory + "\".");

            makeDirectory();
            System.exit(1);
        } else {
            System.out.println("Found \"" + directory + "\" directory.");
            // System.exit(0);
        }

        ManagedChannel channel = Grpc.newChannelBuilder(
            target,
            InsecureChannelCredentials.create()
        ).build();

        try {
            Consumer consumer = new Consumer(channel);
            consumer.getFiles(consumer.getVideos());
        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    private static void handleArgs(String[] args) {
        if (args.length == 1 && args[0].equals("--help")) {
            System.err.println("Syntax: hostname:port [output]");
            System.err.println("");
            System.err.println("    hostname  localhost or ip address in " +
                "the format of XXX.XXX.XXX.XXX");
            System.err.println("    port      port number");
            System.err.println("    output    name of the directory");
            System.exit(1);
        }
        target = args[0];
        if (args.length > 1) {
            directory = args[1];
        }
    }

    private static void makeDirectory() {
        System.err.println("Creating a directory named \"consumer-dir\" " +
            "where the videos will be downloaded.");

    }

    private static boolean checkDirectory() {
        return new File(directory).exists();
    }
}
