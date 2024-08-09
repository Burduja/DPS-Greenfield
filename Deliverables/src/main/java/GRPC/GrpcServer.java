package GRPC;

import Robot.CleaningRobot;

import java.io.IOException;
import io.grpc.Server;
import io.grpc.ServerBuilder;

public class GrpcServer extends Thread{
    private CleaningRobot cleaningRobot;
    private Server server;

    public GrpcServer(CleaningRobot cleaningRobot) {
        this.cleaningRobot = cleaningRobot;
    }

    public void run(){
        server = ServerBuilder.forPort(cleaningRobot.getPort())
                //.addService(new RobotCommunicationImpl(robot.getRobotsList()))
                .addService(new HeartbeatServiceImpl(cleaningRobot.getRobotsList()))
                .addService(new GreetingServiceImpl(cleaningRobot))
                .addService(new LeavingServiceImpl(cleaningRobot))
                .addService(new MechanicServiceImpl(cleaningRobot))
                .addService(new RelocationServiceImpl(cleaningRobot))
                .build();

        try {
            server.start();
            System.out.println("GRPC server started");
        } catch (IOException e) {
            System.out.println("ERROR WHILE STARTING GRPC SERVER");
            e.printStackTrace();
        }

        try {
            server.awaitTermination();
        } catch (InterruptedException e) {
            server.shutdown();
            System.out.println("GRPC server stopped ");
        }
    }

}
