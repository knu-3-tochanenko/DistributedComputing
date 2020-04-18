package algorithm;

import java.util.Arrays;
import java.util.Random;

public class Matrix {
    // Матриця у вигляді одномірного масиву
    private int[][] matrix;

    // Розміри матриці
    private int size;

    Matrix(int size) {
        this.size = size;
        matrix = new int[size][];
        for (int i = 0; i < size; i++)
            matrix[i] = new int[size];
    }

    Matrix(int[] lineMatrix) {
//        System.out.println("Constructor with line Started");
        size = (int)Math.sqrt(lineMatrix.length);

        matrix = new int[size][];
        for (int i = 0; i < size; i++) {
            matrix[i] = new int[size];
            for (int j = 0; j < size; j++) {
                matrix[i][j] = lineMatrix[i * size + j];
            }
        }
//        System.out.println("Constructor with line Finished");
    }

    public void copyFromLine(int[] lineMatrix) {
//        System.out.println("Copy From Line Started");

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = lineMatrix[i * size + j];
            }
        }
//        System.out.println("Copy From Line Finished");

    }

    // Заповнення матриці випадковими цілими числами у заданих межах
    public void fillRandom(int maxNumber) {
        var rand = new Random(System.currentTimeMillis());
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                matrix[i][j] = rand.nextInt(maxNumber);
    }

    public int[] getMatrix() {
//        System.out.println("Get algorithm.Matrix Started");
        int[] linedMatrix = new int[size * size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                linedMatrix[i * size + j] = matrix[i][j];
//        System.out.println("Get algorithm.Matrix Finished");
        return linedMatrix;
    }

    public int getSize() {
        return size;
    }

    public int get(int i, int j) {
        return matrix[i][j];
    }

    public void set(int i, int j, int value) {
        matrix[i][j] = value;
    }

    public void add(int i, int j, int value) {
        matrix[i][j] += value;
    }
}