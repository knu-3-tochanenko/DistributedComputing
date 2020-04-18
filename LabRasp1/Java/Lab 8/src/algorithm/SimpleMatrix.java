package algorithm;

import logger.TimeLogger;
import mpi.MPI;

// Послідовний алгоритм
public class SimpleMatrix {
    public static void calculate(String[] args, int N) {
        int mc;
        int[] a = new int[N * N];
        int[] b = new int[N * N];
        int[] c = new int[N * N];
        long startTime = 0L;

        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        if (rank == 0) {
            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++) {
//a[i*N+j] = i*N+j;
                    a[i * N + j] = 1;
//b[i*N+j] = (i==j)?1:0;
                    b[i * N + j] = 1;
                }
            startTime = System.currentTimeMillis();

//            System.out.println("algorithm.Matrix A");
//            for (int i = 0; i < N; i++) {
//                for (int j = 0; j < N; j++)
//                    System.out.print(a[i * N + j] + " ");
//                System.out.println("");
//            }
//
//            System.out.println("algorithm.Matrix B");
//            for (int i = 0; i < N; i++) {
//                for (int j = 0; j < N; j++)
//                    System.out.print(b[i * N + j] + " ");
//                System.out.println("");
//            }
        }

        mc = N / size;
        int[] ai = new int[mc * N];
        int[] bi = new int[N * N];
        int[] ci = new int[mc * N];
//send matrix a from main process to other process
        if (rank == 0) {
            for (int i = 0; i < mc; i++)
                for (int j = 0; j < N; j++) {
                    ai[i * N + j] = a[i * N + j];
                }
            for (int i = 1; i < size; i++) {
                MPI.COMM_WORLD.Send(a, i * mc * N, mc * N, MPI.INT, i, i);
            }
        } else {
            MPI.COMM_WORLD.Recv(ai, 0, mc * N, MPI.INT, 0, rank);
        }
//send matrix b from main process to other process
        if (rank == 0) {
            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++) {
                    bi[i * N + j] = b[i * N + j];
                }
            for (int i = 1; i < size; i++) {
                MPI.COMM_WORLD.Send(b, 0, N * N, MPI.INT, i, i);
            }
        } else {
            MPI.COMM_WORLD.Recv(bi, 0, N * N, MPI.INT, 0, rank);
        }
//calculator
        for (int i = 0; i < mc; i++)
            for (int j = 0; j < N; j++) {
                ci[i * N + j] = 0;
                for (int k = 0; k < N; k++) {
                    ci[i * N + j] += ai[i * N + k] * bi[k * N + j];
                }
            }
//send result from other process to main process
        if (rank != 0) {
            MPI.COMM_WORLD.Send(ci, 0, mc * N, MPI.INT, 0, rank);
        } else {
            for (int i = 0; i < mc; i++)
                for (int j = 0; j < N; j++)
                    c[i * N + j] = ci[i * N + j];
            for (int i = 1; i < size; i++)
                MPI.COMM_WORLD.Recv(c, i * mc * N, mc * N, MPI.INT, i, i);
        }
//show result
        if (rank == 0) {



//            System.out.print("1) " + N + " x " + N + ", ");

            TimeLogger.log("B", N, MPI.COMM_WORLD.Size(), System.currentTimeMillis() - startTime);
//            System.out.println(System.currentTimeMillis() - startTime + " ms");
        }

        MPI.Finalize();
    }
}