package core.matrix.dense;

import core.exceptions.NotSupportedOperation;
import core.matrix.Matrix;
import core.solver.decomposition.QRDecomposition;
import core.vector.DenseVector;
import core.vector.Vector;

import java.util.function.Function;

public class DenseMatrix extends Matrix<DenseMatrix> {

    private double[][] values;
    protected boolean transpose_tag = false;


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

    public DenseMatrix setTransposeTag(boolean tag) {
        this.transpose_tag = tag;
        return this;
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
        if(this.getM() != this.getN()) throw new RuntimeException();
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
    public DenseMatrix mul(Matrix<?> matrix) {
        DenseMatrix m = new DenseMatrix(this.getM(), matrix.getN());
        for (int i = 0; i < this.getM(); i++) {
            for (int j = 0; j < matrix.getN(); j++) {
                double sum = 0;
                for (int k = 0; k < this.getN(); k++) {
                    sum += getValue(i, k) * matrix.getValue(k, j);
                }
                m.setValue(i, j, sum);
            }
        }
        return m;
    }

    @Override
    public DenseVector mul(Vector<?> vec) {
        if (vec.getSize() != this.getN()) throw new RuntimeException();


        DenseVector denseVector = new DenseVector(this.getM());

        for (int i = 0; i < this.getM(); i++) {
            double sum = 0;
            for (int n = 0; n < this.getN(); n++) {
                sum += getValue(i, n) * vec.getValue(n);
            }
            denseVector.setValue(i, sum);
        }
        return denseVector;

    }

    @Override
    public void swapRow(int r1, int row2) {
        throw new NotSupportedOperation();
    }

    @Override
    public void sawpColumn(int c1, int c2) {
        throw new NotSupportedOperation();
    }

    @Override
    public void scale(double scalar) {
        for (int i = 0; i < this.getM(); i++) {
            for (int n = 0; n < this.getN(); n++) {
                values[i][n] *= scalar;
            }
        }
    }

    @Override
    public void scale_column(int column, double scalar) {
        throw new NotSupportedOperation();
    }

    @Override
    public void scale_row(int column, double scalar) {
        throw new NotSupportedOperation();
    }

    @Override
    public void setValue(int m, int n, double value) {
        values[m][n] = value;
    }

    @Override
    public double getValue(int m, int n) {
        return transpose_tag ? values[n][m] : values[m][n];
    }

    @Override
    public boolean hasValue(double v) {
        throw new NotSupportedOperation();
    }

    @Override
    public void replaceValue(double v, double r) {
        throw new NotSupportedOperation();
    }

    @Override
    public DenseMatrix add(DenseMatrix matrix) {
        DenseMatrix res = new DenseMatrix(this.getM(), this.getN());
        for (int i = 0; i < this.getM(); i++) {
            for (int n = 0; n < this.getN(); n++) {
                res.setValue(i, n, this.getValue(i, n) + matrix.getValue(i, n));
            }
        }
        return res;
    }

    @Override
    public DenseMatrix sub(DenseMatrix matrix) {
        DenseMatrix res = new DenseMatrix(this.getM(), this.getN());
        for (int i = 0; i < this.getM(); i++) {
            for (int n = 0; n < this.getN(); n++) {
                res.setValue(i, n, this.getValue(i, n) - matrix.getValue(i, n));
            }
        }
        return res;
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
        String s = new String();
        for (int i = 0; i < this.getM(); i++) {
            for (int n = 0; n < this.getN(); n++) {
                //s+=((String.format("%.3E",this.getValue(i,n)) + "              ").substring(0,10) + "  ");
                s += ((this.getValue(i, n) + "                                  ").substring(0, 10) + "  ");
            }
            s += "\n";
        }

        return s;
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
}
