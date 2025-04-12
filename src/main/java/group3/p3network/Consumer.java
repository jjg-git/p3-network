package group3.p3network;

import io.grpc.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

public class Consumer extends Application {
    private SendingVideoServiceGrpc.SendingVideoServiceBlockingStub
        blockingStub = null;

    public Consumer(ManagedChannel channel) {
        this.blockingStub = SendingVideoServiceGrpc.newBlockingStub(channel);
    }

    public void getFiles(ConsumerConfig setting, Iterator<VideoInfo> videos) {
        // Use a fixed thread pool with, say, 4 threads (tweak as needed)
        ExecutorService executor =
            Executors.newFixedThreadPool(setting.threads());
        List<Future<?>> futures = new ArrayList<>();

        while (videos.hasNext()) {
            VideoInfo info = videos.next();

            // Submit each video download as a separate task
            Future<?> future = executor.submit(() -> {
                System.out.println("Reading " + info.getFilename() +
                        " (" + info.getFilesize() + ")...");

                String filename = setting.output() + info.getFilename();
                try (FileOutputStream os = new FileOutputStream(filename)) {
                    Iterator<VideoData> streamedData = blockingStub.sendVideo(info);
                    while (streamedData.hasNext()) {
                        VideoData data = streamedData.next();
                        os.write(data.getData().toByteArray());
                    }
                } catch (Exception e) {
                    System.err.println("Error downloading " + info.getFilename() + ": " + e.getMessage());
                }
            });

            futures.add(future);
        }

        // Optional: Wait for all tasks to finish
        for (Future<?> future : futures) {
            try {
                future.get(); // This will throw if any task failed
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
    }

    public Iterator<VideoInfo> getVideos() {
        return blockingStub.listVideo(Commands.ListVideos);
    }

    @Override
    public void start(Stage stage){
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        Scene mainScene = new Scene(new StackPane(l), 640, 480);
        stage.setScene(mainScene);
        stage.show();
    }

    public static void main(String[] args){
        launch();
    }

    private static void connectGrpc(String[] args) throws InterruptedException {
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
