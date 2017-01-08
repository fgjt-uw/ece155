package lab3_201_14.uwaterloo.ca.lab3_201_14;

import android.graphics.PointF;

import java.lang.Math;

import ca.uwaterloo.mapper.PositionListener;

/**
 * Created by frederick on 6/25/2016.
 */

interface CompassAngles
{
    public final double angleOffset = 25*(Math.PI/180); //offset angle converted to radians

    public final double south = (7.0/8.0)*Math.PI;
    public final double sEast = (5.0/8.0)*Math.PI;
    public final double east = (5.0/8.0)*Math.PI;
    public final double nEast = (1.0/8.0)*Math.PI;
    public final double north = (-1.0/8.0)*Math.PI;
    public final double nWest = (-3.0/8.0)*Math.PI;
    public final double west = (-5.0/8.0)*Math.PI;
    public final double sWest = (-7.0/8.0)*Math.PI;
}


interface DisplacementConstants
{
    public final double stepSize = 1.0; // meters\
}


public class DisplacementManager implements DisplacementConstants, CompassAngles {

    // Fields
    private static double westToEast;
    private static double southToNorth;
    private static double roomY;
    private static double roomX;
    private static double wTESteps;
    private static double sTNSteps;
    private static double stepAngle;
    private static double offsetStepAngle;

    // Empty constructor
    public DisplacementManager() { }

    // Displacement calculations
    //    One version in terms of meters (step size assumed)
    //    Other version in terms of steps (nothing assumed)
    public static double calculateXComponent(double angle) {
        return stepSize*Math.cos(angle);
    }

    public static double calculateYComponent(double angle) {
        return stepSize*Math.sin(angle);
    }

    public static double calculateXStep(double angle) { return Math.cos(angle); }

    public static double calculateYStep(double angle) { return Math.sin(angle); }

    // Calculated when a step has been detected: called in StepDetector's inputData method
    public static void calculateDisplacement()
    {
        southToNorth += calculateXComponent(stepAngle);
        westToEast += calculateYComponent(stepAngle);
        wTESteps += calculateXStep(stepAngle);
        sTNSteps += calculateYStep(stepAngle);
        roomX += calculateXComponent(offsetStepAngle);
        roomY += calculateYComponent(offsetStepAngle);
    }

    // Displace user on the MapView when a step has been detected: called in StepDetector's inputData method
    public static boolean displaceUserInRoom(PositionListener pl)
    {
        PointF newP = Navigator.getUser();
        PointF p = Navigator.pointOffset(newP, (float)calculateXComponent(offsetStepAngle), (float)calculateYComponent(offsetStepAngle));
        if (Navigator.isNotWall(newP, p)) {
            Navigator.setUser(p);
            MainActivity.mv.setUserPoint(p);
            MainActivity.nav.determineRoute();
            MainActivity.nav.isDestination(p, Navigator.getDestination());
            return true;
        }
        return false;
    }

    public static String compassDisplay()
    {
        if((stepAngle > south) || (stepAngle <= sWest))
            return "S";
        else if(stepAngle > sEast)
            return "SE";
        else if(stepAngle > east)
            return "E";
        else if(stepAngle > nEast)
            return "NE";
        else if(stepAngle > north)
            return "N";
        else if(stepAngle > nWest)
            return "NW";
        else if(stepAngle > west)
            return "W";
        else if(stepAngle > sWest)
            return "SW";
        else
            return "NaN";
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

    public static double getRoomX() { return roomX; }

    public static double getRoomY() { return roomY; }

    public static double getStepAngle() { return stepAngle; }

    public static double getOffsetStepAngle() { return offsetStepAngle; }

    // Setter
    public static void setStepAngle(double angle)
    {
        stepAngle = angle;
        offsetStepAngle = 2*Math.PI - (((Math.PI)/2 - angleOffset) - angle);
    }

    // Resetter
    public static void resetVector()
    {
        southToNorth = 0;
        westToEast = 0;
        sTNSteps = 0;
        wTESteps = 0;
        stepAngle = 0;
    }


}