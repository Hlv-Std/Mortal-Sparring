package model;

public class Vector4 extends Vector3 {
    public double w;

    public Vector4(){
        super();
        w = 0;
    }

    public Vector4(double x, double y, double z, double w){
        super(x,y,z);
        this.w = w;
    }

}
