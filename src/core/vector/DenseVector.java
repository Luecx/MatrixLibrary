package core.vector;


import core.exceptions.NotEnoughSlotsException;
import core.exceptions.NotMatchingSlotsException;
import core.matrix.dense.DenseMatrix;

public class DenseVector extends Vector<DenseVector> {

    private double[] values;


    public DenseVector(int size) {
        super(size);
    }

    public DenseVector(double... values){
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
    public DenseVector self_negate() {
        for(int i = 0; i < this.getSize(); i++){
            values[i] = -values[i];
        }
        return this;
    }

    @Override
    public DenseVector self_add(DenseVector other) {
        if(other.getSize() != this.getSize()) throw new NotMatchingSlotsException(this.getSize(), other.getSize());
        for(int i = 0; i < this.getSize(); i++){
            this.setValue(i, this.getValue(i) + other.getValue(i));
        }
        return this;
    }

    @Override
    public DenseVector self_sub(DenseVector other) {
        if(other.getSize() != this.getSize()) throw new NotMatchingSlotsException(this.getSize(), other.getSize());
        for(int i = 0; i < this.getSize(); i++){
            this.setValue(i, this.getValue(i) - other.getValue(i));
        }
        return this;
    }

    @Override
    public DenseVector self_scale(double factor) {
        for(int i = 0; i < this.getSize(); i++){
            values[i] *= factor;
        }
        return this;
    }

    @Override
    public DenseVector negate() {
        DenseVector result = new DenseVector(this.getSize());
        for(int i = 0; i < this.getSize(); i++){
            result.setValue(i, -this.getValue(i));
        }
        return result;
    }

    @Override
    public DenseVector sub(DenseVector other) {
        if(other.getSize() != this.getSize()) throw new NotMatchingSlotsException(this.getSize(), other.getSize());
        DenseVector result = new DenseVector(this.getSize());
        for(int i = 0; i < this.getSize(); i++){
            result.setValue(i, this.getValue(i) - other.getValue(i));
        }
        return result;
    }

    @Override
    public DenseVector add(DenseVector other) {
        if(other.getSize() != this.getSize()) throw new NotMatchingSlotsException(this.getSize(), other.getSize());
        DenseVector result = new DenseVector(this.getSize());
        for(int i = 0; i < this.getSize(); i++){
            result.setValue(i, this.getValue(i) + other.getValue(i));
        }
        return result;
    }

    @Override
    public DenseVector scale(double factor) {
        DenseVector result = new DenseVector(this.getSize());
        for(int i = 0; i < this.getSize(); i++){
            result.setValue(i, values[i] * factor);
        }
        return result;
    }

    @Override
    public double getValue(int index) {
        return values[index];
    }

    @Override
    public double innerProduct(DenseVector other) {
        if(other.getSize() != this.getSize()) throw new NotMatchingSlotsException(this.getSize(), other.getSize());
        double sum = 0;
        for(int i = 0; i < this.getSize(); i++){
            sum += other.getValue(i) * values[i];
        }
        return sum;
    }

    @Override
    public double length() {
        double g = 0;
        for(double v:values){
            g += v * v;
        }
        return Math.sqrt(g);
    }

    @Override
    public String toString() {
        String s = "[";
        for(double v:values){
            s += " " + String.format("%.3E",v);
        }
        return s+"]";
    }

    @Override
    public DenseMatrix outerProduct(DenseVector other) {
        if(other.getSize() != this.getSize()) throw new NotMatchingSlotsException(this.getSize(), other.getSize());
        DenseMatrix matrix = new DenseMatrix(this.getSize(), this.getSize());
        for(int i = 0; i < this.getSize(); i++){
            for(int n = 0; n < this.getSize(); n++){
                matrix.setValue(i,n, this.getValue(i) * other.getValue(n));
            }
        }

        return matrix;
    }

    public double[] getValues() {
        return values;
    }
}
