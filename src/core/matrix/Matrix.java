package core.matrix;

import core.matrix.dense.DenseMatrix;
import core.vector.Vector;
import core.vector.DenseVector;


public abstract class Matrix<T extends Matrix<T>> {

    protected int M,N;


    public Matrix(int m, int n) {
        M = m;
        N = n;
    }
    public Matrix(double[][] ar){
        this.M = ar.length;
        this.N = ar[0].length;
    }
    public Matrix(Matrix<?> o){
        this.M = o.getM();
        this.N = o.getN();
    }

    public int getM() {
        return M;
    }
    public int getN() {
        return N;
    }


    public abstract int storageSize();

    public abstract DenseVector mul(Vector<?> vec);
    public abstract DenseMatrix mul(Matrix<?> matrix);
    public abstract T add(T matrix);
    public abstract T sub(T matrix);
    public abstract T transpose();
    public abstract T self_transpose();
    public abstract T self_identity();

    public abstract double norm_1();
    public abstract double norm_infinity();
    public abstract boolean isSymmetric();
    public abstract double determinant();

    public abstract void swapRow(int r1, int row2);
    public abstract void sawpColumn(int c1, int c2);
    public abstract void scale(double scalar);
    public abstract void scale_column(int column, double scalar);
    public abstract void scale_row(int column, double scalar);
    public abstract void setValue(int m, int n, double value);
    public abstract double getValue(int m, int n);
    public abstract boolean hasValue(double v);
    public abstract void replaceValue(double v, double r);
    public abstract T copy();
    public abstract DenseMatrix copyToDense();
    public abstract String toString();

}
