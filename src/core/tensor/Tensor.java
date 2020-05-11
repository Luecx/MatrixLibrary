package core.tensor;

import core.exceptions.NotMatchingSlotsException;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

public class Tensor implements Serializable {

    protected double[] data;

    protected int[] partialDimensions;
    protected int[] dimensions;
    protected int size;

    public Tensor(Tensor tensor){
        this.partialDimensions = Arrays.copyOf(tensor.partialDimensions, tensor.partialDimensions.length);
        this.dimensions = Arrays.copyOf(tensor.dimensions, tensor.dimensions.length);
        this.size = tensor.size;
        this.data = Arrays.copyOf(tensor.data, tensor.data.length);
    }

    public Tensor(double[] data, int... dimensions) {
        this(dimensions);
        if (this.size != data.length) throw new RuntimeException("array size doesnt fit dimensions: " +
                Arrays.toString(dimensions) + " = " + this.size + " =/= " + data.length);
        this.dimensions = dimensions;
        this.size = 1;
        this.partialDimensions = new int[dimensions.length];
        for (int i = 0; i < dimensions.length; i++) {
            partialDimensions[i] = size;
            size *= dimensions[i];
        }
        this.data = data;
    }

    /**
     * 1D tensor
     * @param data
     */
    public Tensor(double... data) {
        this(data, data.length);
    }

    public Tensor(int... dimensions) {
        this.dimensions = dimensions;
        this.size = 1;
        this.partialDimensions = new int[dimensions.length];
        for (int i = 0; i < dimensions.length; i++) {
            partialDimensions[i] = size;
            size *= dimensions[i];
        }
        this.data = new double[this.size];
    }

    public void transpose(int dimA, int dimB) {
        int dim = partialDimensions[dimA];
        partialDimensions[dimA] = partialDimensions[dimB];
        partialDimensions[dimB] = dim;
        dim = dimensions[dim];
        dimensions[dimA] = dimensions[dimB];
        dimensions[dimB] = dim;
    }

    public void reset(double val){
        for(int i = 0; i < this.size; i++){
            this.data[i] = val;
        }
    }

    public int rank() {
        return dimensions.length;
    }

    public int size() {
        return size;
    }

    public int index(int... indices) {
        if (indices.length != dimensions.length)
            throw new NotMatchingSlotsException(this.dimensions.length, indices.length);
        int i = 0;
        for (int k = 0; k < indices.length; k++) {
            if (dimensions[k] <= indices[k]) throw new IndexOutOfBoundsException();
            i += partialDimensions[k] * indices[k];
        }
        return i;
    }

    public double[] getData() {
        return data;
    }

    public void setData(double[] data) {
        if(data.length == this.size)
            this.data = data;
    }

    public int getDimension(int dimension){
        return dimensions[dimension];
    }

    public int[] getDimensions(){
        return Arrays.copyOf(dimensions, dimensions.length);
    }

    public void randomizeRegular(double min, double max) {
        for(int i = 0; i < this.size; i++){
            data[i] = Math.random() * (max-min) + min;
        }
    }

    public void randomizeRegular(double min, double max, long seed){
        Random r = new Random(seed);
        for(int i = 0; i < this.size; i++){
            data[i] = r.nextDouble() * (max-min) + min;
        }
    }

    public double get(int... indices) {
        int index = index(indices);
        return data[index];
    }

    public void set(double val, int... indices) {
        int index = index(indices);
        data[index] = val;
    }

    public void add(double val, int... indices){
        int index = index(indices);
        data[index] += val;
    }

    public void add(double val){
        for(int i = 0; i < size; i++){
            data[i] += val;
        }
    }


    public Tensor self_apply(Function<Double, Double> consumer){
        for(int i = 0; i < data.length; i++){
            data[i] = consumer.apply(data[i]);
        }
        return this;
    }

    public Tensor self_add(Tensor other){
        for(int i = 0; i < Math.min(other.size, size); i++){
            data[i] += other.data[i];
        }
        return this;
    }

    public Tensor self_hadamard(Tensor other){
        for(int i = 0; i < Math.min(other.size, size); i++){
            data[i] *= other.data[i];
        }
        return this;
    }

    public Tensor self_sub(Tensor other){
        for(int i = 0; i < Math.min(other.size, size); i++){
            data[i] -= other.data[i];
        }
        return this;
    }

    public Tensor self_negate(){
        this.self_scale(-1);
        return this;
    }

    public Tensor self_normalise() {
        double min = min();
        double max = max();
        this.add(-min);
        this.self_scale(1d / (max-min));
        return this;
    }

    public Tensor self_scale(double scalar) {
        for(int i = 0; i < this.size; i++){
            data[i] *= scalar;
        }
        return this;
    }



    public Tensor apply(Function<Double, Double> consumer){
        return new Tensor(this).self_apply(consumer);
    }

    public Tensor add(Tensor other){
        return new Tensor(this).self_add(other);
    }

    public Tensor hadamard(Tensor other){
        return new Tensor(this).self_hadamard(other);
    }

    public Tensor sub(Tensor other){
        return new Tensor(this).self_sub(other);
    }

    public Tensor negate(){
        return new Tensor(this).self_negate();
    }

    public Tensor normalise() {
        return new Tensor(this).self_normalise();
    }

    public Tensor scale(double scalar) {
        return new Tensor(this).self_scale(scalar);
    }

    public double min() {
        double min = this.data[0];
        for (double d : this.data) {
            min = d < min ? d : min;
        }
        return min;
    }

    public double max() {
        double max = this.data[0];
        for (double d : this.data) {
            max = d > max ? d : max;
        }
        return max;
    }

    public Tensor copy() {
        return new Tensor(this);
    }

    @Override
    public String toString() {
        return "Tensor{" +
                "data=" + Arrays.toString(data) +
                '}';
    }

    public static void main(String[] args) {

        double[][] ar = new double[][]{

                {1,2},
                {3,4},
                {3,4}
        };

        Tensor2D t1 = new Tensor2D(ar);
        t1.self_normalise();
        System.out.println(t1);


    }
}
