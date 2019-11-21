package core.matrix;

import core.exceptions.NotMatchingSlotsException;
import core.exceptions.NotSupportedOperation;
import core.matrix.dense.DenseMatrix;
import core.threads.Pool;
import core.threads.PoolThreadRange;
import core.threads.PoolFunction;
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


    public DenseVector mul(Vector<?> vec, Pool pool){
        if(vec.getSize() != this.getN()) throw new RuntimeException();

        DenseVector target = new DenseVector(this.getM());
        PoolFunction function = index -> mul_partial_row(target, vec, index);
        pool.execute(function, this.getM());

        return target;
    }
    public DenseMatrix mul(Matrix<?> mat, Pool pool){
        if(mat.getM() != this.getN()) throw new RuntimeException();

        DenseMatrix target = new DenseMatrix(this.getM(), mat.getN());
        PoolFunction function = index -> {
            mul_partial_row(target, mat, index);
        };
        pool.execute(function, this.getM());
        return target;
    }
    public T scale(double scalar, Pool pool){
        T target = this.newInstance();
        PoolFunction function = index -> {
            scale_partial_row(target, scalar,index);
        };
        pool.execute(function, this.getM());
        return target;
    }
    public T self_scale(double scalar, Pool pool){
        PoolFunction function = index -> {
            scale_partial_row((T)this, scalar,index);
        };
        pool.execute(function, this.getM());
        return (T) this;
    }
    public T add(T other, Pool pool){
        if(this.getM() != other.getM() || this.getN() != other.getN()) throw new RuntimeException();
        T target = this.newInstance();
        PoolFunction function = index -> {
            add_partial_row(target, other, index);
        };
        pool.execute(function, this.getM());
        return target;
    }
    public T self_add(T other, Pool pool){
        if(this.getM() != other.getM() || this.getN() != other.getN()) throw new RuntimeException();
        PoolFunction function = index -> {
            add_partial_row((T)this, other, index);
        };
        pool.execute(function, this.getM());
        return (T)this;
    }
    public T sub(T other, Pool pool){
        if(this.getM() != other.getM() || this.getN() != other.getN()) throw new RuntimeException();
        T target = this.newInstance();
        PoolFunction function = index -> {
            sub_partial_row(target, other, index);
        };
        pool.execute(function, this.getM());
        return target;
    }
    public T self_sub(T other, Pool pool){
        if(this.getM() != other.getM() || this.getN() != other.getN()) throw new RuntimeException();
        PoolFunction function = index -> {
            sub_partial_row((T)this, other, index);
        };
        pool.execute(function, this.getM());
        return (T)this;
    }

    public DenseVector mul(Vector<?> vec){
        DenseVector target = new DenseVector(this.getM());
        for(int i = 0; i < this.getM(); i++){
            this.mul_partial_row(target, vec, i);
        }
        return target;
    }
    public DenseMatrix mul(Matrix<?> mat){
        if(mat.getM() != this.getN()) throw new RuntimeException();

        DenseMatrix target = new DenseMatrix(this.getM(), mat.getN());
        for(int i = 0; i < this.getM(); i++){
            this.mul_partial_row(target, mat, i);
        }
        return target;
    }
    public T scale(double scalar){
        T target = this.newInstance();
        for(int i = 0; i < this.getM(); i++){
            this.scale_partial_row(target, scalar,i);
        }
        return target;
    }
    public T self_scale(double scalar){
        for(int i = 0; i < this.getM(); i++){
            this.scale_partial_row((T)this, scalar,i);
        }
        return (T)this;
    }
    public T add(T other){
        if(this.getM() != other.getM() || this.getN() != other.getN()) throw new RuntimeException();
        T target = this.newInstance();
        for(int i = 0; i < this.getM(); i++){
            this.add_partial_row(target, other, i);
        }
        return target;
    }
    public T self_add(T other){
        if(this.getM() != other.getM() || this.getN() != other.getN()) throw new RuntimeException();
        for(int i = 0; i < this.getM(); i++){
            this.add_partial_row((T) this, other, i);
        }
        return (T)this;
    }
    public T sub(T other){
        if(this.getM() != other.getM() || this.getN() != other.getN()) throw new RuntimeException();
        T target = this.newInstance();
        for(int i = 0; i < this.getM(); i++){
            this.sub_partial_row(target, other, i);
        }
        return target;
    }
    public T self_sub(T other){
        if(this.getM() != other.getM() || this.getN() != other.getN()) throw new RuntimeException();
        for(int i = 0; i < this.getM(); i++){
            this.sub_partial_row((T)this, other, i);
        }
        return (T)this;
    }

    protected abstract void mul_partial_row(DenseVector target, Vector<?> vec, int row);
    protected abstract void mul_partial_row(DenseMatrix target, Matrix<?> matrix, int row);
    protected abstract void scale_partial_row(T target, double scalar, int row);
    protected abstract void add_partial_row(T target, T matrix, int row);
    protected abstract void sub_partial_row(T target, T matrix, int row);

    public abstract T transpose();
    public abstract T self_transpose();
    public abstract T self_identity();

    public abstract double norm_1();
    public abstract double norm_infinity();
    public abstract boolean isSymmetric();
    public abstract double determinant();

    public abstract void swapRow(int r1, int row2);
    public abstract void swapColumn(int c1, int c2);
    public abstract void scale_column(int column, double scalar);
    public abstract void scale_row(int column, double scalar);
    public abstract void setValue(int m, int n, double value);
    public abstract double getValue(int m, int n);
    public abstract boolean hasValue(double v);
    public abstract void replaceValue(double v, double r);

    public abstract T newInstance();
    public abstract T copy();
    public abstract DenseMatrix copyToDense();
    public abstract String toString();

}
