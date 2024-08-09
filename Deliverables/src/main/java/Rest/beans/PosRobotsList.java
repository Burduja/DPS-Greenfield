package Rest.beans;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement
public class PosRobotsList {
    private ArrayList<Robot> robotsList;
    private int[] coordinates;

    private int district;


    public PosRobotsList(){}

    // class used as a response to a robot that is requesting to join the network
    public PosRobotsList(ArrayList<Robot> robotsList, int[] coordinates, int districts) {
        this.robotsList = robotsList;
        this.coordinates = coordinates;
        this.district = districts;
    }

    public void setDistrict(int district) {
        this.district = district;
    }

    public int getDistrict() {
        return district;
    }

    public ArrayList<Robot> getRobotsList() {
        return robotsList;
    }

    public void setRobotsList(ArrayList<Robot> robotsList) {
        this.robotsList = robotsList;
    }

    public int[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(int[] coordinates) {
        this.coordinates = coordinates;
    }
}