package ru.zelourses.lab1.MatrixUtils;

public class MatrixFromRandomBuilder implements IMatrixBuilder<Matrix> {
    private double[] freeParts;
    private double[][] matrix;
    private int size;
    @Override
    public Matrix buildMatrix() {
        Matrix matrix = new Matrix(size);
        matrix.setFreeParts(freeParts);
        matrix.setMatrix(this.matrix);
        return matrix;
    }
    public MatrixFromRandomBuilder(double[][] matrix, double[] freeParts, int size){
        this.freeParts = freeParts;
        this.matrix = matrix;
        this.size = size;
    }
}
