package GRPC;

import Robot.CleaningRobot;
import Robot.RobotsList;
import com.robot.grpc.LeavingServiceGrpc;
import com.robot.grpc.RobotService;
import io.grpc.stub.StreamObserver;

public class LeavingServiceImpl extends LeavingServiceGrpc.LeavingServiceImplBase {
    private final CleaningRobot cleaningRobot;

    public LeavingServiceImpl(CleaningRobot cleaningRobot) {
        this.cleaningRobot = cleaningRobot;
    }

    @Override
    public void leave(RobotService.LeavingRequest req, StreamObserver<RobotService.LeavingResponse> responseObserver) {
        //System.out.println("[GRPC_LEAVING] " + req.getId() + " is leaving");
        // Add the new robot to the list
        //robotsList.removeRobot(cleaningRobot.getId());
        cleaningRobot.receiveLeaving(req.getId(), req.getDistrict());
        // Prepare the response
        RobotService.LeavingResponse response = RobotService.LeavingResponse.newBuilder()
                .setReceived(true)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }



    private int[] parsePosition(String positionString) {
        // Parse the position string and return int array
        // Assume the format is "[x, y]"
        String[] parts = positionString.replace("[", "").replace("]", "").split(",");
        return new int[] { Integer.parseInt(parts[0].trim()), Integer.parseInt(parts[1].trim()) };
    }
}
