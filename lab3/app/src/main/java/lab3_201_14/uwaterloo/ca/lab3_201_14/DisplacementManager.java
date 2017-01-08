package lab3_201_14.uwaterloo.ca.lab3_201_14;

import java.lang.Math;

/**
 * Created by frederick on 6/25/2016.
 */

public class DisplacementManager {

    // Fields
    private static double westToEast = 0;
    private static double southToNorth = 0;
    private static double wTESteps = 0;
    private static double sTNSteps = 0;
    private static double stepAngle = 0;
    private static double stepSize = 0.63; // meters

    // Empty constructor
    public DisplacementManager() { }

    // Displacement calculations
    //    One version in terms of meters
    //    Other version in terms of steps
    public static double calculateXComponent(double angle) {
        return stepSize*Math.cos(angle);
    }

    public static double calculateYComponent(double angle) {
        return stepSize*Math.sin(angle);
    }

    public static double calculateXStep(double angle) { return Math.cos(angle); }

    public static double calculateYStep(double angle) { return Math.sin(angle); }

    // Calculated when a step has been detected: stateEnd in StepDetector's inputData method
    public static void calculateDisplacement() {
        southToNorth += calculateXComponent(stepAngle);
        westToEast += calculateYComponent(stepAngle);
        wTESteps += calculateXStep(stepAngle);
        sTNSteps += calculateYStep(stepAngle);
    }

    // Getters
    public static double getX() {
        return southToNorth;
    }

    public static double getY() {
        return westToEast;
    }

    public static double getXSteps() { return wTESteps; }

    public static double getYSteps() { return sTNSteps; }

    public static double getStepAngle() { return stepAngle; }

    // Setter
    public static void setStepAngle(double angle) { stepAngle = angle; }

    // Resetter
    public static void resetVector() {
        southToNorth = 0;
        westToEast = 0;
        sTNSteps = 0;
        wTESteps = 0;
        stepAngle = 0;
    }

}