package core.solver.decomposition;

import core.matrix.Matrix;
import core.vector.Vector;

public class LinearEquationSystem<T extends Matrix<T>, V extends Vector<V>> {

    private T matrix;
    private V vector;

    public LinearEquationSystem(T matrix, V vector) {
        this.matrix = matrix;
        this.vector = vector;
    }

    public T getMatrix() {
        return matrix;
    }

    public void setMatrix(T matrix) {
        this.matrix = matrix;
    }

    public V getVector() {
        return vector;
    }

    public void setVector(V vector) {
        this.vector = vector;
    }
}
