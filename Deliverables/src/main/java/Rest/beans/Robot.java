package Rest.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Robot {

    private int id;
    private String ip;
    private int port;

    private int[] coordinates;
    private int district;
    private int state;

    public Robot() {}

    public Robot(int id, String ip, int port, int state) {
        this.id = id;
        this.ip = ip;
        this.port = port;
        this.state = state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setCoordinates(int[] coordinates) {
        this.coordinates = coordinates;
    }

    public int[] getCoordinates() {
        return coordinates;
    }

    public void setDistrict(int district) {
        this.district = district;
    }

    public int getDistrict() {
        return district;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "Robot:\n" +
                "\n\t-id: " + id +
                "\n\t-ip: " + ip +
                "\n\t-port: " + port;
    }
}