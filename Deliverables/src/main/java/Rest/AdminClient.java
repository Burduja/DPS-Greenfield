package Rest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AdminClient {
    public static String restBaseAddressRobots = "http://localhost:1337/robots/";
    public static String restBaseAddressStatistics = "http://localhost:1337/statistics/";
    public static Client client = Client.create();
    public static Scanner sc = new Scanner(System.in);

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_ORANGE = "\u001B[33m"; // Approximation for orange using ANSI yellow
    private static final String ANSI_RED = "\u001B[31m";
    static String commandList = "\nAvailable methods:\n\n" +
            "\t(1) Get list of robots in Greenfield\n" +
            "\t(2) Get average pollution by a specific robot\n" +
            "\t(3) Get average pollution between timestamps\n" +
            "\t(4) Show Live Map\n" +
            "\t(5) Quit\n\n" +
            "Insert a command between 1 and 4: ";

    private static void getRobots() {
        WebResource webResource = client.resource(restBaseAddressRobots + "get");
        ClientResponse response = webResource.type("application/json").get(ClientResponse.class);

        try {
            JSONArray r = new JSONArray(response.getEntity(String.class));
            System.out.println((r.length() > 0) ? "Robots in Greenfield: \n" : "No robots found\n");
            for (int i = 0; i < r.length(); i++) {
                JSONObject robot = r.getJSONObject(i);
                System.out.println((i+1) + ". Robot: " + "\n\t- id: " + robot.getInt("id")
                        + "\n\t- ip: " + robot.getString("ip") + "\n\t- port: " + robot.getInt("port") + '\n');
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void getAveragePollutionByRobot() {
        // Fetch the list of robots
        WebResource webResource = client.resource(restBaseAddressRobots + "get");
        ClientResponse response = webResource.type("application/json").get(ClientResponse.class);

        try {
            JSONArray robots = new JSONArray(response.getEntity(String.class));
            if (robots.length() == 0) {
                System.out.println("No robots found.");
                return;
            }

            System.out.println("Available robots:");
            for (int i = 0; i < robots.length(); i++) {
                JSONObject robot = robots.getJSONObject(i);
                System.out.println((i + 1) + ". Robot ID: " + robot.getInt("id"));
            }

            System.out.print("\nEnter the number corresponding to the robot: ");
            int choice = sc.nextInt();
            if (choice < 1 || choice > robots.length()) {
                System.out.println("Invalid robot selection.");
                return;
            }

            int robotId = robots.getJSONObject(choice - 1).getInt("id");

            // Now get the count of measurements for the selected robot
            webResource = client.resource(restBaseAddressStatistics + "countByRobot?robotId=" + robotId);
            response = webResource.type("application/json").get(ClientResponse.class);
            int count = Integer.parseInt(response.getEntity(String.class));

            System.out.println("Number of measurements for robot " + robotId + ": " + count);
            System.out.print("Enter the number of measurements (n): ");
            int n = sc.nextInt();
            if (n > count) {
                System.out.println("Number exceeds the count of measurements. Using the maximum available: " + count);
                n = count;
            }

            // Fetch the average pollution for the selected robot
            webResource = client.resource(restBaseAddressStatistics + "averageByRobot?robotId=" + robotId + "&measurementsCount=" + n);
            response = webResource.type("application/json").get(ClientResponse.class);
            System.out.println("Average pollution for robot " + robotId + ": " + response.getEntity(String.class));
        } catch (JSONException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private static String getColorByState(int state) {
        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_GREEN = "\u001B[32m";
        final String ANSI_ORANGE = "\u001B[33m"; // Approximation for orange using ANSI yellow
        final String ANSI_RED = "\u001B[31m";

        switch (state) {
            case 0:
                return ANSI_GREEN;
            case 1:
                return ANSI_ORANGE;
            case 2:
                return ANSI_RED;
            default:
                return ANSI_RESET;
        }
    }
    private static int visibleLength(String str) {
        String withoutAnsi = str.replaceAll("\\u001B\\[[;\\d]*m", "");
        return withoutAnsi.length();
    }
    private static void printCityMap() {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);
        // Print the grid to the console
        System.out.println("City Map at : " + formattedNow);
        WebResource webResource = client.resource(restBaseAddressRobots + "get");
        ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);

        try {
            JSONArray robotsArray = new JSONArray(response.getEntity(String.class));
            ArrayList<String>[][] grid = new ArrayList[10][10];
            int maxChars = 3; // Initialize with a minimum value to accommodate small IDs

            // Initialize grid
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    grid[i][j] = new ArrayList<>();
                }
            }

            // Populate grid and update maxChars considering only the length of robot ID as a string
            for (int i = 0; i < robotsArray.length(); i++) {
                JSONObject robot = robotsArray.getJSONObject(i);
                JSONArray coordinates = robot.getJSONArray("coordinates");
                int x = coordinates.getInt(0);
                int y = coordinates.getInt(1);
                int state = robot.getInt("state");
                int robotId = robot.getInt("id");

                String coloredId = getColorByState(state) + robotId + ANSI_RESET;
                grid[x][y].add(coloredId);

                maxChars = Math.max(maxChars, String.valueOf(robotId).length() + 1);
            }

            // Adjusting the format string to include padding and ensure uniform cell width
            String cellFormat = " %-" + maxChars + "s |";

            // Printing the city map
            for (ArrayList<String>[] row : grid) {
                System.out.print("|"); // Start border for each row
                for (ArrayList<String> cell : row) {
                    if (cell.isEmpty()) {
                        System.out.printf(cellFormat, ""); // Empty cell
                    } else {
                        StringBuilder cellContent = new StringBuilder();
                        for (String coloredId : cell) {
                            cellContent.append(coloredId).append(" ");
                        }
                        System.out.printf(cellFormat, cellContent.toString());
                    }
                }
                System.out.println(); // End of row
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void showLiveMap() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable periodicTask = () -> {
            printCityMap();
            System.out.println("Type 'ok' and press Enter to return to the menu.");
        };

        executor.scheduleAtFixedRate(periodicTask, 0, 3, TimeUnit.SECONDS);

        while (true) {
            String input = sc.next();
            if ("ok".equalsIgnoreCase(input)) {
                executor.shutdownNow();
                break;
            }
        }
    }

    private static void getAveragePollutionBetweenTimestamps() {
        // Fetch earliest and latest timestamps
        WebResource webResource = client.resource(restBaseAddressStatistics + "earliestTimestamp");
        ClientResponse response = webResource.type("application/json").get(ClientResponse.class);
        long earliestTimestamp, latestTimestamp;
        try {
            earliestTimestamp = Long.parseLong(response.getEntity(String.class));

            webResource = client.resource(restBaseAddressStatistics + "latestTimestamp");
            response = webResource.type("application/json").get(ClientResponse.class);
            latestTimestamp = Long.parseLong(response.getEntity(String.class));

            System.out.println("Earliest timestamp: " + earliestTimestamp);
            System.out.println("Latest timestamp: " + latestTimestamp);

            System.out.print("\nEnter the start timestamp (t1) or type 'all': ");
            String t1Input = sc.next();
            long t1;
            long t2;

            if (t1Input.equalsIgnoreCase("all")) {
                t1 = earliestTimestamp;
                t2 = latestTimestamp;
            } else {
                t1 = Long.parseLong(t1Input);
                System.out.print("Enter the end timestamp (t2): ");
                t2 = sc.nextLong();
                if (t1 < earliestTimestamp || t2 > latestTimestamp || t1 > t2) {
                    System.out.println("Invalid timestamp range.");
                    return;
                }
            }

            webResource = client.resource(restBaseAddressStatistics + "averageBetweenTimestamps?t1=" + t1 + "&t2=" + t2);
            response = webResource.type("application/json").get(ClientResponse.class);
            System.out.println("Average pollution between " + t1 + " and " + t2 + ": " + response.getEntity(String.class));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("==== Greenfield ADMIN CLIENT ====\n");
        int command;
        boolean exit = false;
        while (!exit) {
            System.out.print(commandList);
            try {
                command = sc.nextInt();
                switch (command) {
                    case 1: getRobots(); break;
                    case 2: getAveragePollutionByRobot(); break;
                    case 3: getAveragePollutionBetweenTimestamps(); break;
                    case 5: exit = true; break;
                    case 4: showLiveMap(); break;
                    default:
                        System.out.println("Please enter a valid command.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please try again.");
                sc.nextLine(); // clear buffer
            }
        }
        sc.close();
    }
}
