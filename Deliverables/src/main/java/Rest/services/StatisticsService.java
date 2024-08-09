package Rest.services;

import Rest.beans.AvgMeasurement;
import Rest.beans.PollutionRecord;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/statistics")
public class StatisticsService {
    @GET
    @Path("/countByRobot")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCountOfMeasurementsByRobot(@QueryParam("robotId") int robotId) {
        Map<Integer, List<AvgMeasurement>> measurementsMap = PollutionRecord.getInstance().getMeasurementsMap();
        int count = measurementsMap.getOrDefault(robotId, Collections.emptyList()).size();

        return Response.ok(count).build();
    }


    @GET
    @Path("/averageByRobot")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAverageByRobot(@QueryParam("robotId") int robotId,
                                      @QueryParam("measurementsCount") int n) {
        Map<Integer, List<AvgMeasurement>> measurementsMap = PollutionRecord.getInstance().getMeasurementsMap();
        List<AvgMeasurement> measurements = measurementsMap.getOrDefault(robotId, Collections.emptyList());

        // Get the last 'n' measurements
        List<AvgMeasurement> recentMeasurements = measurements.stream()
                .skip(Math.max(0, measurements.size() - n))
                .collect(Collectors.toList());

        double average = recentMeasurements.stream()
                .mapToDouble(AvgMeasurement::getValue)
                .average()
                .orElse(Double.NaN);

        return Response.ok(average).build();
    }

    @GET
    @Path("/averageBetweenTimestamps")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAverageBetweenTimestamps(@QueryParam("t1") long t1,
                                                @QueryParam("t2") long t2) {
        Map<Integer, List<AvgMeasurement>> measurementsMap = PollutionRecord.getInstance().getMeasurementsMap();

        double average = measurementsMap.values().stream()
                .flatMap(List::stream)
                .filter(m -> m.getTimestamp() >= t1 && m.getTimestamp() <= t2)
                .mapToDouble(AvgMeasurement::getValue)
                .average()
                .orElse(Double.NaN);

        return Response.ok(average).build();
    }
    @GET
    @Path("/earliestTimestamp")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEarliestTimestamp() {
        Map<Integer, List<AvgMeasurement>> measurementsMap = PollutionRecord.getInstance().getMeasurementsMap();

        Long earliestTimestamp = measurementsMap.values().stream()
                .flatMap(List::stream)
                .map(AvgMeasurement::getTimestamp)
                .min(Long::compare)
                .orElse(null);

        if (earliestTimestamp == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("No measurements found").build();
        }

        return Response.ok(earliestTimestamp).build();
    }
    @GET
    @Path("/latestTimestamp")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLatestTimestamp() {
        Map<Integer, List<AvgMeasurement>> measurementsMap = PollutionRecord.getInstance().getMeasurementsMap();

        Long latestTimestamp = measurementsMap.values().stream()
                .flatMap(List::stream)
                .map(AvgMeasurement::getTimestamp)
                .max(Long::compare)
                .orElse(null);

        if (latestTimestamp == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("No measurements found").build();
        }

        return Response.ok(latestTimestamp).build();
    }
}
