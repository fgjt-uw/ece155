package lab2_201_14.uwaterloo.ca.lab2_201_14;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Anthony on 2016-06-06.
 */

public class WalkingDataFileInput {

    // Arrays for each acceleration value type
    static double[] xAccel = new double[3000];
    static double[] yAccel = new double[3000];
    static double[] zAccel = new double[3000];
    static double[] filteredxAccel = new double[3000];
    static double[] filteredyAccel = new double[3000];
    static double[] filteredzAccel = new double[3000];

    // Trigger ranges for the Finite-State Machine
    static double rangeInitialMin = -0.5;
    static double rangeInitialMax = 0;
    static double rangePeakMin = 0.16;
    static double rangePeakMax = 1.25;
    static double rangeDropMin = -1.25;
    static double rangeDropMax = -0.25;

    // Constants indicating the states to the switch-case
    private static final int stateInitial = 0;
    private static final int statePeak = 1;
    private static final int stateDrop = 2;
    private static final int stateReturn = 3;
    private static int currentState = stateInitial;

    static int numberOfSteps = 0;

    // Main loop
    public static void main(String[] args) throws IOException {

        // Initializing input
        BufferedReader inputStream = null;

        // Getting all the data from the .txt and separating into different arrays for each acceleration value type
        try {
            // inputStream = new BufferedReader(new FileReader("walking_data.txt"));
            inputStream = new BufferedReader(new FileReader("external_16steps+14steps.txt"));

            String line = inputStream.readLine();
            int i = 0;

            while((line = inputStream.readLine()) != null && i < 2499) {
                String[] tempArray = line.split(" "); // Separate the columns by space character

                // Store data into appropriate array
                xAccel[i] = Double.parseDouble(tempArray[0]);
                yAccel[i] = Double.parseDouble(tempArray[1]);
                zAccel[i] = Double.parseDouble(tempArray[2]);
                filteredxAccel[i] = Double.parseDouble(tempArray[3]);
                filteredyAccel[i] = Double.parseDouble(tempArray[4]);
                filteredzAccel[i] = Double.parseDouble(tempArray[5]);

                i++;
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }

        // Inputting filtered-y acceleration data into the FSM
        for (int z = 0; z < 2500; z++) {
            inputData(filteredyAccel[z], z);
        }

        System.out.println("\nNumber of steps counted: " + numberOfSteps);

    }

    // Finite-state machine for detecting footsteps
    static public void inputData(double y, int index) {

        index+=2; // Small bugfix to align index values with Excel

        switch(currentState) {

            case stateInitial:
                if (y > rangeInitialMin && y < rangeInitialMax) {
                    System.out.println("Initial to Peak: " + y + " at " + index);
                    currentState = statePeak;
                }
                break;

            case statePeak:
                if (y > rangePeakMin && y < rangePeakMax) {
                    System.out.println("Peak to Drop: " + y + " at " + index);
                    currentState = stateDrop;
                }
                break;

            case stateDrop:
                if (y > rangeDropMin && y < rangeDropMax) {
                    // currentState = stateReturn;
                    currentState = stateInitial;
                    numberOfSteps++;
                    System.out.println("Step " + numberOfSteps + " complete: " + y + " at " + index + "\n");
                }
                break;

            case stateReturn:
                if (y > -0.5 && y < 0) {
                    currentState = stateInitial;
                    numberOfSteps++;
                    System.out.println("Step at " + index);
                }
                break;

            default:
                break;

        }

    }

}