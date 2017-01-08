package lab3_201_14.uwaterloo.ca.lab3_201_14;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

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
    private float[] orientation;
    private int currentAmountOfDataPoints = 0;

    public MagneticFieldSensorEventListener(TextView outputView) {
        output = outputView;
    }

    public void onAccuracyChanged(Sensor s, int i) { }

    public void onSensorChanged(SensorEvent se) {
        if(se.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            storeSensorValues(se);

            OrientationSensorEventListener.calcOrientation();
            orientation = OrientationSensorEventListener.getOrientationMatrix();

            if(currentAmountOfDataPoints % 10 == 0) {
                DisplacementManager.setStepAngle(orientation[0]);
            }

            output.setText(String.format("%.2f", orientation[0]) + " radians");
        }
        //graph.addPoint(orientation);
    }

    public void storeSensorValues(SensorEvent se) {
        x = se.values[0];
        y = se.values[1];
        z = se.values[2];
    }

    public static float[] getMagneticSensorReading(){
        float[] magneticSensorReadings = {x, y, z};
        return magneticSensorReadings;
    }

}
