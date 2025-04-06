package group3.p3network;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;

public class SendingVideoService
    extends SendingVideoServiceGrpc.SendingVideoServiceImplBase {
    @Override
    public void sendVideo(
        VideoInfo request,
        StreamObserver<VideoData> responseObserver
    ) {
        System.out.println(request);
        responseObserver.onNext(
            VideoData.newBuilder()
                .setData(ByteString.EMPTY)
                .build()
        );
        responseObserver.onCompleted();
    }
}
