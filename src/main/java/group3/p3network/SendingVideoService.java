package group3.p3network;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.function.BiConsumer;

public class SendingVideoService
    extends SendingVideoServiceGrpc.SendingVideoServiceImplBase {

    private final String videosPath = "producer-dir/";

    @Override
    public void sendVideo(
        VideoInfo request,
        StreamObserver<VideoData> responseObserver
    ) {
        VideoSearch videoSearch =
            new VideoSearch(new Directory(videosPath));

        Map<String, Video> videoMap = videoSearch.gatherVideos();
        Video video = videoMap.get(request.getFilename());

        System.out.println("Sending " + video.getVideoFile() + "...");
        FileInputStream inputStream;
        try {
            inputStream =
                new FileInputStream(video.getVideoFile());

            byte[] bytesStream = new byte[2048];

            while (inputStream.read(bytesStream) != -1) {
                VideoData byteVideo = VideoData.newBuilder()
                    .setData(ByteString.copyFrom(bytesStream))
                    .build();
                responseObserver.onNext(byteVideo);
            }

            inputStream.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        responseObserver.onCompleted();

    }

    @Override
    public void listVideo(Command request, StreamObserver<VideoInfo> responseObserver) {
        if (request.equals(Commands.ListVideos)) {
            Map<String, Video> videoMap=
                new VideoSearch(new Directory(videosPath)).gatherVideos();

            videoMap.forEach(new BiConsumer<String, Video>() {
                @Override
                public void accept(String filename, Video video) {
                    // System.out.println(video.getDirectory()
                    // .getCurrentDirectory());
                    String fileName = video.getFilename();
                    long fileSize = video.getSize();

                    responseObserver.onNext(
                        VideoInfo.newBuilder()
                            .setFilename(fileName)
                            .setFilesize(fileSize)
                            .build()
                    );
                }
            });
        }

        responseObserver.onCompleted();
    }
}
