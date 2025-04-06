package group3.p3network;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class SendingVideoService
    extends SendingVideoServiceGrpc.SendingVideoServiceImplBase {

    private String videosPath = "server-dir/";

    @Override
    public void sendVideo(
        VideoInfo request,
        StreamObserver<VideoData> responseObserver
    ) {
        List<Video> listOfVideos =
            new VideoSearch(new Directory(videosPath)).gatherVideos();

        listOfVideos.forEach(System.out::println);
        File firstVideoFile = listOfVideos.get(0).getVideoFile();

        System.out.println(firstVideoFile);
        System.out.println(firstVideoFile.length());

        FileInputStream inputStream = null;
        try {
            inputStream =
                new FileInputStream(firstVideoFile);

            byte[] bytesStream = new byte[2048];

            while (inputStream.read(bytesStream) != -1) {
                VideoData byteVideo = VideoData.newBuilder()
                    .setData(ByteString.copyFrom(bytesStream))
                    .build();
                responseObserver.onNext(byteVideo);
            }

            inputStream.close();
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        responseObserver.onCompleted();

    }

    @Override
    public void listVideo(Command request, StreamObserver<VideoInfo> responseObserver) {
        if (request.equals(Commands.ListVideos)) {
            System.out.println("very cool");
        }

        for (Video video :
            new VideoSearch(new Directory(videosPath)).gatherVideos()){
            String fileName = video.getFilename();
            long fileSize = video.getSize();

            responseObserver.onNext(
                VideoInfo.newBuilder()
                    .setFilename(fileName)
                    .setFilesize(fileSize)
                    .build()
            );
        }

        responseObserver.onCompleted();
    }
}
