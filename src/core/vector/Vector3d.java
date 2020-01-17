package core.vector;

import core.matrix.dense.DenseMatrix;
import core.threads.Pool;

import java.util.Objects;

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

    public static void main(String[] args) {

        Vector2d vec1 = new Vector2d(4,3);
        System.out.println(new Vector3d(vec1));
    }

    @Override
    public void setValue(int index, double val) {
        switch (index){
            case 0: x=val; break;
            case 1: y=val; break;
            case 2: z=val; break;
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
    public Vector3d self_hadamard(Vector3d other) {
        this.x *= other.x;
        this.y *= other.y;
        this.z *= other.z;
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
    public double dot(Vector3d other) {
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
    public Vector3d hadamard(Vector3d other) {
        return new Vector3d(x *other.x, y * other.y, z * other.z);
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


    @Override
    public Vector3d copy() {
        return new Vector3d(x,y,z);
    }

    @Override
    public Vector3d newInstance() {
        return new Vector3d();
    }

    @Override
    public double length(Pool pool) {
        return length();
    }

    @Override
    public DenseMatrix outerProduct(Vector3d other, Pool pool) {
        return outerProduct(other);
    }

    @Override
    public Vector3d add(Vector3d other, Pool pool) {
        return add(other);
    }

    @Override
    public Vector3d sub(Vector3d other, Pool pool) {
        return sub(other);
    }

    @Override
    public Vector3d scale(double scalar, Pool pool) {
        return scale(scalar);
    }

    @Override
    public Vector3d negate(Pool pool) {
        return negate();
    }

    @Override
    public Vector3d hadamard(Vector3d other, Pool pool) {
        return hadamard(other);
    }

    @Override
    public Vector3d self_add(Vector3d other, Pool pool) {
        return self_add(other);
    }

    @Override
    public Vector3d self_sub(Vector3d other, Pool pool) {
        return self_sub(other);
    }

    @Override
    public Vector3d self_scale(double scalar, Pool pool) {
        return self_scale(scalar);
    }

    @Override
    public Vector3d self_negate(Pool pool) {
        return self_negate();
    }

    @Override
    public Vector3d self_hadamard(Vector3d other, Pool pool) {
        return this.self_hadamard(other);
    }

    @Override
    public double dot(Vector3d other, Pool pool) {
        return this.dot(other);
    }

    @Override
    public void scale_partial(Vector3d target, double scalar, int start, int end) {
        throw new RuntimeException();
    }

    @Override
    public void negate_partial(Vector3d target, int start, int end) {
        throw new RuntimeException();
    }

    @Override
    public void add_partial(Vector3d target, Vector3d other, int start, int end) {
        throw new RuntimeException();
    }

    @Override
    public void sub_partial(Vector3d target, Vector3d other, int start, int end) {
        throw new RuntimeException();
    }

    @Override
    public double dot_partial(Vector3d other, int start, int end) {
        throw new RuntimeException();
    }

    @Override
    public void outerProduct_partial(DenseMatrix target, Vector3d other, int row) {
        throw new RuntimeException();
    }

    @Override
    public void hadamard_partial(Vector3d target, Vector3d other, int start, int end) {
        throw new RuntimeException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector3d vector3d = (Vector3d) o;
        return Double.compare(vector3d.x, x) == 0 &&
                Double.compare(vector3d.y, y) == 0 &&
                Double.compare(vector3d.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
