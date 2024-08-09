package Rest.beans;

public class AvgMeasurement {

    private long timestamp;
    private double value;

    public AvgMeasurement( double value, long timestamp){
        this.timestamp = timestamp;
        this.value = value;
    }


    public long getTimestamp() {
        return timestamp;
    }

    public double getValue() {
        return value;
    }


    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
