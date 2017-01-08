package lab2_201_14.uwaterloo.ca.lab2_201_14;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import ca.uwaterloo.sensortoy.LineGraphView;

import android.content.Context;

/**
 * Created by frederick on 5/17/2016.
 */
public class AccelerometerEventListener  implements SensorEventListener {
    Context context;
    TextView output;
    LineGraphView graph;

    float x;
    float y;
    float z;

    public AccelerometerEventListener(TextView outputView,LineGraphView graphView) {   //ADDED CONTEXT CONTEXT

        output = outputView;
        graph = graphView;


    }
    sensorMaxValue accelerometerMax = new sensorMaxValue();

    public void onAccuracyChanged(Sensor s, int i) {

    }

    public void onSensorChanged(SensorEvent se) {

        if(se.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
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
        //JUNE 4
        try
        {
            // external storage
            String i= "hey world "; //TODO
            FileOutputStream os = new FileOutputStream(new File(context.getExternalFilesDir(null), "external.txt"));
            PrintWriter osw = new PrintWriter(new OutputStreamWriter(os));
            osw.println(i);
            osw.close();

        }catch(IOException e){
            //TODO HANDLE EXCEPTION
        }
    }
/*JUNE 1
//    Context context = Context.MODE_PRIVATE;
//
     File file = new File(context.getFilesDir(),file);
    PrintWriter printWriter = new PrintWriter("file.txt");
       printWriter.append("Hello World" );
    printWriter.close();
*/



}
