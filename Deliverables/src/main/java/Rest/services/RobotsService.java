package Rest.services;

import Rest.beans.PosRobotsList;
import Rest.beans.Robot;
import Rest.beans.Robots;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;


@Path("robots")
public class RobotsService {

    @GET
    @Produces({"application/json", "application/xml"})
    public Response getRobots(){
        return Response.ok(Robots.getInstance()).build();
    }

    @Path("add")
    @POST
    @Consumes({"application/json", "application/xml"})
    public Response addRobot(Robot robot){
        PosRobotsList result = Robots.getInstance().add(robot);
        if(result != null)
            return Response.ok(result).build(); // Return the response with the list of robots, coordinates, and district
        else
            return Response.status(Response.Status.CONFLICT).build(); // Handle conflict if the robot already exists
    }



    @Path("updatePosition")
    @POST
    @Consumes("application/json")
    public Response updateRobotPosition(String updateRequestJson) {
        //System.out.println("Received update position request: " + updateRequestJson);
        try {
            JSONObject updateRequest = new JSONObject(updateRequestJson);
            int id = updateRequest.getInt("id");
            JSONArray coordinatesArray = updateRequest.getJSONArray("coordinates");
            int[] newCoordinates = {coordinatesArray.getInt(0), coordinatesArray.getInt(1)};
            int newDistrict = updateRequest.getInt("district");

            System.out.println("Updating position for Robot ID: " + id + " to District: " + newDistrict + " Coordinates: " + Arrays.toString(newCoordinates));

            boolean success = Robots.getInstance().updateRobotPosition(id, newCoordinates, newDistrict);
            if (success) {
                System.out.println("Successfully updated robot position.");
                return Response.ok().build(); // Successfully updated
            } else {
                System.out.println("Failed to find robot for updating position.");
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (JSONException e) {
            System.out.println("Error parsing update position request: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request format").build();
        }
    }

    @Path("updateState")
    @POST
    @Consumes("application/json")
    public Response updateRobotState(String updateRequestJson) {
        //System.out.println("Received update position request: " + updateRequestJson);
        try {
            JSONObject updateRequest = new JSONObject(updateRequestJson);
            int id = updateRequest.getInt("id");
            int state = updateRequest.getInt("state");
            boolean success = Robots.getInstance().updateRobotState(id, state);
            if (success) {
                System.out.println("Successfully updated robot state.");
                return Response.ok().build(); // Successfully updated
            } else {
                System.out.println("Failed to find robot for updating state.");
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (JSONException e) {
            System.out.println("Error parsing update state request: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request format").build();
        }
    }





    @Path("get")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getRobotsList(){
        ArrayList<Robot> l = Robots.getInstance().getRobotsList();
        return Response.ok(l).build();
    }

    @Path("get/{id}")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getById(@PathParam("id") int id){
        Robot u = Robots.getInstance().getById(id);
        if(u!=null)
            return Response.ok(u).build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();
    }

    @Path("remove/{id}")
    @DELETE
    @Produces({"application/json", "application/xml"})
    public Response deleteById(@PathParam("id") int id){
        Robot u = Robots.getInstance().deleteById(id);
        if(u!=null)
            return Response.ok(u).build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();
    }

}
