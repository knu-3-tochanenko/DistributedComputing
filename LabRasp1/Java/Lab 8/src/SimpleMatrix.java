import mpi.MPI;

// Послідовний алгоритм
public class SimpleMatrix {
    public static void calculate(String[] args, int matSize) {
        /*
        Будь-яка прикладна MPI-програма починається з виклику функції ініціалізації MPI: функції MPI.Init. В результаті
        виконання цієї функції створюється група процесів, в яку поміщаються всі процеси-додатки, і створюється область
        зв'язку, що описується визначеним комунікатором MPI.COMM_WORLD. Ця область зв'язку об'єднує всі процеси-додатки.
        */
        MPI.Init(args);

        // Номер процесу
        int procRank = MPI.COMM_WORLD.Rank();

        var A = new Matrix(matSize);
        var B = new Matrix(matSize);
        var C = new Matrix(matSize);
        long startTime = 0L;

        // Початкове наповнення виконується процесом номер 0
        if (procRank == 0) {
            A.fillRandom(3);
            B.fillRandom(3);
            startTime = System.currentTimeMillis();
        }

        if (MPI.COMM_WORLD.Size() == 1) {
            // Перемноження
            for (int i = 0; i < A.getSize(); i++)
                for (int j = 0; j < B.getSize(); j++)
                    for (int k = 0; k < A.getSize(); k++)
                        C.add(i, j, A.get(i,k) * B.get(k,j));
        } else  {
            if (matSize % 3 != 0 || matSize % 8 != 0) {
                System.out.println("Matrix size should be dividable by 3 and 8");
                return;
            }
            if (procRank == 0) {
                System.out.println("Start");
                for (int i = 1; i < MPI.COMM_WORLD.Size(); i++) {
                    System.out.println("Try to send matrices to " + i);
                    MPI.COMM_WORLD.Send(A.getMatrix(), 0, matSize * matSize, MPI.INT, i, 0);
                    System.out.println("Send A to " + i);
                    MPI.COMM_WORLD.Send(B.getMatrix(), 0, matSize * matSize, MPI.INT, i, 1);
                    System.out.println("Send B to " + i);
                }

                Matrix res = new Matrix(matSize);

                for (int i = 1; i < MPI.COMM_WORLD.Size(); i++) {
                    System.out.println("Received matrices from " + i);
                    int[] fromMatrix = new int[matSize * matSize];
                    MPI.COMM_WORLD.Recv(fromMatrix, 0, 1, MPI.INT, i, 3);
                    Matrix from = new Matrix(fromMatrix);

                    int start = (procRank - 1) * matSize / (MPI.COMM_WORLD.Size() - 1);
                    int end = procRank * matSize * (MPI.COMM_WORLD.Size() - 1);

                    for (int k = start; k < end; k++)
                        for (int j = 0; j < matSize; j++) {
                            res.set(k, j, from.get(k, j));
                        }
                }
            } else {
                System.out.println("Started calculating process " + procRank);
                int[] fromA = new int[matSize * matSize];
                int[] fromB = new int[matSize * matSize];
                MPI.COMM_WORLD.Recv(fromA, 0, matSize * matSize, MPI.INT, 0, 1);
                MPI.COMM_WORLD.Recv(fromB, 0, matSize * matSize, MPI.INT, 0, 2);
                System.out.println("Process " + procRank + " received A & B");

                A.copyFromLine(fromA);
                B.copyFromLine(fromB);

                Matrix res = new Matrix(matSize);

                int start = (procRank - 1) * matSize / (MPI.COMM_WORLD.Size() - 1);
                int end = procRank * matSize * (MPI.COMM_WORLD.Size() - 1);

                for (int computed = start; computed < end; computed++) {
                    for (int i = 0; i < matSize; i++)
                        for (int j = 0; j < matSize; j++)
                            res.add(computed, i, A.get(computed, j) * B.get(j, i));
                }
                System.out.println("Process " + procRank + " computed Matrix");
                MPI.COMM_WORLD.Send(res.getMatrix(), 0, matSize * matSize, MPI.INT, 0, 3);
                System.out.println("Process " + procRank + " send result");
            }
        }

        // Повідомлення результатів
        if (procRank == 0) {
            System.out.print("1) " + matSize + " x " + matSize + ", ");
            System.out.println(System.currentTimeMillis() - startTime + " ms");
        }
        MPI.Finalize();
    }
}