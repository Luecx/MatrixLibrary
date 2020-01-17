package core.solver;

import core.matrix.Matrix;
import core.vector.DenseVector;
import core.vector.Vector;

import java.util.function.Function;

public enum Preconditioner {


    None(null,null),
    Jacobian(null, matrix -> {
        DenseVector result = new DenseVector(matrix.getM());
        for (int i = 0; i < matrix.getN(); i++) {
            result.setValue(i, 1 / matrix.getValue(i, i));
        }
        return new DenseVector(result);
    }),
    ;


    private Function<Matrix, Matrix> matrixFunction;
    private Function<Matrix, Vector> vectorFunction;


    Preconditioner(Function<Matrix, Matrix> matrixFunction, Function<Matrix, Vector> vectorFunction) {
        this.matrixFunction = matrixFunction;
        this.vectorFunction = vectorFunction;
    }

    public boolean isMatrixOutputSupported() {
        return matrixFunction != null;
    }

    public boolean isVectorOutputSupported() {
        return vectorFunction != null;
    }
}
