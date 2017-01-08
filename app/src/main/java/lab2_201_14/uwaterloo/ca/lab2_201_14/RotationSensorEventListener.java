package lab2_201_14.uwaterloo.ca.lab2_201_14;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

/**
 * Created by frederick on 5/17/2016.
 */
public class RotationSensorEventListener implements SensorEventListener {
    TextView output;

    float x;
    float y;
    float z;

    public RotationSensorEventListener(TextView outputView) {
        output = outputView;
    }

    sensorMaxValue rotationMax = new sensorMaxValue();

    public void onAccuracyChanged(Sensor s, int i) {
    }

    public void onSensorChanged(SensorEvent se) {
        if(se.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {

            x = se.values[0];
            y = se.values[1];
            z = se.values[2];

            rotationMax.calcMaxX(x);
            rotationMax.calcMaxY(y);
            rotationMax.calcMaxZ(z);

            output.setText("Rotation: " + String.valueOf(x + ", " + y + ", " + z) + "\n" + String.valueOf(rotationMax.getMaxString()+ "\n"));
            // se.values[0] is x-axis, [1] y, [2] z
        }
    }
}
