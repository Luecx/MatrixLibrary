package core.tensor;

import core.exceptions.NotMatchingSlotsException;

import java.util.Arrays;

public class Tensor4D extends Tensor{

    int pd1,pd2,pd3,pd4;

    public Tensor4D(Tensor4D tensor) {
        super(tensor);
        updatePartialDimensions();
    }

    public Tensor4D(double[] data, int d1, int d2, int d3, int d4) {
        super(data, d1,d2,d3,d4);
        updatePartialDimensions();
    }

    public Tensor4D(Tensor3D... tensors){
        this.dimensions = new int[4];
        this.dimensions[0] = tensors.length;
        this.dimensions[1] = tensors[0].dimensions[0];
        this.dimensions[2] = tensors[0].dimensions[1];
        this.dimensions[3] = tensors[0].dimensions[2];

        this.partialDimensions = new int[]{
                1,
                this.dimensions[0],
                this.dimensions[1] * this.dimensions[0],
                this.dimensions[2] * this.dimensions[1] * this.dimensions[0]};

        this.size = this.dimensions[0] * this.dimensions[1] * this.dimensions[2] * this.dimensions[3];
        this.data = new double[size];

        updatePartialDimensions();
        for(int i = 0; i < this.dimensions[0]; i++){
            for(int n = 0; n < this.dimensions[1]; n++){
                for(int j = 0; j < this.dimensions[2]; j++){
                    for(int k = 0; k < this.dimensions[3]; k++){
                        this.data[index(i,n,j,k)] = tensors[i].get(n,j,k);

                    }
                }
            }
        }
    }

    public Tensor4D(int d1, int d2, int d3, int d4) {
        super(d1,d2,d3,d4);
        updatePartialDimensions();
    }

    public int index(int d1, int d2, int d3, int d4) {
        return d1 * pd1 + d2 * pd2 + d3 * pd3 + pd4 * d4;
    }

    public double get(int d1, int d2, int d3, int d4) {
        int index = index(d1,d2,d3,d4);
        return data[index];
    }

    public void set(double val, int d1, int d2, int d3, int d4) {
        int index = index(d1,d2,d3,d4);
        data[index] = val;
    }

    @Override
    public void transpose(int dimA, int dimB) {
        super.transpose(dimA, dimB);
        updatePartialDimensions();
    }

    @Override
    public Tensor4D copy() {
        return new Tensor4D(this);
    }

    private void updatePartialDimensions(){
        this.pd1 = partialDimensions[0];
        this.pd2 = partialDimensions[1];
        this.pd3 = partialDimensions[2];
        this.pd4 = partialDimensions[3];
    }



}
