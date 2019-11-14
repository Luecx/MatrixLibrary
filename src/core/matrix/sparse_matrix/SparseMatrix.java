package core.matrix.sparse_matrix;

import core.exceptions.NotSupportedOperation;
import core.matrix.Matrix;
import core.matrix.dense.DenseMatrix;
import core.vector.Vector;
import core.vector.DenseVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * using CSR format
 */
public class SparseMatrix extends Matrix<SparseMatrix> {


    private ArrayList<Double> val = new ArrayList<>();
    private ArrayList<Integer> col_index = new ArrayList<>();
    private ArrayList<Integer> row_ptr = new ArrayList<>();


    public SparseMatrix(int m, int n) {
        super(m, n);
        for(int i = 0; i < this.getM() + 1; i++){
            row_ptr.add(0);
        }
    }

    public SparseMatrix(DenseMatrix matrix) {
        this(matrix.getValues());
    }

    public SparseMatrix(HashMatrix hashMatrix){
        super(hashMatrix);
        row_ptr.add(0);
        HashMap<Integer, Double>[] map = hashMatrix.getRows();
        int total = 0;
        for(int i = 0; i < map.length; i++){
            total += map[i].size();
            row_ptr.add(total);
            List<Integer> keys = map[i].keySet().stream().sorted().collect(Collectors.toList());
            for(Integer n:keys){
                col_index.add(n);
                val.add(map[i].get(n));
            }
        }
    }

    public SparseMatrix(double[][] ar) {
        super(ar);
        row_ptr.add(0);
        int counter = 0;
        for(int i = 0; i < this.getM(); i++){
            for(int n = 0; n < this.getN(); n++){
                if(ar[i][n] != 0){
                    counter++;
                    val.add(ar[i][n]);
                    col_index.add(n);
                }
            }
            row_ptr.add(counter);
        }
    }

    ArrayList<Double> getVal() {
        return val;
    }

    ArrayList<Integer> getCol_index() {
        return col_index;
    }

    ArrayList<Integer> getRow_ptr() {
        return row_ptr;
    }

    public void printCSRFormat() {
        System.out.print("values    :");
        for(int i = 0; i < val.size(); i++){
            System.out.format("%5s", val.get(i));
        }
        System.out.print("\ncol_index :");
        for(int i = 0; i < col_index.size(); i++){
            System.out.format("%5s", col_index.get(i));
        }
        System.out.print("\nrow_ptr.  :");
        for(int i = 0; i < row_ptr.size(); i++){
            System.out.format("%5s", row_ptr.get(i));
        }
        System.out.println();
    }

    @Override
    public int storageSize(){
        return this.row_ptr.get(row_ptr.size()-1);
    }

    @Override
    public DenseVector mul(Vector<?> vec) {
        if(vec.getSize() != this.getN()) throw new RuntimeException();
        DenseVector denseVector = new DenseVector(this.getN());

        for(int i = 0; i < this.getM(); i++){
            double sum = 0;
            for(int n = row_ptr.get(i); n < row_ptr.get(i+1); n++){
                sum += vec.getValue(col_index.get(n)) * this.val.get(n);
            }
            denseVector.setValue(i, sum);
        }

        return denseVector;
    }

    @Override
    public DenseMatrix mul(Matrix<?> matrix) {
        throw new NotSupportedOperation();
    }

    @Override
    public SparseMatrix transpose() {
        throw new NotSupportedOperation();
    }

    @Override
    public SparseMatrix add(SparseMatrix matrix) {
        throw new NotSupportedOperation();
    }

    @Override
    public SparseMatrix sub(SparseMatrix matrix) {
        throw new NotSupportedOperation();
    }

    @Override
    public SparseMatrix self_transpose() {
        throw new NotSupportedOperation();
    }

    @Override
    public SparseMatrix self_identity() {
        for(int i = 0; i < Math.min(this.getM(), this.getN()); i++){
            this.setValue(i,i,1);
        }
        return this;
    }

    @Override
    public double determinant() {
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
        int insertIndex = row_ptr.get(m+1);
        for(int i = row_ptr.get(m); i < row_ptr.get(m+1); i++){
            if(col_index.get(i) < n){
                continue;
            }else if(col_index.get(i) == n){
                val.set(i, value);
                return;
            }else if(col_index.get(i) > n){
                insertIndex = i;
                break;
            }
        }
        col_index.add(insertIndex, n);
        val.add(insertIndex, value);
        for(int i = m+1; i < getM() + 1; i++){
            row_ptr.set(i, row_ptr.get(i) + 1);
        }
    }

    @Override
    public double getValue(int m, int n) {
        for(int i = row_ptr.get(m); i < row_ptr.get(m+1); i++){
            if(col_index.get(i) == n){
                return val.get(i);
            }
        }
        return 0;
    }

    @Override
    public boolean hasValue(double v) {
        for(double va:val){
            if(va == v) return true;
        }return false;
    }

    @Override
    public void replaceValue(double v, double r) {
        throw new NotSupportedOperation();
    }

    @Override
    public SparseMatrix copy() {
        SparseMatrix m = new SparseMatrix(this.getM(), this.getN());
        m.val = new ArrayList<>(this.val);
        m.row_ptr = new ArrayList<>(row_ptr);
        m.col_index = new ArrayList<>(col_index);
        return m;
    }

    @Override
    public DenseMatrix copyToDense() {
        DenseMatrix matrix = new DenseMatrix(this.getM(), this.getN());
        for(int m = 0; m < this.getM(); m++){
            for(int i = row_ptr.get(m); i < row_ptr.get(m+1); i++){
                matrix.setValue(m, col_index.get(i), val.get(i));
            }
        }
        return matrix;
    }

    @Override
    public String toString() {
        String s = new String();
        for(int i = 0; i < this.getM(); i++){
            for(int n = 0; n < this.getN(); n++){
                System.out.print((this.getValue(i,n) + "                               ").substring(0,14) + "  ");
            }
            System.out.println();
        }

        return s;
    }


    public static void main(String[] args) {
        SparseMatrix m = new SparseMatrix(4,5);
        m.setValue(1,2,11);
        m.setValue(0,0,10);
        m.setValue(0,3,12);
        m.setValue(1,4,13);
        m.setValue(2,1, 16);
        m.setValue(3,2,11);
        m.setValue(3,4,13);

        System.out.println(m);

        HashMatrix m2 = new HashMatrix(4,5);
        m2.setValue(1,2,11);
        m2.setValue(0,0,10);
        m2.setValue(0,3,12);
        m2.setValue(1,4,13);
        m2.setValue(2,1, 16);
        m2.setValue(3,2,11);
        m2.setValue(3,4,13);

        System.out.println(m2);
        new SparseMatrix(m2);
        System.out.println(new SparseMatrix(m2));
        //System.out.println(new SparseMatrix(m2));

    }
}
