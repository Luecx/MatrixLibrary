package core.vector;

import core.matrix.Matrix;
import core.matrix.dense.DenseMatrix;

public abstract class Vector<T extends Vector<T>> {

    private int size;

    public Vector(int size) {
        this.size = size;
        genArrays();
    }

    public Vector(Vector<?> other){
        this.size = other.size;
        genArrays();
        other.loadTo(this);
    }

    public Vector(Vector<?> other, int size){
        this.size = other.size;
        genArrays();
        other.loadTo(this);
    }

    public void loadTo(Vector<?> other) {
        for(int i = 0; i < Math.min(this.getSize(), other.getSize()); i++){
            other.setValue(i, this.getValue(i));
        }
    }

    public int getSize() {
        return size;
    }

    public void randomise(double lower, double upper) {
        for(int i = 0; i < this.getSize(); i++){
            this.setValue(i, Math.random() * (upper-lower) + lower);
        }
    }

    public T set_self_Length(double l){
        this.self_normalise();
        this.self_scale(l);
        return (T)this;
    }

    public T self_normalise(){
        this.self_scale(1 / this.length());
        return (T)this;
    }

    public boolean equals(Vector other){
        if(other.size != this.size) return false;
        for(int i = 0; i < this.size; i++){
            if(other.getValue(i) != this.getValue(i)) return false;
        }
        return true;
    }

    public static <T extends Vector<T>> double radiansBetween(T a, T b){
        return Math.acos(a.innerProduct(b) / (a.length() * b.length()));

    }

    public static <T extends Vector<T>> double radiansBetween(T a, T b, T c){
        return radiansBetween(a.sub(c), b.sub(c));
    }

    public static <T extends Vector<T>> double distanceBetween(T a, T b){
        return a.sub(b).length();
    }



    public abstract void genArrays();
    public abstract void setValue(int index, double val);
    public abstract double getValue(int index);

    public abstract T self_negate();
    public abstract T self_add(T other);
    public abstract T self_sub(T other);
    public abstract T self_scale(double factor);


    public abstract T scale(double factor);
    public abstract T negate();
    public abstract T add(T other);
    public abstract T sub(T other);
    public abstract double innerProduct(T other);
    public abstract DenseMatrix outerProduct(T other);
    public abstract double length();


    public static void main(String[] args) {
        DenseVector vec1 = new DenseVector(3);
        DenseVector vec2 = new DenseVector(3);

        vec1.setValue(1,-2);
        vec2.setValue(0,3);

    }
}
