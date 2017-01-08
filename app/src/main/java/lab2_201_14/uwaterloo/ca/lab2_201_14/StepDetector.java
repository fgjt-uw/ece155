package lab2_201_14.uwaterloo.ca.lab2_201_14;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Anthony on 2016-06-06.
 */

public class StepDetector {

    // Trigger ranges for the Finite-State Machine
    private double rangeInitialMin = -0.5;
    private double rangeInitialMax = 0;
    private double rangePeakMin = 0.16;
    private double rangePeakMax = 1.25;
    private double rangeDropMin = -1.25;
    private double rangeDropMax = -0.25;

    // Constants indicating the states to the switch-case
    private static final int stateInitial = 0;
    private static final int statePeak = 1;
    private static final int stateDrop = 2;
    private static final int stateReturn = 3;
    private int currentState = stateInitial;

    private int numberOfSteps = 0;


    // Finite-state machine for detecting footsteps
    //    index isn't actually needed here but I'm too lazy to remove it...
    public void inputData(double y, int index) {

        index += 2; // Small bugfix to align index values with Excel

        switch(currentState) {

            case stateInitial:
                if (y > rangeInitialMin && y < rangeInitialMax) {
                    System.out.println("Initial to Peak: " + y + " at " + index);
                    this.currentState = statePeak;
                }
                break;

            case statePeak:
                if (y > rangePeakMin && y < rangePeakMax) {
                    System.out.println("Peak to Drop: " + y + " at " + index);
                    this.currentState = stateDrop;
                }
                break;

            case stateDrop:
                if (y > rangeDropMin && y < rangeDropMax) {
                    // currentState = stateReturn;
                    this.currentState = stateInitial;
                    this.numberOfSteps++;
                    System.out.println("Step " + this.numberOfSteps + " complete: " + y + " at " + index + "\n");
                }
                break;

            case stateReturn:
                if (y > -0.5 && y < 0) {
                    this.currentState = stateInitial;
                    this.numberOfSteps++;
                    System.out.println("Step at " + index);
                }
                break;

            default:
                break;

        }

    }

    public int getNumberOfSteps() {

        return numberOfSteps;

    }

    // Setter methods for the ranges so separate ranges can be tested at once
    public void setRangeInitial(double min, double max) {

        this.rangeInitialMin = min;
        this.rangeInitialMax = max;

    }

    public void setRangePeak(double min, double max) {

        this.rangePeakMin = min;
        this.rangePeakMax = max;

    }

    public void setRangeDrop(double min, double max) {

        this.rangeDropMin = min;
        this.rangeDropMax = max;

    }

}