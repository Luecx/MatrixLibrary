package core.vector;

public class ComplexNumber {

    private double realPart, imaginaryPart;

    public ComplexNumber(double realPart, double imaginaryPart) {
        this.realPart = realPart;
        this.imaginaryPart = imaginaryPart;
    }

    public ComplexNumber add(ComplexNumber other){
        return new ComplexNumber(this.realPart + other.realPart, this.imaginaryPart + other.imaginaryPart);
    }

    public ComplexNumber sub(ComplexNumber other){
        return new ComplexNumber(this.realPart - other.realPart, this.imaginaryPart - other.imaginaryPart);
    }

    public ComplexNumber scale(double scalar){
        return new ComplexNumber(this.realPart * scalar, this.imaginaryPart * scalar);
    }

    public ComplexNumber mul(ComplexNumber other){
        return new ComplexNumber(this.realPart * other.realPart - this.imaginaryPart * other.imaginaryPart,
        this.realPart * other.imaginaryPart - this.imaginaryPart * other.realPart);
    }

    public void self_add(ComplexNumber other){
        this.realPart += other.realPart;
        this.imaginaryPart += other.imaginaryPart;
    }

    public void self_sub(ComplexNumber other){
        this.realPart -= other.realPart;
        this.imaginaryPart -= other.imaginaryPart;
    }

    public void self_scale(double scalar){
        this.realPart *= scalar;
        this.imaginaryPart *= scalar;
    }

    public void self_mul(ComplexNumber other){
        double rp = this.realPart * other.realPart - this.imaginaryPart * other.imaginaryPart;
        this.imaginaryPart = this.realPart * other.imaginaryPart - this.imaginaryPart * other.realPart;
        this.realPart = rp;
    }


    public double norm(){
        return Math.sqrt(realPart*realPart + imaginaryPart*imaginaryPart);
    }

    public double getRealPart() {
        return realPart;
    }

    public void setRealPart(double realPart) {
        this.realPart = realPart;
    }

    public double getImaginaryPart() {
        return imaginaryPart;
    }

    public void setImaginaryPart(double imaginaryPart) {
        this.imaginaryPart = imaginaryPart;
    }
}
