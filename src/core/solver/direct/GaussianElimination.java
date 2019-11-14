package core.solver.direct;


import core.matrix.dense.DenseMatrix;
import core.vector.DenseVector;

public class GaussianElimination {
    public static double[] solve(double[][] A, double[] B) {
        int N = B.length;
        for (int k = 0; k < N; k++) {
            /** find pivot row **/

            int max = k;
            for (int i = k + 1; i < N; i++)
                if (Math.abs(A[i][k]) > Math.abs(A[max][k]))
                    max = i;
            /** swap row in A matrix **/
            double[] temp = A[k];
            A[k] = A[max];
            A[max] = temp;
            /** swap corresponding values in constants matrix **/
            double t = B[k];
            B[k] = B[max];
            B[max] = t;
            /** pivot within A and B **/
            for (int i = k + 1; i < N; i++) {
                double factor = A[i][k] / A[k][k];
                B[i] -= factor * B[k];
                for (int j = k; j < N; j++)
                    A[i][j] -= factor * A[k][j];
            }
        }

        /** back substitution **/
        double[] solution = new double[N];
        for (int i = N - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < N; j++)
                sum += A[i][j] * solution[j];
            solution[i] = (B[i] - sum) / A[i][i];
        }
        return solution;
    }


    public static DenseVector lsolve(DenseMatrix A, DenseVector b) {
        return new DenseVector(solve(A.getValues(), b.getValues()));
    }

}