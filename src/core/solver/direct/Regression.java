package core.solver.direct;

import core.matrix.Matrix;
import core.matrix.dense.DenseMatrix;
import core.solver.decomposition.QRDecomposition;
import core.vector.DenseVector;
import core.vector.Vector;
import core.vector.Vector2d;

import static core.solver.direct.Solver.solveUpperTriangularMatrix;

public class Regression {


    /**
     * minimizes the mean squared error for y = ax^b
     * returns [a,b]
     * @param x_i
     * @param y_i
     * @return
     */
    public static Vector2d power_regression(double[] x_i, double[] y_i){
        if(x_i.length != y_i.length){
            throw new RuntimeException();
        }
        DenseMatrix matrix = new DenseMatrix(x_i.length, 2);
        DenseVector vector = new DenseVector(x_i.length);

        for(int i = 0; i < x_i.length; i++) {
            vector.setValue(i, Math.log(y_i[i]));

            matrix.setValue(i,0,1);
            matrix.setValue(i,1,Math.log(x_i[i]));
        }

        DenseVector vec = linear_regression(matrix, vector);
        vec.setValue(0,Math.exp(vec.getValue(0)));
        return new Vector2d(vec);
    }


    /**
     * minimizes the mean squared error for y = ax + b
     * returns [a,b]
     * @param x_i
     * @param y_i
     * @return
     */
    public static Vector2d linear_regression(double[] x_i, double[] y_i){
        if(x_i.length != y_i.length){
            throw new RuntimeException();
        }

        DenseMatrix matrix = new DenseMatrix(x_i.length, 2);
        DenseVector vector = new DenseVector(x_i.length);
        for(int i = 0; i < x_i.length; i++) {
            vector.setValue(i, y_i[i]);

            matrix.setValue(i,0,x_i[i]);
            matrix.setValue(i,1,1);
        }


        DenseVector vec = linear_regression(matrix, vector);
        return new Vector2d(vec);
    }

    /**
     * solves the system Ax = b with minimum error in 2-norm
     * @return
     */
    public static DenseVector linear_regression(Matrix A, Vector b){
        QRDecomposition decomposition = QRDecomposition.givens(A);
        DenseVector r = decomposition.getQ().transpose().mul(b);

        DenseVector solution = solveUpperTriangularMatrix(decomposition.getR(),r);

        return solution;
    }


    public static void main(String[] args) {

        double[] x = new double[]{1,2};
        double[] y = new double[]{3,2};

        System.out.println(linear_regression(x,y));
        System.out.println(power_regression(x,y));
    }
}
