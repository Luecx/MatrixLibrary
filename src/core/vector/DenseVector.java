package core.vector;


import core.exceptions.NotEnoughSlotsException;
import core.exceptions.NotMatchingSlotsException;
import core.matrix.dense.DenseMatrix;

import java.util.Arrays;

public class DenseVector extends Vector<DenseVector> {

    private double[] values;


    public DenseVector(int size) {
        super(size);
    }

    public DenseVector(double... values) {
        super(values.length);
        this.values = values;
    }

    public DenseVector(Vector<?> other) {
        super(other);
    }

    public DenseVector(Vector<?> other, int size) {
        super(other, size);
    }

    @Override
    public void genArrays() {
        values = new double[this.getSize()];
    }

    @Override
    public void setValue(int index, double val) {
        values[index] = val;
    }

    @Override
    public double getValue(int index) {
        return values[index];
    }

    @Override
    protected void scale_partial(DenseVector target, double scalar, int start, int end) {
        for (int i = start; i < end; i++) {
            target.setValue(i, getValue(i) * scalar);
        }
    }

    @Override
    protected void negate_partial(DenseVector target, int start, int end) {
        for (int i = start; i < end; i++) {
            target.setValue(i, -getValue(i));
        }
    }

    @Override
    protected void add_partial(DenseVector target, DenseVector other, int start, int end) {
        for (int i = start; i < end; i++) {
            target.setValue(i, other.getValue(i) + getValue(i));
        }
    }

    @Override
    protected void sub_partial(DenseVector target, DenseVector other, int start, int end) {
        for (int i = start; i < end; i++) {
            target.setValue(i, getValue(i) - other.getValue(i));
        }
    }

    @Override
    protected double dot_partial(DenseVector other, int start, int end) {
        double k = 0;
        for (int i = start; i < end; i++) {
            k += getValue(i) * other.getValue(i);
        }
        return k;
    }

    @Override
    protected void outerProduct_partial(DenseMatrix target, DenseVector other, int row) {
        if(this.getValue(row) != 0){
            for(int n = 0; n < other.getSize(); n++){
                target.setValue(row,n,this.getValue(row) * other.getValue(n));
            }
        }
    }

    @Override
    protected void hadamard_partial(DenseVector target, DenseVector other, int start, int end) {
        for (int i = start; i < end; i++) {
            target.setValue(i, getValue(i) * other.getValue(i));
        }
    }

    @Override
    public DenseVector copy() {
        return new DenseVector(Arrays.copyOf(this.values, this.values.length));
    }

    @Override
    public DenseVector newInstance() {
        return new DenseVector(this.values.length);
    }

    @Override
    public String toString() {
        String s = "[";
        for (double v : values) {
            s += " " + String.format("%.3E", v);
        }
        return s + "]";
    }

    public double[] getValues() {
        return values;
    }
}
