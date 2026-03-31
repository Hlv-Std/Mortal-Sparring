package model;

public class Vector3 extends Vector2{
    public double z;

    public Vector3(){
        super();
        z = 0;
    }

    public Vector3(double x, double y, double z){
        super(x,y);
        this.z = z;
    }
}
