package Robot;

import Simulators.Buffer;
import Simulators.Measurement;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MeasurementBuffer implements Buffer {
    private ArrayList<Measurement> measurements = new ArrayList<>();
    private final int windowSize = 8;
    public MeasurementBuffer() {
        measurements = new ArrayList<>();
    }

    @Override
    public synchronized void addMeasurement(Measurement m) {
        measurements.add(m);
    }

    @Override
    public synchronized List<Measurement> readAllAndClean() {
        List<Measurement> allMeasurements = new ArrayList<>(measurements);
        measurements.clear();
        return allMeasurements;
    }
}
