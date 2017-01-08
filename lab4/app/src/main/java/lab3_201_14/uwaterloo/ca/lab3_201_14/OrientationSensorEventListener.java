package lab3_201_14.uwaterloo.ca.lab3_201_14;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;


/**
 * Created by frederick on 6/23/2016.
 */
public class OrientationSensorEventListener extends EventListener {

    private static float azimuth;
    private static float pitch;
    private static float roll;
    private static float[] accelerometerArray;
    private static float[] magneticFieldArray;

    public OrientationSensorEventListener(TextView outputView) {
        output = outputView;
    }

    public void onSensorChanged(SensorEvent se) { }

    public static void calcOrientation() {
        accelerometerArray = RealAccelerometerSensorEventListener.getAccelerometerReading();
        magneticFieldArray = MagneticFieldSensorEventListener.getMagneticSensorReading();
        if (accelerometerArray != null && magneticFieldArray != null) {
            float rotationMatrix[] = new float[9];
            float I[] = new float[9];
            boolean getRotationWorked = SensorManager.getRotationMatrix(rotationMatrix, I, accelerometerArray, magneticFieldArray);
            if (getRotationWorked) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(rotationMatrix, orientation);
                azimuth = orientation[0];
                pitch = orientation[1];
                roll = orientation[2];
            }
        }
    }

    public static float[] getOrientationMatrix() {
        float[] o = {azimuth, pitch, roll};
        return o;
    }

    public static float getAzimuth() {
        return azimuth;
    }

}

//}
