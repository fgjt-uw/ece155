package lab3_201_14.uwaterloo.ca.lab3_201_14;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;
import java.lang.Math;

import ca.uwaterloo.sensortoy.LineGraphView;

/**
 * Created by frederick on 5/15/2016.
 */

public class MagneticFieldSensorEventListener implements SensorEventListener {

    private TextView output;
    private LineGraphView graph;
    private static float x;
    private static float y;
    private static float z;
    private float orientation;
    private int currentAmountOfDataPoints = 0;
    private double averageAngle = 0;
    private double sumOfXAngles = 0;
    private double sumOfYAngles = 0;
    private static DataFilter magneticFilter;

    public MagneticFieldSensorEventListener(TextView outputView) {
        output = outputView;
        magneticFilter = new DataFilter();
    }

    public void onAccuracyChanged(Sensor s, int i) { }

    public void onSensorChanged(SensorEvent se) {
        if(se.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            //storeSensorValues(se);
            magneticFilter.run(se);

            OrientationSensorEventListener.calcOrientation();
            orientation = OrientationSensorEventListener.getAzimuth();

            if(currentAmountOfDataPoints % 10 == 0) {
                averageAngle = calcAverageAngle(sumOfXAngles, sumOfYAngles);
                DisplacementManager.setStepAngle(averageAngle);
                resetAngleSums();
            } else {
                addToAngleVector(calcUnitVectorX(orientation),calcUnitVectorY(orientation));
            }
            currentAmountOfDataPoints++;

            //output.setText(String.format("%.2f", DisplacementManager.getStepAngle()) + " radians");
            output.setText(/*currentAmountOfDataPoints + "\n" + round(orientation) + "\n" + round(sumOfXAngles) + "\n" + round(sumOfYAngles) + "\n" +*/ "swag naysh\n" + round(DisplacementManager.getStepAngle()) + "\n" + round(DisplacementManager.getOffsetStepAngle()) + "\n" + DisplacementManager.compassDisplay());
        }
        //graph.addPoint(orientation);
    }

    public String round(double n) { return String.format("%.2f", n); }

    public double calcAverageAngle(double xSums, double ySums) { return Math.atan2(ySums, xSums); }

    public void addToAngleVector(double x, double y) {
        sumOfXAngles += x;
        sumOfYAngles += y;
    }

    public double calcUnitVectorX(float angle) { return Math.cos(angle); }

    public double calcUnitVectorY(float angle) { return Math.sin(angle); }

    public void resetAngleSums() {
        sumOfXAngles = 0;
        sumOfYAngles = 0;
    }

    public void storeSensorValues(SensorEvent se) {
        x = se.values[0];
        y = se.values[1];
        z = se.values[2];
    }

    public static float[] getMagneticSensorReading(){
        //return new float[]{x, y, z};
        return magneticFilter.getReadings();
    }

}
