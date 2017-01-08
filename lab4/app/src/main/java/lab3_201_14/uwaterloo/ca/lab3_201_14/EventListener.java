package lab3_201_14.uwaterloo.ca.lab3_201_14;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

/**
 * Created by frederick on 6/20/2016.
 */

abstract class EventListener implements SensorEventListener{

    public float x;
    public float y;
    public float z;
    public TextView output;

    public void onAccuracyChanged(Sensor s, int i) { }

    // Implement onSensorChanged on your own

}
