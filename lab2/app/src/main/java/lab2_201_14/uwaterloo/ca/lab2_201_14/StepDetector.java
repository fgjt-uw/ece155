package lab2_201_14.uwaterloo.ca.lab2_201_14;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Timer;
import java.util.TimerTask;

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
    private double rangeTooHigh = 5.00;

    // Constants indicating the states to the switch-case
    private static final int stateInitial = 0;
    private static final int statePeak = 1;
    private static final int stateDrop = 2;
    private static final int stateEnd = 3;
    private int currentState = stateInitial;


    public static int numberOfSteps = 0;

    private boolean stepMinTimeComplete = true;
    private boolean stepMaxTimeComplete = false;
    Timer stepMinTimer;
    Timer stepMaxTimer;
    private int stepMinTime = 350;
    private int stepMaxTime = 1500;
    TimerTask stepMinTimerTask;
    TimerTask stepMaxTimerTask;

    private double tempMax = -1;
    private double tempMin = 0;
    private double accelThresholdMin = 0.5;
    private double accelThresholdMax = 7.0;


    // Finite-state machine for detecting footsteps
    //    index isn't actually needed here but I'm too lazy to remove it...
    public void inputData(double y, int index) {

        index += 2; // Small bugfix to align index values with Excel

        compareToMax(y);
        compareToMin(y);

        switch(currentState) {

            case stateInitial:
                if (y > rangeInitialMin && y < rangeInitialMax) {
                    tempMax = 0;
                    tempMin = 0;
                    this.currentState = statePeak;
                    //setStepMinTimeComplete(false);
                    //setStepMaxTimeComplete(false);
                    //startMinTimer(stepMinTime);
                    //startMaxTimer(stepMaxTime);
                }
                break;

            case statePeak:
                if (y > rangePeakMin && y < rangePeakMax) {
                    this.currentState = stateDrop;
                }
                break;

            case stateDrop:
                if (y > rangeDropMin && y < rangeDropMax) {
                    this.currentState = stateEnd;
                } else if (y > rangeTooHigh) {
                    this.currentState = stateInitial;
                }
                break;
            case stateEnd:
                double peakToPeak = tempMax - tempMin;
                if (stepMinTimeComplete == true && peakToPeak > accelThresholdMin && peakToPeak < accelThresholdMax && y > rangeInitialMin && y < rangeInitialMax) {
                    this.numberOfSteps++;
                    //stopMinTimerTask();
                    this.currentState = stateInitial;
                } else if(stepMinTimeComplete == false || peakToPeak < accelThresholdMin || peakToPeak > accelThresholdMax) {
                    this.currentState = stateInitial;
                }

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

    public String getCurrentState() {

        if (currentState == 0)
            return "Initial state";
        else if (currentState == 1)
            return "Peak state";
        else if (currentState == 2)
            return "Drop state";
        else if (currentState == 3)
            return "End state";
        return "Hello";

    }

    public void setCurrentState(int currState) {

        this.currentState = currState;

    }

    public void setStepMinTimeComplete(boolean bool) {

        this.stepMinTimeComplete = bool;

    }

    public void setStepMaxTimeComplete(boolean bool) {

        this.stepMaxTimeComplete = bool;
    }

    public void startMinTimer(int period) {

        stepMinTimer = new Timer();
        initializeMinTimerTask();
        stepMinTimer.schedule(stepMinTimerTask, period);

    }

    public void startMaxTimer(int period) {

        stepMaxTimer = new Timer();
        initializeMaxTimerTask();
        stepMaxTimer.schedule(stepMaxTimerTask, period);

    }

    public void stopMinTimerTask() {

        if (stepMinTimer != null) {
            stepMinTimer.cancel();
            stepMinTimer = null;
        }

    }

    public void stopMaxTimerTask() {

        if (stepMaxTimer != null) {
            stepMaxTimer.cancel();
            stepMaxTimer = null;
        }

    }

    public void initializeMinTimerTask() {

        stepMinTimerTask = new TimerTask() {
            @Override
            public void run() {
                setStepMinTimeComplete(true);
            }
        };

    }

    public void initializeMaxTimerTask() {

        stepMaxTimerTask = new TimerTask() {
            @Override
            public void run() {
                setStepMaxTimeComplete(true);
                setCurrentState(0);
            }
        };

    }

    public void compareToMax(double input) {

        if(input > tempMax) {
            tempMax = input;
        }

    }

    public void compareToMin(double input) {

        if(input < tempMin) {
            tempMin = input;
        }

    }

    public String printMaxMin() {

        return String.format("%.3f",this.tempMax) + " " + String.format("%.3f",this.tempMin) + "\nMax-min diff: " + String.format("%.3f", this.tempMax - tempMin);

    }

    public static void resetNumberOfSteps(){

        numberOfSteps = 0;

    }

}