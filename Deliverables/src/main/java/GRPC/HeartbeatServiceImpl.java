package GRPC;

import Robot.RobotsList;
import com.robot.grpc.HeartbeatServiceGrpc;
import com.robot.grpc.HeartbeatServiceGrpc.*;


import com.robot.grpc.RobotService.*;
import io.grpc.stub.StreamObserver;

public class HeartbeatServiceImpl extends HeartbeatServiceImplBase {
    private RobotsList robotsList;

    public HeartbeatServiceImpl(RobotsList robotsList) {
        this.robotsList = robotsList;
    }

    @Override
    public void sendHeartbeat(HeartbeatRequest request, StreamObserver<HeartbeatResponse> responseObserver) {
        System.out.println("[GRPC_HEARTBEAT] heartbeat received from robot " + request.getRobotId());
        //robotsList.updateLastHeartbeatTimestamp(request.getRobotId());
        HeartbeatResponse response = HeartbeatResponse.newBuilder()
                .setReceived(true)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}


