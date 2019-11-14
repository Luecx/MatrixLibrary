package core.vector;

import core.matrix.dense.DenseMatrix;

public class Vector3d extends Vector<Vector3d> {

    private double x,y,z;

    public Vector3d() {
        super(3);
    }

    public Vector3d(double x, double y, double z) {
        this();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3d(Vector<?> other){
        super(other,3);
    }

    @Override
    public void genArrays() {

    }

    @Override
    public void setValue(int index, double val) {
        switch (index){
            case 0: x=val;
            case 1: y=val;
            case 2: z=val;
        }
    }

    @Override
    public double getValue(int index) {
        switch (index){
            case 0: return x;
            case 1: return y;
            case 2: return z;
            default: return 0;
        }
    }


    @Override
    public Vector3d self_negate() {
        this.x = -x;
        this.y = -y;
        this.z = -z;
        return this;
    }

    @Override
    public Vector3d self_add(Vector3d other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
        return this;
    }

    @Override
    public Vector3d self_sub(Vector3d other) {
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
        return this;
    }

    @Override
    public Vector3d self_scale(double factor) {
        this.x *= factor;
        this.y *= factor;
        this.z *= factor;
        return this;
    }

    @Override
    public Vector3d scale(double factor) {
        return new Vector3d(this.x * factor, factor * this.y, factor * this.z);
    }

    @Override
    public Vector3d negate() {
        return new Vector3d(-this.x, -this.y, -this.z);
    }

    @Override
    public Vector3d add(Vector3d other) {
        return new Vector3d(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    @Override
    public Vector3d sub(Vector3d other) {
        return new Vector3d(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    @Override
    public double innerProduct(Vector3d other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    @Override
    public DenseMatrix outerProduct(Vector3d other) {
        return new DenseMatrix(new double[][]{
                {this.x * other.x, this.x * other.y, this.x * other.z},
                {this.y * other.x, this.y * other.y, this.y * other.z},
                {this.z * other.x, this.z * other.y, this.z * other.z}});
    }

    public Vector3d cross(Vector3d other){
        return new Vector3d(
                this.y * other.z - this.z * other.y,
                this.z * other.x - this.x * other.z,
                this.x * other.y - this.y * other.x
        );
    }

    @Override
    public double length() {
        return Math.sqrt(x*x+y*y+z*z);
    }

    public double radiansFromXAxis(){
        return Math.atan2(x,y);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "Vector3d{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }


}
