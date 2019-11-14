package core.solver.decomposition;

import core.matrix.Matrix;
import core.matrix.dense.DenseMatrix;
import core.vector.DenseVector;

public class CholeskyDecomposition<T extends Matrix<T>> {

    private T L;
    private DenseVector D;


    public static CholeskyDecomposition decomposeLDLT(DenseMatrix mat){
//        if(matrix.getM() != matrix.getN()) throw new RuntimeException("matrix must be symmetric!");
//        for(int i = 0; i < matrix.getM(); i++){
//
//        }
//        Matrix L = new DenseMatrix(matrix.getM())
        return null;
    }

    public static <T extends Matrix<T>> CholeskyDecomposition<T> decomposeGGT(T mat){
        T matrix = mat.copy();
        for(int i = 0; i < matrix.getM(); i++){
            for(int j = i+1; j < mat.getN(); j++){
                matrix.setValue(i,j,0);
            }
            for(int j = 0; j <= i; j++){
                double sum = matrix.getValue(i,j);
                for(int k = 0; k <= j-1; k++){
                    sum -= matrix.getValue(i,k) * matrix.getValue(j,k);
                }
                if(i > j){
                    matrix.setValue(i,j,sum/(matrix.getValue(j,j)));
                }else if (sum > 0) {
                    matrix.setValue(i,i, Math.sqrt(sum));
                }else{
                    throw new RuntimeException("Matrix is not symmetric positive definite!");
                }
            }
        }
        return new CholeskyDecomposition<T>(matrix);
    }

    CholeskyDecomposition(T g) {
        L = g;
        D = null;
    }

    CholeskyDecomposition(T l, DenseVector d) {
        L = l;
        D = d;
    }

    public void setL(T l) {
        L = l;
    }

    public void setD(DenseVector d) {
        D = d;
    }

    public T getL() {
        return L;
    }

    public DenseVector getD() {
        return D;
    }

}
