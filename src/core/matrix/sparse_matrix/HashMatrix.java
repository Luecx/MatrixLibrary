package core.matrix.sparse_matrix;

import core.exceptions.NotSupportedOperation;
import core.matrix.Matrix;
import core.matrix.dense.DenseMatrix;
import core.vector.DenseVector;
import core.vector.Vector;

import java.util.HashMap;
import java.util.Objects;

public class HashMatrix extends Matrix<HashMatrix> {

    private static final int hash_factor = (int)Math.sqrt(Integer.MAX_VALUE);

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
    public double determinant() {
        throw new NotSupportedOperation();
    }

    @Override
    public DenseVector mul(Vector<?> vec) {
        throw new NotSupportedOperation();
    }

    @Override
    public DenseMatrix mul(Matrix<?> matrix) {
        throw new NotSupportedOperation();
    }

    @Override
    public HashMatrix add(HashMatrix matrix) {
        throw new NotSupportedOperation();
    }

    @Override
    public HashMatrix sub(HashMatrix matrix) {
        throw new NotSupportedOperation();
    }

    @Override
    public HashMatrix transpose() {
        throw new NotSupportedOperation();
    }

    @Override
    public HashMatrix self_transpose() {
        throw new NotSupportedOperation();
    }

    @Override
    public HashMatrix self_identity() {
        throw new NotSupportedOperation();
    }

    @Override
    public double norm_1() {
        throw new NotSupportedOperation();
    }

    @Override
    public double norm_infinity() {
        throw new NotSupportedOperation();
    }

    @Override
    public boolean isSymmetric() {
        throw new NotSupportedOperation();
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
        throw new NotSupportedOperation();
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

    public static void main(String[] args) {

    }

}
