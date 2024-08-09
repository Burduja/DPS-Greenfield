package GRPC;

import Robot.CleaningRobot;
import com.robot.grpc.RobotService;
import io.grpc.stub.StreamObserver;
import com.robot.grpc.MechanicServiceGrpc;
import com.robot.grpc.RobotService.MechanicRequest;
import com.robot.grpc.RobotService.MechanicResponse;
import com.robot.grpc.RobotService.AcknowledgeMechanicRequest;
import com.robot.grpc.RobotService.AcknowledgeMechanicResponse;
//import com.robot.grpc.RobotService.MechanicReleaseRequest;
//import com.robot.grpc.RobotService.MechanicReleaseResponse;

public class MechanicServiceImpl extends MechanicServiceGrpc.MechanicServiceImplBase {
    private final CleaningRobot cleaningRobot;

    public MechanicServiceImpl(CleaningRobot cleaningRobot) {
        this.cleaningRobot = cleaningRobot;
    }

    @Override
    public void requestMechanic(MechanicRequest request, StreamObserver<MechanicResponse> responseObserver) {
        long requestTimestamp = Long.parseLong(request.getTimestamp());
        int requestingRobotId = request.getRobotId();
        String requestLamportTimestamp = request.getLamportTimestamp();

        System.out.println("[GRPC - MECHANIC REQUEST] Received from Robot ID: " + requestingRobotId + " at Timestamp: " + requestTimestamp);
        // Immediately acknowledge the request. Actual permission is based on ACKs, not this response.
        responseObserver.onNext(MechanicResponse.newBuilder().setReceived(true).build());
        responseObserver.onCompleted();

        // Decide whether to grant access, defer, or immediately send ACK based on internal logic
        cleaningRobot.handleMechanicRequest(requestingRobotId, requestTimestamp, requestLamportTimestamp);
    }

    @Override
    public void acknowledgeMechanic(AcknowledgeMechanicRequest request, StreamObserver<AcknowledgeMechanicResponse> responseObserver) {
        int acknowledgingRobotId = request.getRobotId();
        String acknowledgingTime = request.getLamportTimestamp();
        System.out.println("[GRPC - MECHANIC ACK] Received ACK from Robot ID: " + acknowledgingRobotId);

        // Notify the robot instance that an acknowledgment has been received


        // Acknowledge the receipt of the ACK message back to the sender
        AcknowledgeMechanicResponse response = AcknowledgeMechanicResponse.newBuilder()
                .setReceived(true)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

        cleaningRobot.receiveAcknowledgement(acknowledgingRobotId, acknowledgingTime);
    }






}


