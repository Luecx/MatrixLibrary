package core.solver.direct;

import core.matrix.Matrix;
import core.matrix.dense.DenseMatrix;
import core.solver.decomposition.CholeskyDecomposition;
import core.solver.decomposition.QRDecomposition;
import core.threads.Pool;
import core.vector.DenseVector;
import visuals.Printer;

import java.util.function.Function;


public class Solver {


    public static final double GAUSS_NEWTON_MAX_ERROR = 1E-14;
    public static final double CONJUGATE_GRADIENT_MAX_ERROR = 1E-14;
    public static final double EIGENVALUE_ITERARTION_MAX_ERROR = 1E-14;

    public static DenseVector gaussian() {
        //
        return null;
    }

    public static DenseVector solveUpperTriangularMatrix(Matrix<?> matrix, DenseVector vec) {
        if (matrix.getM() != vec.getSize()) throw new RuntimeException();
        DenseVector vector = new DenseVector(vec);
        DenseVector sol = new DenseVector(matrix.getN());
        for (int i = matrix.getN() - 1; i >= 0; i--) {
            sol.setValue(i, vector.getValue(i) / matrix.getValue(i, i));
            for (int n = i - 1; n >= 0; n--) {
                vector.setValue(n, vector.getValue(n) - matrix.getValue(n, i) * sol.getValue(i));
            }
        }
        return sol;
    }

    public static DenseVector solveLowerTriangularMatrix(Matrix<?> matrix, DenseVector vec) {
        if (matrix.getN() != matrix.getM() || matrix.getM() != vec.getSize()) throw new RuntimeException();
        DenseVector vector = new DenseVector(vec);
        DenseVector sol = new DenseVector(vector.getSize());
        for (int i = 0; i < matrix.getM(); i++) {
            sol.setValue(i, vector.getValue(i) / matrix.getValue(i, i));
            for (int n = i + 1; n < matrix.getM(); n++) {
                vector.setValue(n, vector.getValue(n) - matrix.getValue(n, i) * sol.getValue(i));
            }
        }
        return sol;
    }

    public static <T extends Matrix<T>> DenseVector eigenValue_QR(T matrix) {
        if (!matrix.isSymmetric()) {
            throw new RuntimeException("Matrix must be symmetric!");
        }
        DenseMatrix copy = (DenseMatrix) matrix.copy();
        DenseMatrix newCopy;
        double e = 1;
        while (e > EIGENVALUE_ITERARTION_MAX_ERROR) {
            QRDecomposition qr = QRDecomposition.givens(copy);
            newCopy = qr.getR().mul(qr.getQ());
            e = newCopy.sub(copy).norm_infinity();
            System.out.print("\rResiduum: " + e);
            copy = newCopy;
        }
        DenseVector res = new DenseVector(matrix.getM());
        for (int i = 0; i < res.getSize(); i++) {
            res.setValue(i, copy.getValue(i, i));
        }
        copy.applyFilter(new Function<Double, Double>() {
            @Override
            public Double apply(Double aDouble) {
                if (aDouble < 0.00001) {
                    return 0d;
                }
                return aDouble;
            }
        });
        System.out.println();
        System.out.println(copy);
        return res;
    }

    public static <T extends Matrix<T>> DenseVector eigenValue_powerIteration(T matrix) {
        DenseVector b = new DenseVector(matrix.getN());
        DenseMatrix copy = (DenseMatrix) matrix.copy();
        DenseMatrix newCopy;
        double e = 1;
        while (e > EIGENVALUE_ITERARTION_MAX_ERROR) {
            QRDecomposition qr = QRDecomposition.givens(copy);
            newCopy = qr.getR().mul(qr.getQ());
            e = newCopy.sub(copy).norm_infinity();
            System.out.print("\rResiduum: " + e);
            copy = newCopy;
        }
        DenseVector res = new DenseVector(matrix.getM());
        for (int i = 0; i < res.getSize(); i++) {
            res.setValue(i, copy.getValue(i, i));
        }
        copy.applyFilter(new Function<Double, Double>() {
            @Override
            public Double apply(Double aDouble) {
                if (aDouble < 0.00001) {
                    return 0d;
                }
                return aDouble;
            }
        });
        System.out.println();
        System.out.println(copy);
        return res;
    }

    public static <T extends Matrix<T>> DenseVector cholesky(T matrix, DenseVector vec) {
        System.out.print("Cholesky solver: \tdecomp...");
        T g = CholeskyDecomposition.decomposeGGT(matrix).getL();
        System.out.print("\r");
        System.out.print("Cholesky solver: \tdecomp[COMPLETED] \ttransposing...");
        T g_T = g.transpose();
        System.out.print("\r");
        System.out.print("Cholesky solver: \tdecomp[COMPLETED] \ttransposing[COMPLETED] \tsolving...");
        DenseVector sol = solveUpperTriangularMatrix(g_T, solveLowerTriangularMatrix(g, vec));
        System.out.print("\r");
        System.out.println("Cholesky solver: \tdecomp[COMPLETED] \ttransposing[COMPLETED] \tsolving[COMPLETED]");
        return sol;
    }




    public static DenseVector precon_conjugate_gradient(Matrix<?> A, DenseVector b, int cores) {
        return precon_conjugate_gradient(A, b, new DenseVector(b.getSize()), cores);
    }

    public static DenseVector precon_conjugate_gradient(Matrix<?> A, DenseVector b, DenseVector x_0, int cores) {
        Pool p = new Pool(cores);
        long startTime = System.currentTimeMillis();
        DenseVector C = preconditioner_jacobi(A);

        DenseVector old_r = new DenseVector(b.sub(A.mul(x_0,p)));
        DenseVector old_h = C.hadamard(old_r,p);
        DenseVector old_d = new DenseVector(old_h);
        DenseVector old_x = new DenseVector(x_0);
        DenseVector new_r;
        DenseVector new_h;
        DenseVector new_d;
        DenseVector new_x;

        double a, beta;
        double e = 1;
        int counter = 1;
        while (e > CONJUGATE_GRADIENT_MAX_ERROR) {
            DenseVector z = A.mul(old_d,p);
            a = old_r.dot(old_h,p) / (old_d.dot(z,p));
            new_x = old_x.add(old_d.scale(a,p),p);
            new_r = old_r.sub(z.scale(a,p),p);
            new_h = C.hadamard(new_r,p);
            beta = new_r.dot(new_h,p) / (old_r.dot(old_h,p));
            new_d = new_h.add(old_d.scale(beta,p),p);

            old_d = new_d;
            old_x = new_x;
            old_h = new_h;
            old_r = new_r;

            counter ++;
            e = new_r.length(p);
            Printer.print_conjugateGradient(e, counter, System.currentTimeMillis()-startTime, cores);

        }
        p.stop();
        System.out.println();
        return old_x;
    }

    public static DenseVector conjugate_gradient(Matrix<?> A, DenseVector b, int cores) {
        return conjugate_gradient(A, b, new DenseVector(A.getM()), cores);
    }

    public static DenseVector conjugate_gradient(Matrix<?> A, DenseVector b, DenseVector x_0, int cores) {
        long startTime = System.currentTimeMillis();
        Pool pool = new Pool(cores);

        DenseVector old_x = new DenseVector(x_0);
        DenseVector new_x;
        DenseVector old_p = b.sub(A.mul(old_x,pool),pool);
        DenseVector new_p;
        DenseVector old_r = new DenseVector(old_p);
        DenseVector new_r;

        double a, beta;
        double e = 1;
        int counter = 1;
        while (e > CONJUGATE_GRADIENT_MAX_ERROR) {
            a = old_r.dot(old_r,pool) / (old_p.dot(A.mul(old_p,pool),pool));
            new_x = old_x.add(old_p.scale(a,pool),pool);
            new_r = old_r.sub(A.mul(old_p,pool).scale(a,pool),pool);
            beta = new_r.dot(new_r,pool) / old_r.dot(old_r,pool);
            new_p = new_r.add(old_p.scale(beta,pool));

            old_p = new_p;
            old_r = new_r;
            old_x = new_x;

            e = new_r.length(pool);
            Printer.print_conjugateGradient(e, counter, System.currentTimeMillis()-startTime, cores);
            counter ++;

        }
        System.out.println();
        return old_x;
    }




    public static DenseVector gauss_newton(Function<DenseVector, DenseVector> function, Function<DenseVector, DenseMatrix> derivative, DenseVector start, int recalculation) {
        double e = 1;
        double alpha_k = 100;
        DenseVector x = start;
        Matrix der = null;
        int counter = 0;
        while (e > GAUSS_NEWTON_MAX_ERROR) {
            if (counter % recalculation == 0) {
                der = derivative.apply(x);
            }
            Matrix transpose = der.transpose();
            Matrix leftSide = transpose.mul(der);
            DenseVector rightSide = transpose.mul(function.apply(x));
            DenseVector dx = conjugate_gradient(leftSide, rightSide,1);
            x.self_sub(dx.scale(alpha_k));
            e = dx.length();
            System.err.println(x);
            counter++;
        }
        return x;
    }

    public static DenseVector gauss_newton(Function<DenseVector, DenseVector> function, Function<DenseVector, DenseMatrix> derivative, DenseVector start) {
        return gauss_newton(function, derivative, start, 1);
    }

    public static DenseVector gauss_newton(Function<DenseVector, DenseVector> function, Function<DenseVector, DenseMatrix> derivative, int dim) {
        DenseVector start = new DenseVector(dim);
        start.randomise(-1, 1);
        return gauss_newton(function, derivative, start, 1);
    }

    public static DenseVector preconditioner_jacobi(Matrix matrix) {
        DenseVector result = new DenseVector(matrix.getM());
        for (int i = 0; i < matrix.getN(); i++) {
            result.setValue(i, 1 / matrix.getValue(i, i));
        }
        return new DenseVector(result);
    }

    public static void main(String[] args) {
        double nodes[] = new double[]{1, 2, 3};
        double times[] = new double[]{1, Math.pow(2, 3d / 2), Math.pow(3, 3d / 2)};
        Function<DenseVector, DenseVector> function = new Function<DenseVector, DenseVector>() {
            @Override
            public DenseVector apply(DenseVector vector) {
                DenseVector out = new DenseVector(nodes.length);
                for (int i = 0; i < nodes.length; i++) {
                    out.setValue(i, Math.pow(nodes[i], vector.getValue(0)) - times[i]);
                }
                return out;
            }
        };
        Function<DenseVector, DenseMatrix> der = new Function<DenseVector, DenseMatrix>() {
            @Override
            public DenseMatrix apply(DenseVector vector) {
                DenseMatrix out = new DenseMatrix(nodes.length, 1);
                for (int i = 0; i < nodes.length; i++) {
                    double expo = Math.pow(nodes[i], vector.getValue(0));
                    out.setValue(i, 0, -expo * Math.log(vector.getValue(0)));
                }
                return out;
            }
        };
        DenseVector init = new DenseVector(3d / 2d);
        System.out.println(function.apply(new DenseVector(3d / 2d)));
        DenseVector solution = gauss_newton(function, der, init);
        System.out.println(solution);
//        System.out.println(function.apply(solution));
    }
}
