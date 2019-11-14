package core.solver;

import core.matrix.Matrix;
import core.matrix.dense.DenseMatrix;
import core.matrix.sparse_matrix.HashMatrix;
import core.matrix.sparse_matrix.SparseMatrix;
import core.vector.DenseVector;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Utilities {

    public static <T extends Matrix<T>> T generateSymmetricPositiveDefiniteMatrix(Class<T> clazz, int w){
       return generateSymmetricPositiveDefiniteMatrix(clazz, w, (int)(Math.random() * 100000));
    }
    public static <T extends Matrix<T>> T generateSymmetricPositiveDefiniteMatrix(Class<T> clazz, int w, int seed){
        Random r = new Random(seed);
        T A = null;
        try {
            A = clazz.getConstructor(int.class, int.class).newInstance(w,w);
        } catch (Exception e){
            throw new RuntimeException("That matrix class did not override the constructor: Matrix(m,n)!");
        }
        for(int i = 0; i < w; i++){
            A.setValue(i,i,1);
        }

        for(int i = 0; i < w; i++){
            int i1 = (int)(w * r.nextDouble());
            int i2 = (int)(w * r.nextDouble());

            double v1 = r.nextDouble();
            double v2 = r.nextDouble();

            A.setValue(i1,i1,A.getValue(i1,i1)+v1*v1);
            A.setValue(i1,i2,A.getValue(i1,i2)+v1*v2);
            A.setValue(i2,i1,A.getValue(i2,i1)+v2*v1);
            A.setValue(i2,i2,A.getValue(i2,i2)+v2*v2);
        }
        return A;
    }


    public static void measure(int size, Class... classes){

        ArrayList<Class> cls = new ArrayList<>();
        ArrayList<Matrix> ar = new ArrayList<>();
        ArrayList<Matrix> ar2 = new ArrayList<>();
        System.out.format("%-30s", "");
        for(int i = 0; i < classes.length; i++){
            if(!Matrix.class.isAssignableFrom(classes[i])){
                continue;
            }
            System.out.format("%-30s", classes[i].getSimpleName());
            cls.add(classes[i]);
        }
//        System.out.println();
//        System.out.format("%-30s", "full insert: ");
//        for(Class c:cls){
//            try{
//                Matrix m = (Matrix) c.getConstructor(int.class, int.class).newInstance(size,size);
//                long nano = System.nanoTime();
//                for(int i = 0; i < size; i++){
//                    for(int n = 0; n < size; n++){
//                        m.setValue(i,n, Math.random());
//                    }
//                }
//                System.out.format("%-30s", Math.round((System.nanoTime() - nano) / 1E3)/1E3 + " ms (" + m.storageSize()+")");
//            }catch (Exception e){
//                System.out.format("%-30s", "-ERROR-");
//            }
//            System.gc();
//        }

//        System.out.println();
//        System.out.format("%-30s", "full read: ");
//        for(Matrix m:ar){
//            if(m == null){ continue;}
//            try{
//                long nano = System.nanoTime();
//                for(int i = 0; i < size; i++){
//                    for(int n = 0; n < size; n++){
//                        m.getValue(n,i);
//                    }
//                }
//                System.out.format("%-30s", Math.round((System.nanoTime() - nano) / 1E3)/1E3 + " ms (" + m.storageSize()+")");
//            }catch (Exception e){
//                System.out.format("%-30s", "-ERROR-");
//            }
//            System.gc();
//        }

        System.out.println();
        System.out.format("%-30s", "sparse spd fill: ");
        int index = 0;
        for(Class c:cls){
            try{
                long nano = System.nanoTime();
                Matrix m = generateSymmetricPositiveDefiniteMatrix(c, size,10);
                ar2.add(m);
                System.out.format("%-30s", Math.round((System.nanoTime() - nano) / 1E3)/1E3 + " ms (" + m.storageSize()+")");
            }catch (Exception e){
                ar2.add(new DenseMatrix(0,0));
                System.out.format("%-30s", "-ERROR-");
            }
            index ++;
            System.gc();
        }

        System.out.println();
        System.out.format("%-30s", "sparse vector mul: ");
        DenseVector vec = new DenseVector(size);
        vec.randomise(0,1);
        for(Matrix m:ar2){
            if(m.getN() == 0){
                System.out.format("%-30s", "-ERROR-");
                continue;}
            try{
                long nano = System.nanoTime();
                m.mul(vec);
                System.out.format("%-30s", Math.round((System.nanoTime() - nano) / 1E3)/1E3 + " ms (" + m.storageSize()+")");
            }catch (Exception e){
                System.out.format("%-30s", "-ERROR-");
            }
            System.gc();
        }

        System.out.println();
        System.out.format("%-30s", "to sparse format: ");
        for(Matrix m:ar2){
            if(m.getN() == 0){
                System.out.format("%-30s", "-ERROR-");
                continue;}
            try{
                long nano = System.nanoTime();
                if(m instanceof SparseMatrix){
                    m.copy();
                }else if(m instanceof DenseMatrix){
                    new SparseMatrix((DenseMatrix) m);
                }else if(m instanceof HashMatrix){
                    new SparseMatrix((HashMatrix) m);
                }
                System.out.format("%-30s", Math.round((System.nanoTime() - nano) / 1E3)/1E3 + " ms (" + m.storageSize()+")");
            }catch (Exception e){
                e.printStackTrace();
                System.out.format("%-30s", "-ERROR-");
            }
            System.gc();
        }
    }

    public static void main(String[] args) {
        measure(5000, DenseMatrix.class, SparseMatrix.class, HashMatrix.class);
    }

}
