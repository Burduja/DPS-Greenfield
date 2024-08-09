package Robot;

public class MovingRequest implements Comparable<MovingRequest> {
    private final int robotId;
    private final long timestamp;

    public MovingRequest(int robotId, long timestamp) {
        this.robotId = robotId;
        this.timestamp = timestamp;
    }

    public int getRobotId() {
        return robotId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public int compareTo(MovingRequest other) {
        if (this.timestamp != other.timestamp) {
            return Long.compare(this.timestamp, other.timestamp);
        }
        return Integer.compare(this.robotId, other.robotId);
    }
}
