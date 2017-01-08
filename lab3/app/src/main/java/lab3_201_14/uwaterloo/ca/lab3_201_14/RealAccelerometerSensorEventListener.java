package lab3_201_14.uwaterloo.ca.lab3_201_14;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.widget.TextView;

/**
 * Created by frederick on 6/25/2016.
 */

public class RealAccelerometerSensorEventListener extends EventListener {

    private static float x;
    private static float y;
    private static float z;
    private TextView output;

    public RealAccelerometerSensorEventListener(){ }

    public void onSensorChanged(SensorEvent se){
        if(se.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            storeSensorValues(se);
        }
    }

    public void storeSensorValues(SensorEvent se) {
        x = se.values[0];
        y = se.values[1];
        z = se.values[2];
    }

    public static float[] getAccelerometerReading() { return new float[]{x, y, z}; }

    public void screenOutput() { output.setText("x: " + String.valueOf(x) + " y: " + String.valueOf(y)+ " z: " + String.valueOf(z)); }

}
