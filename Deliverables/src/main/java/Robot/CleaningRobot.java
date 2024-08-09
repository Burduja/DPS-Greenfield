package Robot;

import GRPC.GrpcServer;
import Rest.beans.AvgMeasurement;
import Simulators.Measurement;
import Simulators.PM10Simulator;
//import com.robot.grpc.GreetingServiceGrpc;

import com.robot.grpc.*;
//import com.robot.grpc.RobotServiceOuterClass;
//import com.robot.grpc.GreetServiceOuterClass;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreType;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.sql.SQLOutput;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.robot.grpc.GreetServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CleaningRobot {

    protected int id;
    protected String ip;
    protected int port;
    protected int[] coordinates;
    protected int district;
    protected RobotsList robotsList;
    private volatile boolean running = true;

    // POLLUTION SENSOR
    private PM10Simulator pm10Simulator;
    private MeasurementBuffer measurementBuffer;
    private List<Measurement> slidingWindow = new LinkedList<>();
    private final int windowSize = 8;
    private final int overlap = windowSize / 2;

    // MQTT
    private MqttClient mqttClient;
    private String mqttTopic = "greenfield/pollution/district";

    // REST
    public static String restBaseAddressRobots = "http://localhost:9999/robots/";
    protected RestMethods restMethods;

    // MECHANIC
    private GrpcServer grpcServer;
    private boolean requestingMechanic = false;
    private boolean atMechanic = false;
    private long myRequestTimestamp = -1; // Timestamp of my request
    private Set<Integer> awaitingAcknowledgements = new HashSet<>();
    private PriorityQueue<MechanicRequest> deferredMechanicRequests = new PriorityQueue<>();

    // RELOCATION
    private boolean requestingRelocation = false;
    private boolean relocating = false;
    private long myRelocatingRequestTimestamp = -1; // Timestamp of my request
    private Set<Integer> awaitingRelocatingAcknowledgements = new HashSet<>();
    private PriorityQueue<MovingRequest> deferredRelocatingRequests = new PriorityQueue<>();

    // LAMPORT
    protected int offset = 0;
    protected long lamportTime = 0;
    protected long mechanicRequestLamportTime = -1;
    protected long relocatingRequestLamportTime = -1;

    //----------------------------------------------------------------------------
    public CleaningRobot(int id, String ip, int port, int offset){
        this.id = id;
        this.ip = ip;
        this.port = port;
        this.coordinates = new int[2];
        this.robotsList = new RobotsList(this);
        this.district = -1;
        this.restMethods = new RestMethods(this);
        this.measurementBuffer = new MeasurementBuffer();
        this.pm10Simulator = new PM10Simulator(this.measurementBuffer);
        this.pm10Simulator.start();
        this.offset = offset;
        try {
            this.mqttClient = new MqttClient("tcp://localhost:1883", "Client" + id);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        this.awaitingAcknowledgements = new HashSet<>();
        this.deferredMechanicRequests = new PriorityQueue<>();
    }
    public CleaningRobot(int id, String ip, int port, int[] coordinates){
        this.id = id;
        this.ip = ip;
        this.port = port;
        this.coordinates = coordinates;
    }

    // GETTERS SETTERS
    public void setPosX (int posX){
        this.coordinates[0] = posX;
    }
    public void setPosY (int posY){
        this.coordinates[1] = posY;
    }
    public void setCoordinates(int[] coordinates) {
        this.coordinates = coordinates;
    }
    public void setDistrict(int district) {
        this.district = district;
    }
    public int[] getCoordinates() {
        return coordinates;
    }
    public RobotsList getRobotsList() {
        return robotsList;
    }

    public int getId() {
        return id;
    }

    public int getDistrict() {
        return district;
    }
    public int getPort() {
        return port;
    }
    public String getIp() {
        return ip;
    }
    public void getRobotInfo(){
        System.out.println("my district " + district);
        System.out.println("requesting mechanic " + requestingMechanic);
        System.out.println("requesting relocation " + requestingRelocation);
        System.out.println("my lamport time " + getLamportTime());

        //System.out.println(awaitingAcknowledgements.toString());
        if(awaitingAcknowledgements.isEmpty()) {
            System.out.println("awaiting acks mechanic is empty");
        }
        else {
            System.out.println("printing awaiting acks mechanic");
            Iterator<Integer> it = awaitingAcknowledgements.iterator();
            while (it.hasNext()) {
                System.out.print(it.next() + " ");
            }

        }
        if(deferredMechanicRequests.isEmpty()) {
            System.out.println("\ndeffered mechanic is empty");
        }
        else {
            System.out.println("\nprinting deffered mechanic");
            Iterator<MechanicRequest> it = deferredMechanicRequests.iterator();
            while (it.hasNext()) {
                System.out.print(it.next().getRobotId() + " ");
            }

        }
        //System.out.println("Population per district");
        //System.out.println("D1: " +robotsList.getPopulationByDistrict(1)+ " D2: " +robotsList.getPopulationByDistrict(2)+" D3: " +robotsList.getPopulationByDistrict(3)+" D4: " +robotsList.getPopulationByDistrict(4));


    }

    // POLLUTION SENSOR - every 15 seconds send the averages to the server using MQTT
    private void processDataFromBuffer() {
        final long interval = 15000;

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            if (!running) {
                executorService.shutdown();
                return;
            }
            List<Measurement> newMeasurements = measurementBuffer.readAllAndClean();
            List<AvgMeasurement> averages = new ArrayList<>();
            //System.out.println("Got " + newMeasurements.size() + " measurements");
            for (Measurement m : newMeasurements) {
                slidingWindow.add(m);
                //System.out.println("sliding window size " + slidingWindow.size());
                if (slidingWindow.size() == windowSize) {
                    double avg = calculateAverage(slidingWindow);
                    long lastTimestamp = slidingWindow.get(slidingWindow.size() - 1).getTimestamp();
                    averages.add(new AvgMeasurement(avg, lastTimestamp));
                    // Maintain 50% overlap
                    slidingWindow.subList(0, overlap).clear();
                    //System.out.println("new sliding window size " + slidingWindow.size());
                }
            }
            if (!averages.isEmpty()) {
                try {
                    sendAveragesToServer(averages); // send list of averages
                } catch (MqttException | JSONException e) {
                    System.out.println("Exception when sending with MQTT");
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }, 0, interval, TimeUnit.MILLISECONDS);
    }
    private void sendAveragesToServer(List<AvgMeasurement> averages) throws MqttException, JSONException {

        if (!mqttClient.isConnected()) {
            System.out.println("MQTT client is not connected. Attempting to reconnect...");
            mqttClient.reconnect();
        }

        long currentTimestamp = System.currentTimeMillis();
        JSONArray jsonAverages = new JSONArray();
        for (AvgMeasurement avg : averages) {
            JSONObject jsonAvg = new JSONObject();
            jsonAvg.put("value", avg.getValue());
            jsonAvg.put("timestamp", avg.getTimestamp());
            jsonAverages.put(jsonAvg);
        }

        JSONObject payloadJson = new JSONObject();
        payloadJson.put("robot_id", this.id);
        payloadJson.put("timestamp", currentTimestamp);
        payloadJson.put("averages", jsonAverages);

        String topic = this.mqttTopic + this.district;
        mqttClient.publish(topic, new MqttMessage(payloadJson.toString().getBytes()));
    }
    private double calculateAverage(List<Measurement> measurements) {
        double sum = 0;
        for (Measurement m : measurements) {
            sum += m.getValue();
        }
        return sum / measurements.size();
    }

    // MQTT
    private void connectToMqttBroker() {
        try {
            mqttClient.connect();
            System.out.println("Connected to MQTT client");
        } catch (MqttException e) {
            System.out.println("Error connecting to MQTT broker: " + e.getMessage());
        }
    }

    // GREETING - when a robot joins the network, it greets the other robots received from the rest server using grpc
    // this is done so the robots already in the network can add the new one to their internal list and also
    // to update the lamport counter
    private synchronized void greetOtherRobots() {

        incrementLamportTime();
        long greetingLamportTime = getLamportTime();
        for (CleaningRobot cleaningRobot : this.robotsList.getRobots()) {
            ManagedChannel channel = ManagedChannelBuilder.forAddress(cleaningRobot.getIp(), cleaningRobot.getPort())
                    .usePlaintext()
                    .build();

            GreetServiceGrpc.GreetServiceStub asyncStub = GreetServiceGrpc.newStub(channel);
            RobotService.GreetingRequest request = RobotService.GreetingRequest.newBuilder()
                    .setPosition(Arrays.toString(this.coordinates))
                    .setDistrict(this.district)
                    .setId(this.id)
                    .setPort(this.port)
                    .setLamportTime(String.valueOf(greetingLamportTime))
                    .build();

            StreamObserver<RobotService.GreetingResponse> responseObserver = new StreamObserver<RobotService.GreetingResponse>() {
                @Override
                public void onNext(RobotService.GreetingResponse greetingResponse) {
                    System.out.println("[ROBOT - GREET] Received response: " +  " lamport time" + greetingResponse.getLamportTime());
                    updateLamportTime(Long.parseLong(greetingResponse.getLamportTime()));
                }

                @Override
                public void onError(Throwable throwable) {
                    System.err.println("[ROBOT - GREET] Error in greeting response: " + throwable.getMessage());
                    safelyShutdownChannel(channel);
                }

                @Override
                public void onCompleted() {
                    safelyShutdownChannel(channel);
                }
            };

            asyncStub.greet(request, responseObserver);
        }
    }

    public String handleGreeting(CleaningRobot cleaningRobot, String lamportTime){
        robotsList.addRobot(cleaningRobot);
        updateLamportTime(Long.parseLong(lamportTime));
        incrementLamportTime();
        return String.valueOf(getLamportTime());
    }

    private void safelyShutdownChannel(ManagedChannel channel) {
        try {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {
            System.err.println("Interrupted while shutting down gRPC channel: " + ie.getMessage());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("Exception during channel shutdown: " + e.getMessage());
        }
    }

    // HEARTBEAT - here the robots checks periodically if the other robots are alive
    // if a crashed robot is detected, check if i was waiting any ack from him, inform both the rest server
    // and the other robots. If needed proceed with relocation request.
    private synchronized void sendHeartbeatToOtherRobots() {

        if(!atMechanic) {
            getRobotInfo();
            List<CleaningRobot> currentCleaningRobots = new ArrayList<>(this.robotsList.getRobots()); // Create a copy to iterate over

            for (CleaningRobot otherCleaningRobot : currentCleaningRobots) {

                //System.out.println("Printing all the robots in local robotsList");
                //this.robotsList.printIDs();
                if (otherCleaningRobot.getId() == this.id) continue; // Skip sending heartbeat to self
                //System.out.println("Sending heartbeat to Robot ID: " + otherCleaningRobot.getId());

                ManagedChannel channel = ManagedChannelBuilder.forAddress(otherCleaningRobot.getIp(), otherCleaningRobot.getPort())
                        .usePlaintext()
                        .build();
                HeartbeatServiceGrpc.HeartbeatServiceStub asyncStub = HeartbeatServiceGrpc.newStub(channel);
                RobotService.HeartbeatRequest request = RobotService.HeartbeatRequest.newBuilder()
                        .setRobotId(this.id)
                        .build();

                StreamObserver<RobotService.HeartbeatResponse> responseObserver = new StreamObserver<RobotService.HeartbeatResponse>() {
                    @Override
                    public void onNext(RobotService.HeartbeatResponse heartbeatResponse) {
                        System.out.println("[ROBOT - HEARTBEAT] Heartbeat successfully sent from robot " + CleaningRobot.this.getId() + " to robot " + otherCleaningRobot.getId());
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println("[ROBOT - HEARTBEAT] Failed to send heartbeat from robot " + CleaningRobot.this.getId() + " to robot " + otherCleaningRobot.getId() + ". Removing robot.");
                        //System.out.println("Closing Channel");
                        safelyShutdownChannel(channel);
                        robotsList.removeRobot(otherCleaningRobot.getId());
                        restMethods.notifyDeath(otherCleaningRobot.getId());
                        sendLeavingToRobots(otherCleaningRobot);

                        if (awaitingAcknowledgements.contains(otherCleaningRobot.getId())) {
                            awaitingAcknowledgements.remove(otherCleaningRobot.getId());
                            checkAndProceedWithMechanicAccess();
                        }
                        if(requestingRelocation) {
                            if (awaitingRelocatingAcknowledgements.contains(otherCleaningRobot.getId())) {
                                awaitingRelocatingAcknowledgements.remove(otherCleaningRobot.getId());
                                checkAndProceedWithMoving();
                            }
                        }else {
                            int myPopulation = robotsList.getPopulationByDistrict(district);
                            int otherPopulation = robotsList.getPopulationByDistrict(otherCleaningRobot.getDistrict());
                            if ((myPopulation > otherPopulation + 1)) {
                                System.out.println("sending relocation due to missed heartbeat");
                                sendRelocationRequests();
                            }
                        }


                    }
                    @Override
                    public void onCompleted() {
                        safelyShutdownChannel(channel);
                    }
                };

                asyncStub.sendHeartbeat(request, responseObserver);
            }
        }


    }
    private void sendHeartbeatToOtherRobotsSafely() {
        try {
            sendHeartbeatToOtherRobots();
        } catch (Exception e) {
            System.err.println("Error sending heartbeat: " + e.getMessage());
            // Consider re-initializing resources if necessary
        }
    }

    // MECHANIC - every 10 seconds check for malfunction. is so send mechanic requests to every robot in the list
    // then wait for all acks before accesing mechanic. Implementation with Ricart and Agrawala
    private void checkForMalfunction() {
        ScheduledExecutorService malfunctionExecutor = Executors.newSingleThreadScheduledExecutor();
        malfunctionExecutor.scheduleAtFixedRate(() -> {
            if(!requestingMechanic) {
                if (new Random().nextInt(10) == 0) { // 10% chance of malfunction
                    try {
                        requestMechanicAccess();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, 10, 10, TimeUnit.SECONDS);
    }
    public synchronized void requestMechanicAccess() throws InterruptedException {
        restMethods.notifyServerOfStateChange(id, 1);
        requestingMechanic = true;
        myRequestTimestamp = System.currentTimeMillis();
        incrementLamportTime();
        long myLamportRequestTimestamp = getLamportTime();
        mechanicRequestLamportTime = myLamportRequestTimestamp;
        awaitingAcknowledgements.clear();


        //System.out.println("SENDING MECHANIC REQUESTS");
        // Send request to all known robots
        for (CleaningRobot cleaningRobot : robotsList.getRobots()) {
            if (cleaningRobot.getId() != this.id) { // Skip self
                awaitingAcknowledgements.add(cleaningRobot.getId());
                sendMechanicRequest(cleaningRobot, myRequestTimestamp, myLamportRequestTimestamp);
            }
        }

        Random rd = new Random();
        Thread.sleep(rd.nextInt(5) * 1000);

        //System.out.println("WAITING FOR ALL ACKS");
        while (!awaitingAcknowledgements.isEmpty()) {
            try {
                wait(); // Wait for all acknowledgements
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }




        //System.out.println("ACCESSING MECHANIC");
        restMethods.notifyServerOfStateChange(id, 2);
        atMechanic = true;
        accessMechanic();
        atMechanic = false;
        restMethods.notifyServerOfStateChange(id, 0);
        //System.out.println("SENDING ACKS TO QUEUE");
        // After accessing the mechanic, handle deferred requests
        processDeferredRequests();
        requestingMechanic = false;
        myRequestTimestamp = -1; // Reset request timestamp
        mechanicRequestLamportTime = -1;
    }
    private void sendMechanicRequest(CleaningRobot cleaningRobot1, long timestamp, long lamportTimestamp) {
        //System.out.println("sending mechanic request to " + cleaningRobot.getId() + " port: " + cleaningRobot.getPort());
        ManagedChannel channel = ManagedChannelBuilder.forAddress(cleaningRobot1.getIp(), cleaningRobot1.getPort())
                .usePlaintext()
                .build();
        MechanicServiceGrpc.MechanicServiceStub asyncStub = MechanicServiceGrpc.newStub(channel);

        RobotService.MechanicRequest request = RobotService.MechanicRequest.newBuilder()
                .setRobotId(this.id)
                .setTimestamp(String.valueOf(timestamp + offset))
                .setLamportTimestamp(String.valueOf(lamportTimestamp))
                .build();

        asyncStub.requestMechanic(request, new StreamObserver<RobotService.MechanicResponse>() {
            @Override
            public void onNext(RobotService.MechanicResponse mechanicResponse) {
                System.out.println("mechanic request delivered to Robot ID: " + cleaningRobot1.getId());

            }

            @Override
            public void onError(Throwable t) {
                //System.err.println("Error sending mechanic request to Robot ID: " + cleaningRobot.getId() + ": " + t.getMessage());

                safelyShutdownChannel(channel);
                if (awaitingAcknowledgements.contains(cleaningRobot1.getId())) {
                    awaitingAcknowledgements.remove(cleaningRobot1.getId());
                    checkAndProceedWithMechanicAccess();
                }
            }

            @Override
            public void onCompleted() {
                //channel.shutdown();
                safelyShutdownChannel(channel);
            }
        });
    }
    private synchronized void checkAndProceedWithMechanicAccess() {
        if (awaitingAcknowledgements.isEmpty() && requestingMechanic) {
            notifyAll(); // Notify any waiting threads (e.g., the mechanic access request thread) to proceed.
            System.out.println("All acknowledgements received or robots crashed, proceeding with mechanic access.");
        }
    }
    private void sendAcknowledgement(int requestingRobotID, String requestingRobotIP, int requestingRobotPort, long lamportTime) {

        //System.out.println("SENDING ACK TO " + robotId);
        ManagedChannel channel = ManagedChannelBuilder.forAddress(requestingRobotIP, requestingRobotPort)
                .usePlaintext()
                .build();
        try {
            MechanicServiceGrpc.MechanicServiceBlockingStub blockingStub = MechanicServiceGrpc.newBlockingStub(channel);

            RobotService.AcknowledgeMechanicRequest request = RobotService.AcknowledgeMechanicRequest.newBuilder()
                    .setRobotId(this.id)
                    .setTimestamp(String.valueOf(System.currentTimeMillis() + offset)) // Current time as acknowledgement timestamp
                    .setLamportTimestamp(String.valueOf(lamportTime))
                    .build();

            RobotService.AcknowledgeMechanicResponse response = blockingStub.acknowledgeMechanic(request);
            // Acknowledgement sent and processed
            System.out.println("Acknowledgement sent to Robot ID: " + requestingRobotID);
        } catch (Exception e) {
            System.err.println("Error sending acknowledgement to Robot ID: " + requestingRobotID + ": " + e.getMessage());
        } finally {
            safelyShutdownChannel(channel);

        }
    }
    public synchronized void handleMechanicRequest(int requestingRobotId, long requestTimestamp, String requestLamportTimestamp) {
        System.out.println("received mechanic request by " + requestingRobotId);
        long longRequestLamportTimestamp = Long.parseLong(requestLamportTimestamp);
        updateLamportTime(longRequestLamportTimestamp); // Update based on the received timestamp
        incrementLamportTime();

        CleaningRobot requestingRobot = robotsList.getRobotById(requestingRobotId);

        MechanicRequest incomingMechanicRequest = new MechanicRequest(requestingRobot.getId(),
                requestingRobot.getIp(),
                requestingRobot.getPort(),
                longRequestLamportTimestamp);

        long myLamportTime = getLamportTime();
        if (!requestingMechanic) {
            System.out.println("RECEIVED MECHANIC REQUEST from "+ requestingRobotId + " - ACK DONT NEED");
            sendAcknowledgement(requestingRobot.getId(),
                    requestingRobot.getIp(),
                    requestingRobot.getPort(), myLamportTime);
        } else if (mechanicRequestLamportTime < longRequestLamportTimestamp ||
                (mechanicRequestLamportTime == longRequestLamportTimestamp && this.id < requestingRobotId)) {
            // my request was earlier, or it's a tie and my ID is lower; wait for all acknowledgments.
            System.out.println("RECEIVED MECHANIC REQUEST from "+ requestingRobotId +" - QUEUING THE REQUEST, MY REQUEST TIME IS SMALLER");
            deferredMechanicRequests.add(incomingMechanicRequest);
        } else {
            // The incoming request was earlier or it's a tie and the other robot's ID is lower; send acknowledgement.
            System.out.println("RECEIVED MECHANIC REQUEST from "+ requestingRobotId + " - ACK MY REQUEST TIME IS BIGGER");
            sendAcknowledgement(requestingRobot.getId(),
                    requestingRobot.getIp(),
                    requestingRobot.getPort(), myLamportTime);
        }
    }
    public synchronized void receiveAcknowledgement(int acknowledgingRobotId, String ackLamportTimestamp) {
        long parsedAckLamportTimestamp = Long.parseLong(ackLamportTimestamp.trim());
        updateLamportTime(parsedAckLamportTimestamp);
        System.out.println("[Robot " + this.id + "] Received ACK from Robot ID: " + acknowledgingRobotId);
        if(awaitingAcknowledgements.contains(acknowledgingRobotId)) {
            awaitingAcknowledgements.remove(acknowledgingRobotId);
            checkAndProceedWithMechanicAccess();
        }
    }
    private void accessMechanic() {
        try {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedNow = now.format(formatter);

            System.out.println("[ MECHANIC " + formattedNow + " ] Robot " + this.id + " is accessing the mechanic.");
            Thread.sleep(10000);

            now = LocalDateTime.now();
            formattedNow = now.format(formatter);
            System.out.println("[ MECHANIC " + formattedNow + " ] Robot " + this.id + " is releasing the mechanic.");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void processDeferredRequests() {
        if(!deferredMechanicRequests.isEmpty()) {
            incrementLamportTime();
            long ackTimestamp = getLamportTime();
            while (!deferredMechanicRequests.isEmpty()) {
                MechanicRequest deferredMechanicRequest = deferredMechanicRequests.poll();

                sendAcknowledgement(deferredMechanicRequest.getRobotId(),deferredMechanicRequest.getIp(),deferredMechanicRequest.getPort(), ackTimestamp);
            }
        }
    }

    // MOVING - when a robot detects that a robot from an under-populated district crashes
    // it send the relocation request to all robots. Implementation with Ricart and Agrawala
    // when a robot has moved it informs both the rest server and the other robots of its new position before releasing the moving resource
    private synchronized void sendRelocationRequests() {
        System.out.println("INITIATING RELOCATION");
        requestingRelocation = true;
        myRelocatingRequestTimestamp = System.currentTimeMillis();
        awaitingRelocatingAcknowledgements.clear();
        incrementLamportTime(); // Increment Lamport time on sending request
        long lamportTimestamp = getLamportTime(); // Get current Lamport time
        relocatingRequestLamportTime = lamportTimestamp;
        for (CleaningRobot robot : robotsList.getRobots()) {
            if (robot.getId() != this.id) { // Skip self
                awaitingRelocatingAcknowledgements.add(robot.getId());
                sendRelocationRequest(robot, myRelocatingRequestTimestamp, lamportTimestamp);
            }
        }

        while (!awaitingRelocatingAcknowledgements.isEmpty()) {
            try {
                wait(); // Wait for all acknowledgements
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("RECEIVED ALL ACK PROCEEDING WITH MOVING");
        relocating = true;
        // After receiving all acknowledgements, update district and MQTT subscription

        int newDistrict = robotsList.determineTargetDistrictForRelocation();
        if(newDistrict != -1) {
            int[] newPos = generateCoordinatesForDistrict(newDistrict);
            int oldDistrict = this.getDistrict();
            System.out.println("MY NEW POS " + Arrays.toString(newPos));
            setCoordinates(newPos);
            setDistrict(newDistrict);
            robotsList.updatePosition(this.getId(), newPos[0], newPos[1], newDistrict);
            updateMqttSubscriptionForNewDistrict(oldDistrict);
            incrementLamportTime();
            long movingLamportTimestamp = getLamportTime(); // Get current Lamport time
            for (CleaningRobot robot : robotsList.getRobots()) {
                if (robot.getId() != this.id) { // Skip self
                    sendMovingMessage(robot, newPos, newDistrict, movingLamportTimestamp);
                }
            }
            restMethods.notifyServerOfNewPosition(this.getId(), newPos, newDistrict);

        }

        relocating = false;
        System.out.println("SENDING ACKS");
        processRelocatingDeferredRequests();

        requestingRelocation = false;
        myRelocatingRequestTimestamp = -1; // Reset request timestamp
        relocatingRequestLamportTime = -1;
    }
    private void sendMovingMessage(CleaningRobot cleaningRobot, int[] newPos, int newDistrict, Long lamportTime) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(cleaningRobot.getIp(), cleaningRobot.getPort())
                .usePlaintext()
                .build();
        RelocationServiceGrpc.RelocationServiceStub asyncStub = RelocationServiceGrpc.newStub(channel);
        RobotService.MovingRequest request = RobotService.MovingRequest.newBuilder()
                .setRobotId(this.id)
                .setNewPosX(newPos[0])
                .setNewPosY(newPos[1])
                .setNewDistrict(newDistrict)
                .setLamportTimestamp(String.valueOf(lamportTime))
                .build();

        asyncStub.moving(request, new StreamObserver<RobotService.MovingResponse>() {
            @Override
            public void onNext(RobotService.MovingResponse movingResponse) {

            }
            @Override
            public void onError(Throwable t) {
                safelyShutdownChannel(channel);
            }

            @Override
            public void onCompleted() {
                //channel.shutdown();
                safelyShutdownChannel(channel);
            }
        });
    }
    private int[] generateCoordinatesForDistrict(int district) {
        Random random = new Random();
        int x, y;
        if (district == 1 || district == 3) {
            x = random.nextInt(5); // 0-4 for districts 1 and 3
        } else {
            x = 5 + random.nextInt(5); // 5-9 for districts 2 and 4
        }
        if (district == 1 || district == 2) {
            y = random.nextInt(5); // 0-4 for districts 1 and 2
        } else {
            y = 5 + random.nextInt(5); // 5-9 for districts 3 and 4
        }
        return new int[]{x, y};
    }
    private void sendRelocationRequest(CleaningRobot robot, long timestamp, long lamportTime ){
        System.out.println("SENDING RELOCATION REQUEST TO " + robot);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(robot.getIp(), robot.getPort())
                .usePlaintext()
                .build();

        RelocationServiceGrpc.RelocationServiceStub asyncStub = RelocationServiceGrpc.newStub(channel);

        RobotService.RelocationRequest request = RobotService.RelocationRequest.newBuilder()
                .setRobotId(this.id)
                .setTimestamp(String.valueOf(timestamp + offset))
                .setLamportTimestamp(String.valueOf(lamportTime))
                .build();


        asyncStub.requestRelocation(request, new StreamObserver<RobotService.RelocationResponse>() {
            @Override
            public void onNext(RobotService.RelocationResponse response) {

            }

            @Override
            public void onError(Throwable t) {
                safelyShutdownChannel(channel);
            }

            @Override
            public void onCompleted() {
                //channel.shutdown();
                safelyShutdownChannel(channel);
            }
        });
    }

    private void updateMqttSubscriptionForNewDistrict(int oldDistrict) {
        try {
            mqttClient.unsubscribe(this.mqttTopic + oldDistrict); // Unsubscribe from the old district's topic
            this.mqttTopic = "greenfield/pollution/district" + this.district; // Update topic
            mqttClient.subscribe(this.mqttTopic); // Subscribe to the new district's topic
            System.out.println("Robot " + this.id + " now subscribed to MQTT topic of District " + this.district);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    public synchronized void handleRelocatingRequest(int requestingRobotId, long requestTimestamp, String requestLamportTimestamp) {

        long longRequestLamportTimestamp = Long.parseLong(requestLamportTimestamp);
        MovingRequest incomingMovingRequest = new MovingRequest(requestingRobotId, longRequestLamportTimestamp);
        // Scenario 1 : don't need or timestamp smaller
        // Scenario 2 :
        // Scenario 1 and 3: Not requesting or timestamp comparison
        updateLamportTime(longRequestLamportTimestamp);
        incrementLamportTime();
        long relocationAckTime = getLamportTime();
        if ((!requestingRelocation || longRequestLamportTimestamp < relocatingRequestLamportTime ||
                (longRequestLamportTimestamp == relocatingRequestLamportTime && requestingRobotId < this.id))) {
            System.out.println("RECEIVED RELOCATING REQUEST - ACK DON'T NEED OR MY REQUEST TIME IS BIGGER");
            sendRelocationAcknowledgement(requestingRobotId, relocationAckTime);
        } else {
            System.out.println("RECEIVED RELOCATING REQUEST - QUEUEING, MY REQUEST TIME IS SMALLER");
            deferredRelocatingRequests.add(incomingMovingRequest);
        }
    }
    private void processRelocatingDeferredRequests() {
        if(!deferredRelocatingRequests.isEmpty()) {
            incrementLamportTime();
            long relocationAckTimestamp = getLamportTime();
            while (!deferredRelocatingRequests.isEmpty()) {
                MovingRequest deferredMechanicRequest = deferredRelocatingRequests.poll();
                sendRelocationAcknowledgement(deferredMechanicRequest.getRobotId(), relocationAckTimestamp);
            }
        }
    }

    private synchronized void sendRelocationAcknowledgement(int robotId, long relocationAckTimestamp) {

        CleaningRobot targetCleaningRobot = robotsList.getRobotById(robotId);
        if (targetCleaningRobot == null) {
            System.err.println("Robot ID: " + robotId + " not found for sending acknowledgement.");
            return;
        }
        //System.out.println("SENDING ACK TO " + robotId);
        ManagedChannel channel = ManagedChannelBuilder.forAddress(targetCleaningRobot.getIp(), targetCleaningRobot.getPort())
                .usePlaintext()
                .build();
        try {
            RelocationServiceGrpc.RelocationServiceBlockingStub blockingStub = RelocationServiceGrpc.newBlockingStub(channel);
            RobotService.AcknowledgeRelocationRequest request = RobotService.AcknowledgeRelocationRequest.newBuilder()
                    .setRobotId(this.id)
                    .setTimestamp(String.valueOf(System.currentTimeMillis() + offset)) // Current time as acknowledgement timestamp
                    .setLamportTimestamp(String.valueOf(relocationAckTimestamp))
                    .build();

            RobotService.AcknowledgeRelocationResponse response = blockingStub.acknowledgeRelocation(request);
            // Acknowledgement sent and processed
            System.out.println("Acknowledgement sent to Robot ID: " + robotId);
        } catch (Exception e) {
            System.err.println("Error sending acknowledgement to Robot ID: " + robotId + ": " + e.getMessage());
            // will get deletied from internal list with the heartbeat mechanic
        } finally {
            channel.shutdown(); // Ensure channel is always shutdown
        }
    }
    public synchronized void receiveRelocationAcknowledgement(int acknowledgingRobotId, String ackLamportTime) {
        long parsedAckLamportTimestamp = Long.parseLong(ackLamportTime.trim());
        updateLamportTime(parsedAckLamportTimestamp);
        System.out.println("[Robot " + this.id + "] Received ACK from Robot ID: " + acknowledgingRobotId);
        awaitingRelocatingAcknowledgements.remove(acknowledgingRobotId);
        checkAndProceedWithMoving();
    }
    public void receiveMoving(int robotID, int posX, int posY, int district, String moveLamportTime){
        long parsedMoveLamportTimestamp = Long.parseLong(moveLamportTime.trim());
        updateLamportTime(parsedMoveLamportTimestamp);
        System.out.println("updatind robot pos. new: " + robotID + " district: " + district + " posx " + posX + " posy " + posY);
        this.robotsList.updatePosition(robotID, posX, posY, district);

    }
    private synchronized void checkAndProceedWithMoving() {
        if (awaitingRelocatingAcknowledgements.isEmpty() && requestingRelocation) {
            notifyAll(); // Notify any waiting threads
            System.out.println("All acknowledgements received or robots crashed, proceeding with relocating.");
        }
    }

    // LEAVING - grpc message send when a robot wants to leave the network
    private synchronized void sendLeavingToRobots(CleaningRobot leavingRobot) {

        for (CleaningRobot otherCleaningRobot : this.robotsList.getRobots()) {
            if (otherCleaningRobot.getId() == this.id) continue; // Skip sending leaving message to self

            ManagedChannel channel = ManagedChannelBuilder.forAddress(otherCleaningRobot.getIp(), otherCleaningRobot.getPort())
                    .usePlaintext()
                    .build();
            LeavingServiceGrpc.LeavingServiceStub asyncStub = LeavingServiceGrpc.newStub(channel);

            RobotService.LeavingRequest request = RobotService.LeavingRequest.newBuilder()
                    .setId(leavingRobot.getId())
                    .setDistrict(leavingRobot.getDistrict())
                    .build();

            StreamObserver<RobotService.LeavingResponse> responseObserver = new StreamObserver<RobotService.LeavingResponse>() {
                @Override
                public void onNext(RobotService.LeavingResponse leavingResponse) {
                    System.out.println("Notified robot " + otherCleaningRobot.getId() + " of leaving: ");
                }

                @Override
                public void onError(Throwable throwable) {
                    System.out.println("Failed to notify robot " + otherCleaningRobot.getId() + " of leaving.");
                    safelyShutdownChannel(channel);
                }

                @Override
                public void onCompleted() {
                    //channel.shutdown();
                    safelyShutdownChannel(channel);
                }
            };

            asyncStub.leave(request, responseObserver);
        }
    }
    public synchronized void receiveLeaving(int leavingRobotID, int leavingRobotDistrict){

        restMethods.notifyDeath(leavingRobotID);

        robotsList.removeRobot(leavingRobotID);

        int myPopulation = robotsList.getPopulationByDistrict(district);
        int otherPopulation = robotsList.getPopulationByDistrict(leavingRobotDistrict);

        if(!requestingRelocation && (myPopulation > 1) && (myPopulation > otherPopulation + 1)){
            //System.out.println("sending relocating request due to leave message");
            sendRelocationRequests();

        }

        if(awaitingRelocatingAcknowledgements.contains(leavingRobotID)){
            awaitingRelocatingAcknowledgements.remove(leavingRobotID);
            checkAndProceedWithMoving();
        }

        if (awaitingAcknowledgements.contains(leavingRobotID)) {
            awaitingAcknowledgements.remove(leavingRobotID);
            checkAndProceedWithMechanicAccess();
        }



    }
    public void quit() {
        System.out.println("Quitting robot " + this.id);
        if (pm10Simulator != null) {
            pm10Simulator.stopMeGently();
        }

        sendLeavingToRobots(this);

        this.running = false;
        try {
            Client client = Client.create();
            // calling a DELETE host/remove/id removes the robot with the given id
            WebResource webResource = client
                    .resource(restBaseAddressRobots + "remove/" + this.id);

            ClientResponse response = webResource.type("application/json")
                    .delete(ClientResponse.class);


            int status = response.getStatus();

            if (status == 200) {
                // id found
                System.out.println("Robot " + this.id + " removed from REST api");
            } else if (status == 404) {
                // if rest api gives a conflict response
                System.out.println("Robot " + this.id + " was not found on rest api");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // stop threads and quit
        System.exit(0);
    }

    // LAMPORT - lamport counter in order to order the events.
    // the counter is updated/incremented with grpc messages: Greeting, Mechanic, Relocation.
    // Heartbeat doesn't update the counter since it doesn't need ordering
    public synchronized void incrementLamportTime() {
        this.lamportTime++;
    }
    public synchronized void updateLamportTime(long receivedTime) {
        this.lamportTime = Math.max(this.lamportTime, receivedTime) + 1;
    }
    private synchronized long getLamportTime() {
        return this.lamportTime;
    }

    // MAIN
    public void run() throws InterruptedException {
        // Robot initialization
        if (!restMethods.initialize()) {
            System.out.println("Failed to initialize the robot with ID: " + this.id);
            return;
        }

        grpcServer = new GrpcServer(this);
        grpcServer.start();

        greetOtherRobots();




        connectToMqttBroker();
        processDataFromBuffer();

        ScheduledExecutorService heartbeatExecutor = Executors.newSingleThreadScheduledExecutor();
        heartbeatExecutor.scheduleAtFixedRate(this::sendHeartbeatToOtherRobots, 0, 5, TimeUnit.SECONDS);

        if (heartbeatExecutor == null || heartbeatExecutor.isShutdown() || heartbeatExecutor.isTerminated()) {
            heartbeatExecutor = Executors.newSingleThreadScheduledExecutor();
            heartbeatExecutor.scheduleAtFixedRate(this::sendHeartbeatToOtherRobotsSafely, 0, 5, TimeUnit.SECONDS);
        }

        checkForMalfunction();

    }

    public static void main(String[] args) {
        Random rd = new Random();
        CleaningRobot cleaningRobot = new CleaningRobot(rd.nextInt(899) + 100, "localhost", 10000 + rd.nextInt(30000), rd.nextInt(1000));

        Thread robotThread = new Thread(() -> {
            try {
                cleaningRobot.run();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        robotThread.start();

        // Wait for user input in a separate thread
        Thread userInputThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            while (!input.equalsIgnoreCase("quit")) {
                if (input.equalsIgnoreCase("fix")) {
                    try {
                        cleaningRobot.requestMechanicAccess();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                input = scanner.nextLine();
            }

            cleaningRobot.quit();
        });
        userInputThread.start();

        // Wait for both threads to complete

        try {
            robotThread.join();
            userInputThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
