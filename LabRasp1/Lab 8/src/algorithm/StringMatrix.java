package algorithm;

import logger.TimeLogger;
import mpi.MPI;
import settings.Properties;

public class StringMatrix {
    public static void calculate(String[] args, int matrixSize) {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        var A = new Matrix(matrixSize);
        var B = new Matrix(matrixSize);
        var C = new Matrix(matrixSize);
        long startTime = 0L;

        if (rank == 0) {
            B.fillRandom(Properties.MAX_VALUE);
            A.fillRandom(Properties.MAX_VALUE);
            startTime = System.currentTimeMillis();
        }

        int[] matrixA = A.getMatrix();
        int[] matrixB = B.getMatrix();
        int[] matrixC = C.getMatrix();

        int lineHeight = matrixSize / size;

        int[] bufferA = new int[lineHeight * matrixSize];
        int[] bufferB = new int[lineHeight * matrixSize];
        int[] bufferC = new int[lineHeight * matrixSize];

        // Send & Receive data to other cores
        MPI.COMM_WORLD.Scatter(matrixA, 0, lineHeight * matrixSize, MPI.INT, bufferA, 0, lineHeight * matrixSize, MPI.INT, 0);
        MPI.COMM_WORLD.Scatter(matrixB, 0, lineHeight * matrixSize, MPI.INT, bufferB, 0, lineHeight * matrixSize, MPI.INT, 0);

        // Computer next & previous core
        int nextProc = (rank + 1) % size;
        int prevProc = rank - 1;
        if (prevProc < 0)
            prevProc = size - 1;
        int prevDataNum = rank;

        for (int p = 0; p < size; p++) {
            for (int i = 0; i < lineHeight; i++)
                for (int j = 0; j < matrixSize; j++)
                    for (int k = 0; k < lineHeight; k++)
                        bufferC[i * matrixSize + j] += bufferA[prevDataNum * lineHeight + i * matrixSize + k] * bufferB[k * matrixSize + j];
            prevDataNum -= 1;
            if (prevDataNum < 0)
                prevDataNum = size - 1;

            // Send data to next core
            MPI.COMM_WORLD.Sendrecv_replace(bufferB, 0, lineHeight * matrixSize, MPI.INT, nextProc, 0, prevProc, 0);
        }

        // Get data from other cores
        MPI.COMM_WORLD.Gather(bufferC, 0, lineHeight * matrixSize, MPI.INT, matrixC, 0, lineHeight * matrixSize, MPI.INT, 0);

        if (rank == 0) {
            TimeLogger.log("S", matrixSize, MPI.COMM_WORLD.Size(), System.currentTimeMillis() - startTime);
        }
        MPI.Finalize();
    }
}