package algorithm;

import logger.TimeLogger;
import mpi.Cartcomm;
import mpi.MPI;

import java.util.Arrays;

public class FoxMatrix {
    // Coordinates in Cartesian Lattice
    private static int[] gridCoords = new int[2];

    private static Cartcomm ColComm;
    private static Cartcomm RowComm;

    // Send & Receive data between cores
    private static void matrixScatter(int[] matrix, int[] matrixBlock, int matSize, int blockSize) {
        int[] matrixRow = new int[blockSize * matSize];

        if (gridCoords[1] == 0)
            ColComm.Scatter(matrix, 0, blockSize * matSize, MPI.INT, matrixRow, 0, blockSize * matSize, MPI.INT, 0);

        for (int i = 0; i < blockSize; i++) {
            int[] subRow = Arrays.copyOfRange(matrixRow, i * matSize, matrixRow.length);
            int[] subRowRes = new int[blockSize];

            RowComm.Scatter(subRow, 0, blockSize, MPI.INT, subRowRes, 0, blockSize, MPI.INT, 0);
            System.arraycopy(subRowRes, 0, matrixBlock, i * blockSize, blockSize);
        }
    }

    public static void calculate(String[] args, int matrixSize) {
        MPI.Init(args);
        int procRank = MPI.COMM_WORLD.Rank();
        int procNum = MPI.COMM_WORLD.Size();

        int gridSize = (int) Math.sqrt(procNum);

        if (procNum != gridSize * gridSize) {
            if (procRank == 0)
                System.out.println("3) " + matrixSize + " x " + matrixSize + ", 0 ms (procNum != gridSize * gridSize)");
            MPI.Finalize();
            return;
        }

        Cartcomm gridComm;

        int blockSize = matrixSize / gridSize;

        var A = new Matrix(matrixSize);
        var B = new Matrix(matrixSize);
        var C = new Matrix(matrixSize);

        int[] ABlock = new int[blockSize * blockSize];
        int[] BBlock = new int[blockSize * blockSize];
        int[] CBlock = new int[blockSize * blockSize];
        int[] tempABlock = new int[blockSize * blockSize];

        long startTime = 0L;
        if (procRank == 0) {
            A.fillRandom(5);
            B.fillRandom(5);
            startTime = System.currentTimeMillis();
        }

        int[] matrixA = A.getMatrix();
        int[] matrixB = B.getMatrix();
        int[] matrixC = C.getMatrix();

        boolean[] subdims = new boolean[2];

        gridComm = MPI.COMM_WORLD.Create_cart(new int[]{gridSize, gridSize}, new boolean[]{false, false}, true);

        gridCoords = gridComm.Coords(procRank);
        subdims[1] = true;
        RowComm = gridComm.Sub(subdims);

        subdims[0] = true;
        subdims[1] = false;
        ColComm = gridComm.Sub(subdims);

        // Send data to different cells
        matrixScatter(matrixA, tempABlock, matrixSize, blockSize);
        matrixScatter(matrixB, BBlock, matrixSize, blockSize);

        // Multiply
        for (int iter = 0; iter < gridSize; iter++) {
            int pivot = (gridCoords[0] + iter) % gridSize;

            if (gridCoords[1] == pivot)
                if (blockSize * blockSize >= 0)
                    System.arraycopy(tempABlock, 0, ABlock, 0, blockSize * blockSize);

            RowComm.Bcast(ABlock, 0, blockSize * blockSize, MPI.INT, pivot);


            for (int i = 0; i < blockSize; i++)
                for (int j = 0; j < blockSize; j++)
                    for (int k = 0; k < blockSize; k++)
                        CBlock[i * blockSize + j] += ABlock[i * blockSize + k] * BBlock[k * blockSize + j];

            int nextProc = gridCoords[0] + 1;
            if (gridCoords[0] == gridSize - 1)
                nextProc = 0;
            int prevProc = gridCoords[0] - 1;
            if (gridCoords[0] == 0)
                prevProc = gridSize - 1;

            ColComm.Sendrecv_replace(BBlock, 0, blockSize * blockSize, MPI.INT, nextProc, 0, prevProc, 0);
        }

        int[] resultRow = new int[matrixSize * blockSize];
        for (int i = 0; i < blockSize; i++) {
            int[] subRow = Arrays.copyOfRange(CBlock, i * blockSize, CBlock.length);
            int[] subRowRes = new int[gridSize * blockSize];

            RowComm.Gather(subRow, 0, blockSize, MPI.INT, subRowRes, 0, blockSize, MPI.INT, 0);
            if (gridSize * blockSize >= 0)
                System.arraycopy(subRowRes, 0, resultRow, i * matrixSize, gridSize * blockSize);
        }

        if (gridCoords[1] == 0)
            ColComm.Gather(resultRow, 0, blockSize * matrixSize, MPI.INT, matrixC, 0, blockSize * matrixSize, MPI.INT, 0);

        if (procRank == 0) {
            TimeLogger.log("F", matrixSize, MPI.COMM_WORLD.Size(), System.currentTimeMillis() - startTime);
        }
        MPI.Finalize();
    }
}