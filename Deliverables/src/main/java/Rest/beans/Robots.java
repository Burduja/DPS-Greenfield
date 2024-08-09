package Rest.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.abs;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Robots {

    @XmlElement(name="robots")
    private ArrayList<Robot> robotsList;

    private int[] districtRobotCounts = new int[4]; //
    private static Robots instance;
    private final int DISTRICT_SIZE = 5;

    private Robots() {
        robotsList = new ArrayList<Robot>();
    }

    //singleton
    public synchronized static Robots getInstance(){
        if(instance == null)
            instance = new Robots();
        return instance;
    }

    public synchronized ArrayList<Robot> getRobotsList() {
        return new ArrayList<Robot>(this.robotsList);
    }

    private int[] generateCoordinatesForDistrict(int district) {
        Random random = new Random();
        int x, y;
        if (district == 1 || district == 3) {
            x = random.nextInt(5); // 0-4 for districts 1 and 3
        } else {
            x = 5 + random.nextInt(5); // 5-9 for districts 2 and 4
        }
        if (district == 1 || district == 2) {
            y = random.nextInt(5); // 0-4 for districts 1 and 2
        } else {
            y = 5 + random.nextInt(5); // 5-9 for districts 3 and 4
        }
        return new int[]{x, y};
    }

    private int findleastPopulatedDistrict() {
        int minIndex = 0;
        for (int i = 1; i < districtRobotCounts.length; i++) {
            if (districtRobotCounts[i] < districtRobotCounts[minIndex]) {
                minIndex = i;
            }
        }
        return minIndex + 1; // Adjust for 1-based district indexing
    }

    private int determineDistrict(int[] coordinates) {
        int x = coordinates[0];
        int y = coordinates[1];

        if (x < 5 && y < 5) {
            return 1; // District 1
        } else if (x >= 5 && y < 5) {
            return 2; // District 2
        } else if (x < 5 && y >= 5) {
            return 3; // District 3
        } else {
            return 4; // District 4
        }
    }

    public synchronized PosRobotsList add(Robot robot) {
        for (Robot r : robotsList) {
            if (robot.getId() == r.getId()) return null; // Robot already exists
        }

        int district = findleastPopulatedDistrict();
        System.out.println("least populated distr " + district);
        robot.setDistrict(district);
        robot.setCoordinates(generateCoordinatesForDistrict(district));
        robot.setState(0);
        robotsList.add(robot);

        // Update district robot count
        districtRobotCounts[district - 1]++;

        return new PosRobotsList(new ArrayList<>(robotsList), robot.getCoordinates(), district);
    }


    public synchronized boolean updateRobotPosition(int id, int[] newCoordinates, int newDistrict) {
        Robot robotToUpdate = getById(id);
        if (robotToUpdate != null) {
            // Adjust district counts if the district has changed
            if (robotToUpdate.getDistrict() != newDistrict) {
                districtRobotCounts[robotToUpdate.getDistrict() - 1]--;
                districtRobotCounts[newDistrict - 1]++;
            }
            // Update robot's position and district
            robotToUpdate.setCoordinates(newCoordinates);
            robotToUpdate.setDistrict(newDistrict);

            return true; // Successfully updated
        }
        return false; // Robot not found
    }
    public synchronized boolean updateRobotState(int id, int state) {
        Robot robotToUpdate = getById(id);
        if (robotToUpdate != null) {
            robotToUpdate.setState(state);

            return true; // Successfully updated
        }
        return false; // Robot not found
    }



    public synchronized Robot getById(int id){
        for (Robot r : this.getRobotsList()) {
            if (id == r.getId())
                return r;
        }
        return null;
    }

    public synchronized int getDistrictByID(int id){
        for (Robot r : this.robotsList) {
            if (r.getId() == id) {
                return r.getDistrict();
            }
        }
        return 0;
    }
    public synchronized Robot deleteById(int id) {
        int district = getDistrictByID(id);
        if(district != 0){
            districtRobotCounts[district - 1]--;
        }
        for (Robot r : this.robotsList) {
            if (r.getId() == id) {
                this.robotsList.remove(r);

                return r;
            }
        }
        return null;
    }
}
