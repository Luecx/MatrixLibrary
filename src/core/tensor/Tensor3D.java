package core.tensor;

public class Tensor3D extends Tensor {

    int pd1, pd2, pd3;

    public Tensor3D(Tensor3D tensor) {
        super(tensor);
        updatePartialDimensions();
    }

    public Tensor3D(double[] data, int d1, int d2, int d3) {
        super(data, d1, d2, d3);
        updatePartialDimensions();
    }

    public Tensor3D(int d1, int d2, int d3) {
        super(d1, d2, d3);
        updatePartialDimensions();
    }

    public int index(int d1, int d2, int d3) {
        return d1 * pd1 + d2 * pd2 + d3 * pd3;
    }

    public double get(int d1, int d2, int d3) {
        int index = index(d1, d2, d3);
        return data[index];
    }

    public void set(double val, int d1, int d2, int d3) {
        int index = index(d1, d2, d3);
        data[index] = val;
    }

    @Override
    public void transpose(int dimA, int dimB) {
        super.transpose(dimA, dimB);
        updatePartialDimensions();
    }

    @Override
    public Tensor3D copy() {
        return new Tensor3D(this);
    }

    private void updatePartialDimensions() {
        this.pd1 = partialDimensions[0];
        this.pd2 = partialDimensions[1];
        this.pd3 = partialDimensions[2];
    }


    public String toString() {
        String s = new String();
        for (int i = 0; i < this.getDimension(0); i++) {
            for (int n = 0; n < this.getDimension(1); n++) {
                for (int j = 0; j < this.getDimension(2); j++) {
                    //s+=((String.format("%.3E",this.getValue(i,n)) + "              ").substring(0,10) + "  ");
                    s += ((this.get(i, n, j) + "             ").substring(0, 10) + "  ");
                }
                s += "\n";
            }
            s += "\n";
            s += "\n";
        }
        return s;
    }
}