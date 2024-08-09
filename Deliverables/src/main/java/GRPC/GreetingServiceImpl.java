package GRPC;

import Robot.CleaningRobot;
import Robot.RobotsList;
import com.robot.grpc.RobotService.*;
import io.grpc.stub.StreamObserver;
import com.robot.grpc.GreetServiceGrpc.GreetServiceImplBase;

public class GreetingServiceImpl extends GreetServiceImplBase {
    private final CleaningRobot cleaningRobot;

    public GreetingServiceImpl(CleaningRobot cleaningRobot) {
        this.cleaningRobot = cleaningRobot;
    }

    @Override
    public void greet(GreetingRequest req, StreamObserver<GreetingResponse> responseObserver) {
        System.out.println("[GRPC_GREETING] greeting received by " + req.getId());
        // Extract robot info from request
        CleaningRobot newCleaningRobot = new CleaningRobot(req.getId(), "localhost", req.getPort(), parsePosition(req.getPosition()));
        newCleaningRobot.setDistrict(req.getDistrict());
        // Add the new robot to the list
        //((robotsList.addRobot(newCleaningRobot);
        String answerLamportTime = cleaningRobot.handleGreeting(newCleaningRobot, req.getLamportTime());
        System.out.println("received lamport time " + req.getLamportTime());
        System.out.println("answering with lamport time " + answerLamportTime);
        // TODO if i'm requesting the mechanic when a robot is joining, send the request to him too and wait for his ack
        // Prepare and send the response asynchronously
        GreetingResponse response = GreetingResponse.newBuilder()
                .setReceived(true)

                .setLamportTime(answerLamportTime)
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
