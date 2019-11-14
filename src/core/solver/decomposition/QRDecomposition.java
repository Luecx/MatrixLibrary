package core.solver.decomposition;

import core.matrix.Matrix;
import core.matrix.dense.DenseMatrix;
import core.solver.direct.Solver;
import core.vector.DenseVector;

public class QRDecomposition<T extends Matrix<T>> {

    private T Q;
    private T R;


    QRDecomposition(T Q, T R) {
        this.Q = Q;
        this.R = R;
    }


    public static <T extends Matrix<T>> QRDecomposition<DenseMatrix> givens(T matrix){

        DenseMatrix Q = new DenseMatrix(matrix.getM(), matrix.getM()).self_identity();
        DenseMatrix R = matrix.copyToDense();

        double v1,v2;
        for(int i = 1; i < matrix.getM(); i++){
            for(int n = 0; n < Math.min(i, matrix.getN()); n++){

                if(R.getValue(i,n) != 0){
                    v1 = R.getValue(n,n);
                    v2 = R.getValue(i,n);

                    double r = Math.sqrt(v1 * v1 +v2 * v2);
                    double c = v1 / r;
                    double s = -v2 / r;

                    R.setValue(n,n,r);
                    R.setValue(i,n,0);

                    for(int k = n+1; k < matrix.getN(); k++){
                        v1 = R.getValue(n,k);
                        v2 = R.getValue(i,k);
                        R.setValue(n,k, v1 * c - s * v2);
                        R.setValue(i,k, v1 * s + c * v2);
                    }

                    for(int k = 0; k < matrix.getM(); k++){
                        v1 = Q.getValue(k,n);
                        v2 = Q.getValue(k,i);
                        Q.setValue(k,n, c * v1 - s * v2);
                        Q.setValue(k,i, s * v1 + c * v2);
                    }
                }
            }
        }


        return new QRDecomposition<DenseMatrix>(Q,R);
    }

    public static void main(String[] args) {
        DenseMatrix matrix = new DenseMatrix(3,3);
        matrix.randomise(0,1);
        matrix.setValue(2,1,0);
        matrix.setValue(1,0,0);
        //matrix.setValue(2,0,0);


        System.out.println(matrix);
        QRDecomposition decomp = QRDecomposition.givens(matrix);
        System.out.println(decomp.getQ());
        System.out.println(decomp.getR());

        Matrix m = (decomp.getQ().mul(decomp.getR()));
        System.out.println(m);
        System.out.println(m.getValue(2,1));

    }

    public T getQ() {
        return Q;
    }

    public void setQ(T q) {
        Q = q;
    }

    public T getR() {
        return R;
    }

    public void setR(T r) {
        R = r;
    }
}
