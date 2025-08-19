package ru.otus.protobuf.server.service;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proto.v1.GetSequenceRequest;
import proto.v1.GetSequenceResponse;
import proto.v1.SequenceServiceGrpc;

@SuppressWarnings({"squid:S2142", "squid:S106"})
public class RemoteSequenceService extends SequenceServiceGrpc.SequenceServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(RemoteSequenceService.class);

    @Override
    public void getSequence(GetSequenceRequest request, StreamObserver<GetSequenceResponse> responseObserver) {
        SequenceGenerator generator = createSequenceGenerator(request);

        while (generator.hasNext()) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                logger.atError().setMessage(e.getMessage()).log();
            }
            var next = generator.getNext();
            responseObserver.onNext(convertValueToResponse(next));
            logger.atInfo().setMessage("New value sent: {}").addArgument(next).log();
        }
        responseObserver.onCompleted();
    }

    private SequenceGenerator createSequenceGenerator(GetSequenceRequest request) {
        return new SequenceGeneratorImpl(request.getFirstValue(), request.getLastValue());
    }

    private GetSequenceResponse convertValueToResponse(int value) {
        return GetSequenceResponse.newBuilder().setValue(value).build();
    }
}
