package Rest;

import Rest.beans.AvgMeasurement;
import Rest.beans.PollutionRecord;
import Simulators.Measurement;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.eclipse.paho.client.mqttv3.*;

import java.io.IOException;
import java.util.*;

public class AdminServer {

    private static final String HOST = "localhost";
    private static final int PORT = 1337;
    private static final String MQTT_BROKER = "tcp://localhost:1883";
    private static MqttClient client;

    private static PollutionRecord pollutionRecord;

    //private static final Map<Integer, List<Measurement>> measurementsMap = new HashMap<>();

    public static void main(String[] args) throws IOException, MqttException {
        // REST
        HttpServer server = HttpServerFactory.create("http://"+HOST+":"+PORT+"/");
        server.start();

        // MQTT Client setup
        client = new MqttClient(MQTT_BROKER, MqttClient.generateClientId());
        client.setCallback(new MqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
               // System.out.println("Message received on topic " + topic + ": " + new String(message.getPayload()));

                int robotID = extractRobotID(message);
                List<AvgMeasurement> avgMeasurements = deserializeAvgMeasurements(message.getPayload());

                // Add each average measurement to the PollutionRecord
                for (AvgMeasurement avgMeasurement : avgMeasurements) {
                    PollutionRecord.getInstance().addMeasurement(robotID, avgMeasurement);
                }

                //System.out.println( avgMeasurements.size() +" Measurements added for robot ID " + robotID);
            }


            @Override
            public void connectionLost(Throwable cause) {
                // Handle connection loss
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                // Called when a message has been delivered
            }
        });

        client.connect();
        client.subscribe("greenfield/pollution/district1");
        client.subscribe("greenfield/pollution/district2");
        client.subscribe("greenfield/pollution/district3");
        client.subscribe("greenfield/pollution/district4");

        System.out.println("Server running!");
        System.out.println("Server started on: http://"+HOST+":"+PORT);

        System.out.println("Hit return to stop...");
        System.in.read();
        System.out.println("Stopping server");
        server.stop(0);
        System.out.println("Server stopped");
    }
    private static int extractRobotID(MqttMessage message) throws JSONException {
        // Convert the payload to a String
        String payload = new String(message.getPayload());

        // Parse the payload as a JSON object
        JSONObject jsonObject = new JSONObject(payload);

        // Extract the robot ID
        int robotID = jsonObject.getInt("robot_id");

        return robotID;
    }

    private static List<AvgMeasurement> deserializeAvgMeasurements(byte[] payload) throws JSONException {
        String payloadStr = new String(payload);
        JSONObject jsonObject = new JSONObject(payloadStr);
        JSONArray jsonAverages = jsonObject.getJSONArray("averages");

        List<AvgMeasurement> avgMeasurements = new ArrayList<>();

        for (int i = 0; i < jsonAverages.length(); i++) {
            JSONObject jsonAvg = jsonAverages.getJSONObject(i);
            double value = jsonAvg.getDouble("value");
            long timestamp = jsonAvg.getLong("timestamp");

            AvgMeasurement avgMeasurement = new AvgMeasurement(value, timestamp);
            avgMeasurements.add(avgMeasurement);
        }

        return avgMeasurements;
    }





}