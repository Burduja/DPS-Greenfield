package GRPC;

import Robot.CleaningRobot;
import com.robot.grpc.RelocationServiceGrpc;
import com.robot.grpc.RobotService;
import com.robot.grpc.RobotService.RelocationRequest;
import com.robot.grpc.RobotService.RelocationResponse;
import com.robot.grpc.RobotService.AcknowledgeRelocationRequest;
import com.robot.grpc.RobotService.AcknowledgeRelocationResponse;
import com.robot.grpc.RobotService.MovingRequest;
import com.robot.grpc.RobotService.MovingResponse;
import io.grpc.stub.StreamObserver;

public class RelocationServiceImpl extends RelocationServiceGrpc.RelocationServiceImplBase {
    private final CleaningRobot cleaningRobot;

    public RelocationServiceImpl(CleaningRobot cleaningRobot) {
        this.cleaningRobot = cleaningRobot;
    }

    @Override
    public void requestRelocation(RelocationRequest request, StreamObserver<RelocationResponse> responseObserver){
        int relocatingRobotId = request.getRobotId();
        long requestTimestamp = Long.parseLong(request.getTimestamp());
        String lamportRequestTimestamp = request.getLamportTimestamp();


        RobotService.RelocationResponse response = RobotService.RelocationResponse.newBuilder()
                .setReceived(true)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

        cleaningRobot.handleRelocatingRequest(relocatingRobotId, requestTimestamp, lamportRequestTimestamp);

    }

    @Override
    public void acknowledgeRelocation(AcknowledgeRelocationRequest request, StreamObserver<AcknowledgeRelocationResponse> responseObserver){
        int acknowledgingRobotId = request.getRobotId();
        String acknowlegingLamportTimestam = request.getLamportTimestamp();

        // Notify the robot instance that an acknowledgment has been received


        // Acknowledge the receipt of the ACK message back to the sender
        RobotService.AcknowledgeRelocationResponse response = RobotService.AcknowledgeRelocationResponse.newBuilder()
                .setReceived(true)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

        cleaningRobot.receiveRelocationAcknowledgement(acknowledgingRobotId, acknowlegingLamportTimestam);


    }

    @Override
    public void moving(MovingRequest request, StreamObserver<MovingResponse> responseObserver){
        int movingRobotId = request.getRobotId();
        int posX = request.getNewPosX();
        int posY = request.getNewPosY();
        int district = request.getNewDistrict();
        String lamportTime = request.getLamportTimestamp();

        // Acknowledge the receipt of the ACK message back to the sender
        MovingResponse response = MovingResponse.newBuilder()
                .setReceived(true)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

        cleaningRobot.receiveMoving(movingRobotId, posX, posY, district, lamportTime);
    }
}
