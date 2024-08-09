package Rest.beans;

import Simulators.Measurement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.abs;

public class PollutionRecord {

    // here the key is an ID of a robot, and the values are the measurements it sent
    private Map<Integer, List<AvgMeasurement>> measurementsMap = new HashMap<>();
    private static PollutionRecord instance;

    public PollutionRecord() {
        measurementsMap = new HashMap<>();
    }

    public synchronized static PollutionRecord getInstance(){
        if(instance == null)
            instance = new PollutionRecord();
        return instance;
    }

    public synchronized Map<Integer, List<AvgMeasurement>> getMeasurementsMap() {
        return measurementsMap;
    }
    public synchronized void addMeasurement(int robotId, AvgMeasurement measurement) {
        measurementsMap.computeIfAbsent(robotId, k -> new ArrayList<>()).add(measurement);
    }

}
