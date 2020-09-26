package ru.zelourses.lab1.MatrixUtils;

public class Matrix implements Cloneable{
    private double[][] matrix;
    private double[] freeParts;
    private int size;
    public Matrix(int size){
        this.size = size;
        matrix = new double[size][size];
        freeParts = new double[size];
    }
    public Matrix(Matrix that){

    }
    public double[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }

    public double[] getFreeParts() {
        return freeParts;
    }

    public void setFreeParts(double[] freeParts) {
        this.freeParts = freeParts;
    }

    public int getSize() {
        return size;
    }
    @Override
    public Matrix clone() throws CloneNotSupportedException{
        return (Matrix)super.clone();
    }
}
