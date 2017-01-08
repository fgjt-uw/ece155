package lab3_201_14.uwaterloo.ca.lab3_201_14;

import android.graphics.PointF;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Timer;
import java.util.TimerTask;

import ca.uwaterloo.mapper.PositionListener;

/**
 * Created by Anthony on 2016-06-06.
 */

interface StepDetectorFSMConstants
{

    // Trigger ranges for the Finite-State Machine
    public final double rangeInitialMin = -0.5;
    public final double rangeInitialMax = 0;
    public final double rangePeakMin = 0.16;
    public final double rangePeakMax = 1.25;
    public final double rangeDropMin = -1.25;
    public final double rangeDropMax = -0.25;
    public final double rangeTooHigh = 5.00;

    // Constants indicating the states to the switch-case
    public final int stateInitial = 0;
    public final int statePeak = 1;
    public final int stateDrop = 2;
    public final int stateEnd = 3;

    // Largest and smallest differentials allowed from peak to drop
    public final double accelThresholdMin = 0.5;
    public final double accelThresholdMax = 7.0;

}


public class StepDetector implements StepDetectorFSMConstants
{

    private int currentState = stateInitial;
    public static int numberOfSteps = 0;
    private double tempMax = 0;
    private double tempMin = 0;

    private PositionListener positionListener;

    public StepDetector(PositionListener pl)
    {
        positionListener = pl;
    }

    // Finite-state machine for detecting footsteps
    public void inputData(double y, int index)
    {
        compareToMax(y);
        compareToMin(y);
        switch(currentState) {
            case stateInitial: // Detect resting range
                if (y > rangeInitialMin && y < rangeInitialMax) {
                    tempMax = 0;
                    tempMin = 0;
                    this.currentState = statePeak;
                }
                break;
            case statePeak: // Detect peak range
                if (y > rangePeakMin && y < rangePeakMax) {
                    this.currentState = stateDrop;
                }
                break;
            case stateDrop: // Detect drop range
                if (y > rangeDropMin && y < rangeDropMax) {
                    this.currentState = stateEnd;
                } else if (y > rangeTooHigh) {
                    this.currentState = stateInitial;
                }
                break;
            case stateEnd: // Detect return to resting range
                double peakToPeak = tempMax - tempMin;
                if (peakToPeak > accelThresholdMin && peakToPeak < accelThresholdMax && y > rangeInitialMin && y < rangeInitialMax) {
                    if(DisplacementManager.displaceUserInRoom(positionListener)) {
                        DisplacementManager.calculateDisplacement();
                        numberOfSteps++;
                    }
                    this.currentState = stateInitial;
                } else if(peakToPeak < accelThresholdMin || peakToPeak > accelThresholdMax) {
                    this.currentState = stateInitial;
                }
            default:
                break;
        }
    }

    public void compareToMax(double input)
    {
        if(input > tempMax) {
            tempMax = input;
        }
    }

    public void compareToMin(double input)
    {
        if(input < tempMin) {
            tempMin = input;
        }
    }

    public int getNumberOfSteps()
    {
        return numberOfSteps;
    }

    public static void resetNumberOfSteps()
    {
        numberOfSteps = 0;
    }

}