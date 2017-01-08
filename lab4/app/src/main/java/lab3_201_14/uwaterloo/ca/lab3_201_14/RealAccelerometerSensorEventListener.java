package lab3_201_14.uwaterloo.ca.lab3_201_14;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.widget.TextView;

/**
 * Created by frederick on 6/25/2016.
 */

public class RealAccelerometerSensorEventListener extends EventListener {

    private static float x = 0;
    private static float y = 0;
    private static float z = 0;
    private TextView output;
    private static DataFilter accelerometerFilter;

    public RealAccelerometerSensorEventListener(){
        accelerometerFilter = new DataFilter();
    }

    public void onSensorChanged(SensorEvent se){
        if(se.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //storeSensorValues(se);
            accelerometerFilter.run(se);
        }
    }

    public void storeSensorValues(SensorEvent se) {
        x = se.values[0];
        y = se.values[1];
        z = se.values[2];
    }

    public static float[] getAccelerometerReading() {
        //return new float[]{x, y, z};
        return accelerometerFilter.getReadings();
    }

    public void screenOutput() { output.setText("x: " + String.valueOf(x) + " y: " + String.valueOf(y)+ " z: " + String.valueOf(z)); }

}
