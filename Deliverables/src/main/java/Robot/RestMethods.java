package Robot;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.sound.midi.Soundbank;
import java.sql.SQLOutput;
import java.util.Arrays;

// Helper class for the robots in order to comunicate with rest server

public class RestMethods {
    CleaningRobot cleaningRobot;
    // rest api base
    public static String restBaseAddressRobots = "http://localhost:1337/robots/";
    public static String restBaseAddressStatistics = "http://localhost:1337/statistics/";

    public RestMethods(CleaningRobot cleaningRobot) {
        this.cleaningRobot = cleaningRobot;
    }

    /*
    Make initial API request, initialize all the robot fields
     */
    public boolean initialize() {
        System.out.println("REST API INITIALIZATION: ");
        try {
            Client client = Client.create();
            WebResource webResource = client
                    .resource(restBaseAddressRobots + "add");

            String payload = this.getInitializePostPayload();
            System.out.println("Sending JSON payload: " + payload);
            ClientResponse response = webResource.type("application/json")
                    .post(ClientResponse.class, payload);

            // if the id is not present in the system
            int status = response.getStatus();
            System.out.println("Response status code: " + status);
            if (status == 200) {
                // no conflict, unpack the response and go on
                if (unpackInitializeResponse(response.getEntity(String.class))) {

                    System.out.println("\t- Robot " + cleaningRobot.id + " initialization completed");
                    return true;
                }
            } else if (status == 409) {
                // if rest api gives a conflict response
                System.out.println("\t- The given ID " + cleaningRobot.id + " is already in the system, retry.");
            } else {
                // unhandled
                System.out.println("\t- Unhandled case: response.getStatus() = " + status);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void notifyServerOfNewPosition(int id, int[] newCoordinates, int newDistrict) {
        try {
            Client client = Client.create();
            WebResource webResource = client.resource(restBaseAddressRobots + "updatePosition");

            JSONObject payload = new JSONObject();
            payload.put("id", id);
            payload.put("coordinates", new JSONArray(Arrays.toString(newCoordinates)));
            payload.put("district", newDistrict);

            ClientResponse response = webResource.type("application/json")
                    .post(ClientResponse.class, payload.toString());

            if (response.getStatus() == 200) {
                System.out.println("Successfully updated position on the server for robot ID: " + id);
            } else {
                System.out.println("Failed to update position on the server for robot ID: " + id + ". Status code: " + response.getStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getMovingPayload() throws JSONException {
        JSONObject payload = new JSONObject();
        payload.put("id", cleaningRobot.id);
        payload.put("ip", cleaningRobot.ip);
        payload.put("port", cleaningRobot.port);
        payload.put("coordinates", cleaningRobot.getCoordinates());
        payload.put("district", cleaningRobot.getDistrict());
        return payload.toString();
    }
    /*
    Get the request payload to initialize
     */
    private String getInitializePostPayload() throws JSONException {
        JSONObject payload = new JSONObject();
        payload.put("id", cleaningRobot.id);
        payload.put("ip", cleaningRobot.ip);
        payload.put("port", cleaningRobot.port);
        return payload.toString();
    }


    private boolean unpackInitializeResponse(String response) {
        System.out.println("Raw JSON response: " + response);
        try {
            JSONObject input = new JSONObject(response);

            // Unpack coordinates and district
            JSONArray coordinates = input.getJSONArray("coordinates");
            cleaningRobot.coordinates[0] = coordinates.getInt(0);
            cleaningRobot.coordinates[1] = coordinates.getInt(1);
            cleaningRobot.district = input.getInt("district");
            System.out.println("I WAS ASSINGED");
            System.out.println("district " + input.getInt("district"));
            System.out.println("c0 " + coordinates.getInt(0));
            System.out.println("c1 " + coordinates.getInt(1));
            // Unpack robot list
            Object robotsListObject = input.get("robotsList");
            if (robotsListObject instanceof JSONArray) {
                System.out.println("GOT MULTIPLE ROBOTS IN NETWORK");
                // When there are multiple robots, it's an array
                JSONArray robotsArray = (JSONArray) robotsListObject;
                for (int i = 0; i < robotsArray.length(); i++) {
                    JSONObject robotObject = robotsArray.getJSONObject(i);
                    addRobotToList(robotObject);
                }
            } else if (robotsListObject instanceof JSONObject) {
                // When there's only one robot, it's an object
                System.out.println("GOT ONE ROBOT IN NETWORK");
                addRobotToList((JSONObject) robotsListObject);
            }
        } catch (JSONException e) {
            System.out.println("Error unpacking initialization response: " + e.getMessage());
            return false;
        }
        return true;
    }
    private void addRobotToList(JSONObject robotObject) throws JSONException {
        int id = robotObject.getInt("id");
        String ip = robotObject.getString("ip");
        int port = robotObject.getInt("port");
        int district = robotObject.getInt("district");
        JSONArray coordinatesArray = robotObject.getJSONArray("coordinates"); // Assuming the coordinates come in a JSON array.
        int posX = coordinatesArray.getInt(0);
        int posY = coordinatesArray.getInt(1);
        int[] coords = new int[2];
        coords[0] = posX;
        coords[1] = posY;

        if (id != cleaningRobot.id) {
            CleaningRobot r = new CleaningRobot(id, ip, port, coords);
            //System.out.println("distr " + district);
            r.setDistrict(district);

            //r.setPosX(posX);
            //r.setPosY(posY);
            //cleaningRobot.robotsList.robotsList.add(r); // Ensure robotsList is initialized and is the correct type
            cleaningRobot.robotsList.addRobot(r);
        }
    }

    public void notifyServerOfStateChange(int id, int newState) {
        try {
            Client client = Client.create();
            WebResource webResource = client.resource(restBaseAddressRobots + "updateState");

            // Constructing the JSON payload
            JSONObject payload = new JSONObject();
            payload.put("id", id);
            payload.put("state", newState);

            // Sending the payload to the server
            ClientResponse response = webResource.type("application/json")
                    .post(ClientResponse.class, payload.toString());

            // Handling the response
            if (response.getStatus() == 200) {
                System.out.println("Successfully updated state on the server for robot ID: " + id);
            } else {
                System.out.println("Failed to update state on the server for robot ID: " + id + ". Status code: " + response.getStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    /*
    Send quit request to the API
     */
    public void quit() {
        System.out.println("Quitting robot " + cleaningRobot.id);
        try {
            Client client = Client.create();
            // calling a DELETE host/remove/id removes the robot with the given id
            WebResource webResource = client
                    .resource(restBaseAddressRobots + "remove/" + cleaningRobot.id);

            ClientResponse response = webResource.type("application/json")
                    .delete(ClientResponse.class);

            // if the id is not present in the system
            int status = response.getStatus();

            if (status == 200) {
                // id found
                System.out.println("Robot " + cleaningRobot.id + " removed from REST api");
            } else if (status == 404) {
                // if rest api gives a conflict response
                System.out.println("Robot " + cleaningRobot.id + " was not found on rest api");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void notifyDeath(int robotID) {
        System.out.println("Quitting robot " + robotID);
        try {
            Client client = Client.create();
            // calling a DELETE host/remove/id removes the robot with the given id
            WebResource webResource = client
                    .resource(restBaseAddressRobots + "remove/" + robotID);

            ClientResponse response = webResource.type("application/json")
                    .delete(ClientResponse.class);


            int status = response.getStatus();

            if (status == 200) {
                // id found
                System.out.println("Robot " + cleaningRobot.id + " removed from REST api");
            } else if (status == 404) {
                // if rest api gives a conflict response
                System.out.println("Robot " + cleaningRobot.id + " was not found on rest api, probably aldready deleted");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
