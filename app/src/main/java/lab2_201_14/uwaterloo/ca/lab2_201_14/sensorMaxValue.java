package lab2_201_14.uwaterloo.ca.lab2_201_14;

/**
 * Created by frederick on 5/30/2016.
 */
public class sensorMaxValue {

    protected float x;
    protected float y;
    protected float z;
    protected float maxX = 0;
    protected float maxY = 0;
    protected float maxZ = 0;

    public sensorMaxValue() { };

    public float getX() { return this.x; }
    public float getY() { return this.y; }
    public float getZ() { return this.z; }  //unused but possibly useful functions

    public float getMaxX() {
        return Math.abs(this.maxX);
    }
    public float getMaxY() {
        return Math.abs(this.maxY);
    }
    public float getMaxZ() {
        return Math.abs(this.maxZ);
    }

    public String getMaxString() {
        String maxString = "Max x, y, z: " + String.format("(%f, %f, %f)", getMaxX(), getMaxY(), getMaxZ());
        return maxString;
    }
    public void calcMaxX(float x){
        if( Math.abs(maxX) < Math.abs(x)) {
            this.maxX = x;
        }
    }
    public void calcMaxY(float y){
        if( Math.abs(maxY) < Math.abs(y)) {
            this.maxY = y;
        }
    }
    public void calcMaxZ(float z){
        if( Math.abs(maxZ) < Math.abs(z)) {
            this.maxZ = z;
        }
    }

}
