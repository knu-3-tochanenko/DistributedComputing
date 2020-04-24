package algorithm;

import logger.TimeLogger;
import mpi.MPI;
import settings.Properties;

public class SimpleMatrix {
    public static void calculate(String[] args, int matrixSize) {
        int lines;

        Matrix A = new Matrix(matrixSize);
        Matrix B = new Matrix(matrixSize);
        Matrix C = new Matrix(matrixSize);

        int[] matrixA = new int[matrixSize * matrixSize];
        int[] matrixB = new int[matrixSize * matrixSize];
        int[] matrixC = new int[matrixSize * matrixSize];
        long startTime = 0L;

        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        if (rank == 0) {
            A.fillRandom(Properties.MAX_VALUE);
            B.fillRandom(Properties.MAX_VALUE);

            matrixA = A.getMatrix();
            matrixB = B.getMatrix();
            matrixC = C.getMatrix();

            startTime = System.currentTimeMillis();
        }

        lines = matrixSize / size;
        int[] ai = new int[lines * matrixSize];
        int[] bi = new int[matrixSize * matrixSize];
        int[] ci = new int[lines * matrixSize];

        // Send matrix A to other cores
        if (rank == 0) {
            for (int i = 0; i < lines; i++)
                if (matrixSize >= 0) System.arraycopy(matrixA, i * matrixSize, ai, i * matrixSize, matrixSize);
            for (int i = 1; i < size; i++) {
                MPI.COMM_WORLD.Send(matrixA, i * lines * matrixSize, lines * matrixSize, MPI.INT, i, i);
            }
        } else {
            MPI.COMM_WORLD.Recv(ai, 0, lines * matrixSize, MPI.INT, 0, rank);
        }

        // Send matrix B to other cores
        if (rank == 0) {
            for (int i = 0; i < matrixSize; i++)
                System.arraycopy(matrixB, i * matrixSize, bi, i * matrixSize, matrixSize);
            for (int i = 1; i < size; i++) {
                MPI.COMM_WORLD.Send(matrixB, 0, matrixSize * matrixSize, MPI.INT, i, i);
            }
        } else {
            MPI.COMM_WORLD.Recv(bi, 0, matrixSize * matrixSize, MPI.INT, 0, rank);
        }

        // Multiply matrices
        for (int i = 0; i < lines; i++)
            for (int j = 0; j < matrixSize; j++) {
                ci[i * matrixSize + j] = 0;
                for (int k = 0; k < matrixSize; k++) {
                    ci[i * matrixSize + j] += ai[i * matrixSize + k] * bi[k * matrixSize + j];
                }
            }

        // Send & Receive result
        if (rank != 0) {
            MPI.COMM_WORLD.Send(ci, 0, lines * matrixSize, MPI.INT, 0, rank);
        } else {
            for (int i = 0; i < lines; i++)
                if (matrixSize >= 0) System.arraycopy(ci, i * matrixSize, matrixC, i * matrixSize, matrixSize);
            for (int i = 1; i < size; i++)
                MPI.COMM_WORLD.Recv(matrixC, i * lines * matrixSize, lines * matrixSize, MPI.INT, i, i);
        }

        if (rank == 0) {
            TimeLogger.log("B", matrixSize, MPI.COMM_WORLD.Size(), System.currentTimeMillis() - startTime);
        }

        MPI.Finalize();
    }
}