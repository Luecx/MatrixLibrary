package core.matrix.sparse_matrix;

import core.exceptions.NotSupportedOperation;
import core.matrix.Matrix;
import core.matrix.dense.DenseMatrix;
import core.vector.DenseVector;
import core.vector.Vector;

import java.util.HashMap;
import java.util.Objects;

public class HashMatrix extends Matrix<HashMatrix> {

    private HashMap<Integer, Double>[] rows;

    public HashMatrix(int m, int n) {
        super(m, n);
        rows = new HashMap[m];
        for(int i = 0; i < m; i++){
            rows[i] = new HashMap<>();
        }
    }

    public HashMatrix(double[][] ar) {
        super(ar);
        rows = new HashMap[this.getM()];
        for(int i = 0; i < this.getM(); i++){
            rows[i] = new HashMap<>();
        }
        for(int i = 0; i < this.getM(); i++){
            for(int n = 0; n < this.getN(); n++){
                this.setValue(i,n,ar[i][n]);
            }
        }
    }

    HashMap<Integer, Double>[] getRows(){
        return rows;
    }

    @Override
    public void mul_partial_row(DenseVector target, Vector<?> vec, int row) {
        double v = 0;
        for (int i = 0; i < this.getN(); i++) {
            v += getValue(row, i) * vec.getValue(i);
        }
        vec.setValue(row, v);
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
    public void add_partial_row(HashMatrix target, HashMatrix matrix, int row) {
        for (int i = 0; i < this.getN(); i++) {
            target.setValue(row, i, target.getValue(row, i) + matrix.getValue(row, i));
        }
    }

    @Override
    public void sub_partial_row(HashMatrix target, HashMatrix matrix, int row) {
        for (int i = 0; i < this.getN(); i++) {
            target.setValue(row, i, target.getValue(row, i) - matrix.getValue(row, i));
        }
    }

    @Override
    public void scale_partial_row(HashMatrix target, double scalar, int row) {
        for(Integer i:rows[row].keySet()){
            this.rows[row].put(i, this.rows[row].get(i) * scalar);
        }
    }

    @Override
    public double determinant() {
        throw new NotSupportedOperation();
    }

    @Override
    public HashMatrix transpose() {
        HashMatrix mat = new HashMatrix(this.getN(), this.getM());
        for (int i = 0; i < this.getM(); i++) {
            for (int j = 0; j < this.getN(); j++) {
                mat.setValue(j, i, this.getValue(i, j));
            }
        }
        return mat;
    }

    @Override
    public HashMatrix self_identity() {
        for (int i = 0; i < Math.min(this.getM(), this.getN()); i++) {
            setValue(i,i,1);
        }
        return this;
    }

    @Override
    public HashMatrix self_transpose() {
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
                if (getValue(i,n) != getValue(n,i)) {
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
        this.rows[m].put(n, value);
    }

    @Override
    public double getValue(int m, int n) {
        Double v = this.rows[m].get(n);
        return v == null ? 0: (double)v;
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
    public HashMatrix copy() {
        throw new NotSupportedOperation();
    }

    @Override
    public DenseMatrix copyToDense() {
        throw new NotSupportedOperation();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(HashMap<Integer, Double> map:this.rows){
            for(int i = 0; i < this.getN(); i++){
                if(map.containsKey(i)){
                    builder.append(String.format("%.3E",map.get(i))+"  ");
                }else{
                    builder.append(String.format("%.3E",0.0)+"  ");
                }
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    @Override
    public int storageSize(){
        int s = 0;
        for(HashMap<Integer, Double> m:rows){
            s += m.size();
        }
        return s;
    }

    @Override
    public HashMatrix newInstance() {
        return new HashMatrix(this.getM(), this.getN());
    }

    public static void main(String[] args) {

    }

}
