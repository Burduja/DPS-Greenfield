    package Robot;

    import java.util.ArrayList;
    import java.time.LocalDateTime;
    import java.time.format.DateTimeFormatter;
    import java.util.Arrays;

    // class used by each robot to store the other robots in the network

    public class RobotsList {

        protected CleaningRobot cleaningRobot;
        protected ArrayList<CleaningRobot> robotsList;

        public RobotsList(CleaningRobot cleaningRobot){
            this.cleaningRobot = cleaningRobot;
            this.robotsList = new ArrayList<CleaningRobot>();
        }

        // GETTERS / SETTERS
        public CleaningRobot getRobot() {
            return cleaningRobot;
        }
        public synchronized  ArrayList<CleaningRobot> getRobots() {
            return new ArrayList<CleaningRobot>(robotsList);
        }
        public void setRobotsList(ArrayList<CleaningRobot> robotsList) {
            this.robotsList = robotsList;
        }
        public void setRobot(CleaningRobot cleaningRobot) {
            this.cleaningRobot = cleaningRobot;
        }
        public CleaningRobot getRobotById(int robotId) {
            for (CleaningRobot cleaningRobot : this.robotsList) {
                if (cleaningRobot.getId() == robotId) {
                    return cleaningRobot;
                }
            }
            return null;
        }
        public synchronized int getPopulationByDistrict(int district){

            return getPopulationPerDistrict()[district - 1];
        }
        public int[] getPopulationPerDistrict() {
            int[] populationPerDistrict = new int[4];
            populationPerDistrict[this.getRobot().getDistrict() - 1]++;

            for (CleaningRobot robot : robotsList) {
                int district = robot.getDistrict();
                if (district != -1) {
                    populationPerDistrict[district - 1]++;
                }
            }


            return populationPerDistrict;
        }

        // ADD / REMOVE
        public synchronized void removeRobot(int robotId) {
            robotsList.removeIf(r -> r.getId() == robotId);
            System.out.println("[RobotsList] Robot " + robotId + " removed due to missed heartbeat");
        }
        public synchronized void addRobot(CleaningRobot cleaningRobot){
            robotsList.add(cleaningRobot);
        }

        // BALANCE
        public synchronized int determineTargetDistrictForRelocation() {
            int[] populationPerDistrict = new int[4]; // Assuming 4 districts numbered from 1 to 4

            // Count the current number of robots in each district
            for (CleaningRobot robot : this.robotsList) {
                if (robot.getDistrict() > 0 && robot.getDistrict() <= 4) {
                    populationPerDistrict[robot.getDistrict() - 1]++;
                }
            }
            populationPerDistrict[cleaningRobot.getDistrict() - 1]++;
            int targetDistrict = cleaningRobot.getDistrict();
            int minPopulation = populationPerDistrict[0]; // Initialize with the population of the first district
            int myPopulation = populationPerDistrict[cleaningRobot.getDistrict() - 1];



            for (int i = 0; i < populationPerDistrict.length; i++) {
                if (populationPerDistrict[i] < minPopulation) {
                    minPopulation = populationPerDistrict[i];
                    targetDistrict = i + 1; // District numbers start from 1
                }
            }
            if((myPopulation > minPopulation + 1) && myPopulation > 1 && (targetDistrict != cleaningRobot.getDistrict())){
                return targetDistrict;
            }
            else {
                return -1;
            }

        }
        public synchronized void updatePosition(int robotID, int posX, int posY, int district){
            if(robotID == cleaningRobot.getId()){
                cleaningRobot.setPosX(posX);
                cleaningRobot.setPosY(posY);
                cleaningRobot.setDistrict(district);
            }
            else {
                for(CleaningRobot cleaningRobot1 : robotsList){
                    if(cleaningRobot1.getId() == robotID){
                        cleaningRobot1.setPosX(posX);
                        cleaningRobot1.setPosY(posY);
                        cleaningRobot1.setDistrict(district);
                    }
                }
            }
        }
        // VISUALIZATIONS
        public void printIDs(){
            for (CleaningRobot cleaningRobot :
                 this.robotsList) {
                System.out.println(cleaningRobot.getId() + " distr " + cleaningRobot.getDistrict());
            }
        }














    }
