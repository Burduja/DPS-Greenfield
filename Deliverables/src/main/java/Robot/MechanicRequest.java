package Robot;

public class MechanicRequest implements Comparable<MechanicRequest> {
    private final int robotId;
    private final String ip;
    private final int port;
    private final long timestamp;

    public MechanicRequest(int robotId, String ip, int port, long timestamp) {
        this.robotId = robotId;
        this.ip = ip;
        this.port = port;
        this.timestamp = timestamp;
    }

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }


    public int getRobotId() {
        return robotId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public int compareTo(MechanicRequest other) {
        if (this.timestamp != other.timestamp) {
            return Long.compare(this.timestamp, other.timestamp);
        }
        return Integer.compare(this.robotId, other.robotId);
    }
}
