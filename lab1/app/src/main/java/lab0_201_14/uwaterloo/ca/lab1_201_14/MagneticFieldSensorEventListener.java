package lab0_201_14.uwaterloo.ca.lab1_201_14;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

/**
 * Created by frederick on 5/15/2016.
 */
public class MagneticFieldSensorEventListener implements SensorEventListener {

    TextView output;

    float x;
    float y;
    float z;

    public MagneticFieldSensorEventListener(TextView outputView) {
        output = outputView;
    }

    sensorMaxValue magneticMax = new sensorMaxValue();

    public void onAccuracyChanged(Sensor s, int i) {
    }

    public void onSensorChanged(SensorEvent se) {
        if(se.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {

            x = se.values[0];
            y = se.values[1];
            z = se.values[2];

            magneticMax.calcMaxX(x);
            magneticMax.calcMaxY(y);
            magneticMax.calcMaxZ(z);

            output.setText(String.valueOf("Magnetic Field: " + x + "μT, " + y + "μT, " + z + "μT") + "\n" + String.valueOf(magneticMax.getMaxString()) + "μT \n");
            // se.values[0] is x-axis, 1 y, 2 z; units microTesla

        }
    }
}
