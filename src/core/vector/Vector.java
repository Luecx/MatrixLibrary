package core.vector;

import core.matrix.dense.DenseMatrix;
import core.solver.Utilities;
import core.threads.Pool;
import core.threads.PoolFunction;

public abstract class Vector<T extends Vector<T>> {

    private int size;

    public Vector(int size) {
        this.size = size;
        genArrays();
    }

    public Vector(Vector<?> other) {
        this.size = other.size;
        genArrays();
        other.loadTo(this);
    }

    public Vector(Vector<?> other, int size) {
        this.size = size;
        genArrays();
        other.loadTo(this);
    }

    public void loadTo(Vector<?> other) {
        for (int i = 0; i < Math.min(this.getSize(), other.getSize()); i++) {
            other.setValue(i, this.getValue(i));
        }
    }

    public int getSize() {
        return size;
    }

    public void randomise(double lower, double upper) {
        for (int i = 0; i < this.getSize(); i++) {
            this.setValue(i, Math.random() * (upper - lower) + lower);
        }
    }

    public T set_self_Length(double l) {
        this.self_normalise();
        this.self_scale(l);
        return (T) this;
    }

    public T self_normalise() {
        this.self_scale(1 / this.length());
        return (T) this;
    }

    public boolean equals(Vector other) {
        if (other.size != this.size) return false;
        for (int i = 0; i < this.size; i++) {
            if (other.getValue(i) != this.getValue(i)) return false;
        }
        return true;
    }


    public static <T extends Vector<T>> double radiansBetween(T a, T b) {
        return Math.acos(a.dot(b) / (a.length() * b.length()));
    }

    public static <T extends Vector<T>> double radiansBetween(T a, T b, T c) {
        return radiansBetween(a.sub(c), b.sub(c));
    }

    public static <T extends Vector<T>> double distanceBetween(T a, T b) {
        return a.sub(b).length();
    }


    public abstract void genArrays();

    public abstract void setValue(int index, double val);

    public abstract double getValue(int index);

    public double length(Pool pool) {
        return Math.sqrt(this.dot((T) this, pool));
    }

    public DenseMatrix outerProduct(T other, Pool pool) {
        DenseMatrix target = new DenseMatrix(this.size, other.getSize());
        PoolFunction function = (index, core) -> {
            outerProduct_partial(target, other, index);
        };
        pool.executeTotal(function, this.size, false);
        return target;
    }

    public T add(T other, Pool pool) {
        if (this.getSize() != other.getSize()) throw new RuntimeException();
        T result = newInstance();
        PoolFunction function = (index, core) -> {
            add_partial(result, other, (int) (index * this.getSize() / (double)pool.getActiveThreads()),
                    (int) ((index+1) * this.getSize() / (double)pool.getActiveThreads()));
        };
        pool.executeTotal(function, pool.getActiveThreads(), false);
        return result;
    }

    public T sub(T other, Pool pool) {
        if (this.getSize() != other.getSize()) throw new RuntimeException();
        T result = newInstance();
        PoolFunction function = (index, core) -> {
            sub_partial(result, other, (int) (index * this.getSize() / (double)pool.getActiveThreads()),
                    (int) ((index+1) * this.getSize() / (double)pool.getActiveThreads()));
        };
        pool.executeTotal(function, pool.getActiveThreads(), false);
        return result;
    }

    public T scale(double scalar, Pool pool) {
        T result = newInstance();
        PoolFunction function = (index, core) -> {
            scale_partial(result, scalar, (int) (index * this.getSize() / (double)pool.getActiveThreads()),
                    (int) ((index+1) * this.getSize() / (double)pool.getActiveThreads()));
        };
        pool.executeTotal(function, pool.getActiveThreads(), false);
        return result;
    }

    public T negate(Pool pool) {
        T result = newInstance();
        PoolFunction function = (index, core) -> {
            negate_partial(result, (int) (index * this.getSize() / (double)pool.getActiveThreads()),
                    (int) ((index+1) * this.getSize() / (double)pool.getActiveThreads()));
        };
        pool.executeTotal(function, pool.getActiveThreads(), false);
        return result;
    }

    public T hadamard(T other, Pool pool) {
        if (this.getSize() != other.getSize()) throw new RuntimeException();
        T result = newInstance();
        PoolFunction function = (index, core) -> {
            hadamard_partial(result, other, (int) (index * this.getSize() / (double)pool.getActiveThreads()),
                    (int) ((index+1) * this.getSize() / (double)pool.getActiveThreads()));
        };
        pool.executeTotal(function, pool.getActiveThreads(), false);
        return result;
    }

    public T self_add(T other, Pool pool) {
        if (this.getSize() != other.getSize()) throw new RuntimeException();
        PoolFunction function = (index, core) -> {
            add_partial((T) this, other, (int) (index * this.getSize() / (double)pool.getActiveThreads()),
                    (int) ((index+1) * this.getSize() / (double)pool.getActiveThreads()));
        };
        pool.executeTotal(function, pool.getActiveThreads(), false);
        return (T) this;
    }

    public T self_sub(T other, Pool pool) {
        if (this.getSize() != other.getSize()) throw new RuntimeException();
        PoolFunction function = (index, core) -> {
            sub_partial((T) this, other, (int) (index * this.getSize() / (double)pool.getActiveThreads()),
                    (int) ((index+1) * this.getSize() / (double)pool.getActiveThreads()));
        };
        pool.executeTotal(function, pool.getActiveThreads(), false);
        return (T) this;
    }

    public T self_scale(double scalar, Pool pool) {
        PoolFunction function = (index, core) -> {
            scale_partial((T) this, scalar,(int) (index * this.getSize() / (double)pool.getActiveThreads()),
                    (int) ((index+1) * this.getSize() / (double)pool.getActiveThreads()));
        };
        pool.executeTotal(function, pool.getActiveThreads(), false);
        return (T) this;
    }

    public T self_negate(Pool pool) {
        PoolFunction function = (index, core) -> {
            negate_partial((T) this, (int) (index * this.getSize() / (double)pool.getActiveThreads()),
                    (int) ((index+1) * this.getSize() / (double)pool.getActiveThreads()));
        };
        pool.executeTotal(function, pool.getActiveThreads(), false);
        return (T) this;
    }

    public T self_hadamard(T other, Pool pool) {
        if (this.getSize() != other.getSize()) throw new RuntimeException();
        PoolFunction function = (index, core) -> {
            hadamard_partial((T) this, other, (int) (index * this.getSize() / (double)pool.getActiveThreads()),
                    (int) ((index+1) * this.getSize() / (double)pool.getActiveThreads()));
        };
        pool.executeTotal(function, pool.getActiveThreads(), false);
        return (T) this;
    }

    public double dot(T other, Pool pool) {
        if (this.getSize() != other.getSize()) throw new RuntimeException();
        final double[] result = new double[pool.getActiveThreads()];
        PoolFunction function = (index, core) -> {
            result[index] = dot_partial(other,
                    (int) (index * this.getSize() / (double)pool.getActiveThreads()),
                    (int) ((index+1) * this.getSize() / (double)pool.getActiveThreads()));
        };

        pool.executeTotal(function, pool.getActiveThreads(), false);
        double s = 0;
        for(double k:result){
            s+=k;
        }
        return s;
    }

    public double length() {
        return Math.sqrt(this.dot((T) this));
    }

    public DenseMatrix outerProduct(T other) {
        DenseMatrix target = new DenseMatrix(this.size, other.getSize());
        for (int i = 0; i < this.size; i++) {
            outerProduct_partial(target, other, i);
        }
        return target;
    }

    public T add(T other) {
        if (this.getSize() != other.getSize()) throw new RuntimeException();
        T result = newInstance();
        this.add_partial(result, other, 0, this.getSize());
        return result;
    }

    public T sub(T other) {
        if (this.getSize() != other.getSize()) throw new RuntimeException();
        T result = newInstance();
        this.sub_partial(result, other, 0, this.getSize());
        return result;
    }

    public T scale(double scalar) {
        T result = newInstance();
        this.scale_partial(result, scalar, 0, this.getSize());
        return result;
    }

    public T negate() {
        T result = newInstance();
        this.negate_partial(result, 0, this.getSize());
        return result;
    }

    public T hadamard(T other) {
        if (this.getSize() != other.getSize()) throw new RuntimeException();
        T result = newInstance();
        this.hadamard_partial(result, other, 0, this.getSize());
        return result;
    }

    public T self_add(T other) {
        if (this.getSize() != other.getSize()) throw new RuntimeException();
        this.add_partial((T) this, other, 0, this.getSize());
        return (T) this;
    }

    public T self_sub(T other) {
        if (this.getSize() != other.getSize()) throw new RuntimeException();
        this.sub_partial((T) this, other, 0, this.getSize());
        return (T) this;
    }

    public T self_scale(double scalar) {
        this.scale_partial((T) this, scalar, 0, this.getSize());
        return (T) this;
    }

    public T self_negate() {
        this.negate_partial((T) this, 0, this.getSize());
        return (T) this;
    }

    public T self_hadamard(T other) {
        if (this.getSize() != other.getSize()) throw new RuntimeException();
        this.hadamard_partial((T) this, other, 0, this.getSize());
        return (T) this;
    }

    public double dot(T other) {
        if (this.getSize() != other.getSize()) throw new RuntimeException();
        return dot_partial(other, 0, this.getSize());
    }

    public abstract void scale_partial(T target, double scalar, int start, int end);

    public abstract void negate_partial(T target, int start, int end);

    public abstract void add_partial(T target, T other, int start, int end);

    public abstract void sub_partial(T target, T other, int start, int end);

    public abstract double dot_partial(T other, int start, int end);

    public abstract void outerProduct_partial(DenseMatrix target, T other, int row);

    public abstract void hadamard_partial(T target, T other, int start, int end);


    public abstract T copy();

    public abstract T newInstance();

    public static void main(String[] args) {

        DenseVector vec1 = new DenseVector(10000000);
        DenseVector vec2 = new DenseVector(10000000);
        vec1.randomise(0,1);
        vec2.randomise(0,1);


        Utilities.measureCores(4, (pool, integer) -> {
            if(integer == 0){
                vec1.hadamard(vec2);
            }else{
                pool.setActiveThreads(integer);
                vec1.hadamard(vec2, pool);
            }
        },10);

//        DenseVector target = new DenseVector(1000000);
//
//        long time = System.currentTimeMillis();
//        for(int i = 0; i < 1000; i++){
//            vec1.hadamard_partial(target,vec2, 0, 1000000);
//        }
//        System.out.println(System.currentTimeMillis() - time);
//        time = System.currentTimeMillis();
//        for(int i = 0; i < 1000; i++){
//            vec1.hadamard_partial(target,vec2, 0, 500000);
//        }
//        System.out.println(System.currentTimeMillis() - time);

    }
}
