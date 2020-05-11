package core.matrix.dense;

import core.exceptions.NotSupportedOperation;
import core.matrix.Matrix;
import core.matrix.sparse_matrix.HashMatrix;
import core.matrix.sparse_matrix.SparseMatrix;
import core.solver.Utilities;
import core.solver.decomposition.QRDecomposition;
import core.solver.direct.Solver;
import core.threads.Pool;
import core.vector.DenseVector;
import core.vector.Vector;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class DenseMatrix extends Matrix<DenseMatrix> {

    private double[][] values;

    public DenseMatrix(int m, int n) {
        super(m, n);
        values = new double[m][n];
    }

    public DenseMatrix(double[][] values) {
        super(values);
        this.values = new double[this.getM()][this.getN()];
        for (int i = 0; i < this.getM(); i++) {
            for (int n = 0; n < this.getN(); n++) {
                this.values[i][n] = values[i][n];
            }
        }
    }


    public DenseMatrix randomise(double lower, double upper) {
        for (int i = 0; i < this.getM(); i++) {
            for (int n = 0; n < this.getN(); n++) {
                this.values[i][n] = Math.random() * (upper - lower) + lower;
            }
        }
        return this;
    }

    @Override
    public void mul_partial_row(DenseVector target, Vector<?> vec, int row) {
        double v = 0;
        for (int i = 0; i < this.getN(); i++) {
            v += getValue(row, i) * vec.getValue(i);
        }
        target.setValue(row, v);
    }

    @Override
    public void mul_partial_row(DenseMatrix target, Matrix<?> matrix, int row) {
        for (int j = 0; j < matrix.getN(); j++) {
            double sum = 0;
            for (int k = 0; k < this.getN(); k++) {
                sum += getValue(row, k) * matrix.getValue(k, j);
            }
            target.setValue(row, j, sum);
        }
    }

    @Override
    public void add_partial_row(DenseMatrix target, DenseMatrix matrix, int row) {
        for (int i = 0; i < this.getN(); i++) {
            target.setValue(row, i, target.getValue(row, i) + matrix.getValue(row, i));
        }
    }

    @Override
    public void sub_partial_row(DenseMatrix target, DenseMatrix matrix, int row) {
        for (int i = 0; i < this.getN(); i++) {
            target.setValue(row, i, target.getValue(row, i) - matrix.getValue(row, i));
        }
    }

    @Override
    public void scale_partial_row(DenseMatrix target, double scalar, int row) {
        for (int i = 0; i < this.getN(); i++) {
            target.setValue(row, i, this.getValue(row, i) * scalar);
        }
    }

    @Override
    public DenseMatrix transpose() {
        DenseMatrix mat = new DenseMatrix(this.getN(), this.getM());
        for (int i = 0; i < this.getM(); i++) {
            for (int j = 0; j < this.getN(); j++) {
                mat.setValue(j, i, this.getValue(i, j));
            }
        }
        return mat;
    }

    @Override
    public DenseMatrix self_identity() {
        for (int i = 0; i < Math.min(this.getM(), this.getN()); i++) {
            values[i][i] = 1;
        }
        return this;
    }

    @Override
    public DenseMatrix self_transpose() {
        if (this.getM() != this.getN()) throw new RuntimeException();
        for (int i = 0; i < this.getM(); i++) {
            for (int j = 0; j < i; j++) {
                double v = this.getValue(i, j);
                this.setValue(i, j, this.getValue(j, i));
                this.setValue(j, i, v);
            }
        }
        return this;
    }

    @Override
    public double norm_1() {
        double max = 0;
        double sum;
        for (int i = 0; i < this.getM(); i++) {
            sum = 0;
            for (int n = 0; n < this.getN(); n++) {
                sum += Math.abs(getValue(i, n));
            }
            if (sum > max) max = sum;
        }
        return max;
    }

    @Override
    public double norm_infinity() {
        double max = 0;
        double sum;
        for (int i = 0; i < this.getN(); i++) {
            sum = 0;
            for (int n = 0; n < this.getM(); n++) {
                sum += Math.abs(getValue(n, i));
            }
            if (sum > max) max = sum;
        }
        return max;
    }

    @Override
    public boolean isSymmetric() {
        for (int i = 0; i < this.getN(); i++) {
            for (int n = 0; n < i; n++) {
                if (values[i][n] != values[n][i]) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public void swapRow(int r1, int row2) {
        for(int i = 0; i < this.getM(); i++){
            double a = getValue(r1,i);
            setValue(r1,i,getValue(row2,i));
            setValue(row2,i,a);
        }
    }

    @Override
    public void swapColumn(int c1, int c2) {
        for(int i = 0; i < this.getN(); i++){
            double a = getValue(i,c1);
            setValue(i,c1,getValue(i,c2));
            setValue(i,c2,a);
        }    }

    @Override
    public void scale_column(int column, double scalar) {
        for (int i = 0; i < this.getN(); i++) {
            this.setValue(i, column, this.getValue(i, column) * scalar);
        }
    }

    @Override
    public void scale_row(int row, double scalar) {
        scale_partial_row(this, scalar, row);
    }

    @Override
    public void setValue(int m, int n, double value) {
        values[m][n] = value;
    }

    @Override
    public double getValue(int m, int n) {
        return values[m][n];
    }

    @Override
    public boolean hasValue(double v) {
        for (int i = 0; i < this.getM(); i++) {
            for (int n = 0; n < this.getN(); n++) {
                if (values[i][n] == v) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void replaceValue(double v, double r) {
        for (int i = 0; i < this.getM(); i++) {
            for (int n = 0; n < this.getN(); n++) {
                if (values[i][n] == v) {
                    values[i][n] = r;
                }
            }
        }
    }

    @Override
    public DenseMatrix copy() {
        return new DenseMatrix(this.values);
    }

    @Override
    public DenseMatrix copyToDense() {
        return new DenseMatrix(this.values);
    }

    @Override
    public DenseMatrix newInstance() {
        return new DenseMatrix(this.getM(), this.getN());
    }


    @Override
    public double determinant() {
        if (this.getN() != this.getM()) return 0;
        QRDecomposition qr = QRDecomposition.givens(this);
        double det = 1;
        for (int i = 0; i < qr.getR().getN(); i++) {
            det *= qr.getR().getValue(i, i);
        }
        return det;
    }

    @Override
    public int storageSize() {
        return this.getM() * this.getN();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.getM(); i++) {
            for (int n = 0; n < this.getN(); n++) {

                //builder.append(getValue(i,n));
                builder.append(String.format("%-8.3f ", getValue(i,n)));
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public String toString(int c) {
        String s = new String();
        for (int i = 0; i < this.getM(); i++) {
            for (int n = 0; n < this.getN(); n++) {
                //s+=((String.format("%.3E",this.getValue(i,n)) + "              ").substring(0,10) + "  ");
                s += ((this.getValue(i, n) + "                                  ").substring(0, c) + "  ");
            }
            s += "\n";
        }
        return s;
    }

    public void applyFilter(Function<Double, Double> func) {
        for (int i = 0; i < this.getM(); i++) {
            for (int n = 0; n < this.getN(); n++) {
                this.setValue(i, n, func.apply(this.getValue(i, n)));
            }
        }
    }

    public double[][] getValues() {
        return values;
    }


    public static void main(String[] args) {
//        SparseMatrix mat1 = new SparseMatrix(Utilities.generateSymmetricPositiveDefiniteMatrix(HashMatrix.class, 20));
////        Pool pool = new Pool(8);
////
////        for(int i = 0; i < 1000; i++)
////            mat1.mul(new DenseVector(20),pool);
////
////        pool.stop();
        DenseMatrix denseMatrix = new DenseMatrix(4,4);
        System.out.println(denseMatrix);

    }
}
