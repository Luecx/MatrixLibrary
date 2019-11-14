package core.vector;

import core.matrix.dense.DenseMatrix;

public class Vector2d extends Vector<Vector2d> {

    private double x,y;

    public Vector2d() {
        super(2);
    }

    public Vector2d(double x, double y) {
        this();
        this.x = x;
        this.y = y;
    }

    public Vector2d(Vector<?> other){
        super(other,2);
    }

    @Override
    public void genArrays() {

    }

    @Override
    public void setValue(int index, double val) {
        switch (index){
            case 0: x=val;
            case 1: y=val;
        }
    }

    @Override
    public double getValue(int index) {
        switch (index){
            case 0: return x;
            case 1: return y;
            default: return 0;
        }
    }



    @Override
    public Vector2d self_negate() {
        this.x = -x;
        this.y = -y;
        return this;
    }

    @Override
    public Vector2d self_add(Vector2d other) {
        this.x += other.x;
        this.y += other.y;
        return this;
    }

    @Override
    public Vector2d self_sub(Vector2d other) {
        this.x -= other.x;
        this.y -= other.y;
        return this;
    }

    @Override
    public Vector2d self_scale(double factor) {
        this.x *= factor;
        this.y *= factor;
        return this;
    }

    @Override
    public Vector2d scale(double factor) {
        return new Vector2d(this.x * factor, factor * this.y);
    }

    @Override
    public Vector2d negate() {
        return new Vector2d(-this.x, -this.y);
    }

    @Override
    public Vector2d add(Vector2d other) {
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    @Override
    public Vector2d sub(Vector2d other) {
        return new Vector2d(this.x - other.x, this.y - other.y);
    }

    @Override
    public double innerProduct(Vector2d other) {
        return this.x * other.x + this.y * other.y;
    }

    @Override
    public DenseMatrix outerProduct(Vector2d other) {
        return new DenseMatrix(new double[][]{{this.x * other.x, this.x * other.y},{other.x * this.y, this.y * other.y}});
    }

    @Override
    public double length() {
        return Math.sqrt(x*x+y*y);
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

    @Override
    public String toString() {
        return "Vector2d{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public Vector2d self_rotate(double radians){
        double n_x = x * Math.cos(radians) - Math.sin(radians) * y;
        double n_y = x * Math.sin(radians) + Math.cos(radians) * y;
        this.x = n_x;
        this.y = n_y;
        return this;
    }

    public Vector2d rotate(double radians){
        double n_x = x * Math.cos(radians) - Math.sin(radians) * y;
        double n_y = x * Math.sin(radians) + Math.cos(radians) * y;
        return new Vector2d(n_x, n_y);
    }
}
