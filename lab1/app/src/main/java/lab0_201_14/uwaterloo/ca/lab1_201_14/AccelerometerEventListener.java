package lab0_201_14.uwaterloo.ca.lab1_201_14;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

import ca.uwaterloo.sensortoy.LineGraphView;

/**
 * Created by frederick on 5/17/2016.
 */
public class AccelerometerEventListener  implements SensorEventListener {

    TextView output;
    LineGraphView graph;

    float x;
    float y;
    float z;

    public AccelerometerEventListener(TextView outputView,LineGraphView graphView) {

        output = outputView;
        graph = graphView;

    }
    sensorMaxValue accelerometerMax = new sensorMaxValue();

    public void onAccuracyChanged(Sensor s, int i) {

    }

    public void onSensorChanged(SensorEvent se) {

        if(se.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            x = se.values[0];
            y = se.values[1];
            z = se.values[2];

            output.setText("Accelerometer: " + String.valueOf(x + " m/s^2, " + y + " m/s^2, " + z + " m/s^2") + "\n" + String.valueOf(accelerometerMax.getMaxString()) + "\n");

            accelerometerMax.calcMaxX(x);
            accelerometerMax.calcMaxY(y);
            accelerometerMax.calcMaxZ(z);
            // se.values[0] is x-axis, 1 y, 2 z; units m/s^2

            graph.addPoint(se.values);
        }

    }
}
