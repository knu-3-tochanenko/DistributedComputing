using System;
using MPI;

namespace Lab_8 {
    public static class MainProgram {
        private static int size = 800;

        static void Main(string[] args) {

            Test1();

        }

        private static void Test1() {
            Matrix a = new Matrix(size);
            a.Generate(10);
            
            Matrix b = new Matrix(size);
            b.Generate(10);
            
            long start = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds();

            Matrix res = new SequentialAlgorithm(a, b, 0, size).multiply();
            
            Console.WriteLine("TIME : " + (DateTimeOffset.UtcNow.ToUnixTimeMilliseconds() - start));
        }

        static void Test2(string[] args) {
            using (new MPI.Environment(ref args)) {
                Intracommunicator comm = Communicator.world;

                if (comm.Rank == 0) {
                    Matrix a = new Matrix(size);
                    a.Generate(10);
                    // a.Print("a");

                    Matrix b = new Matrix(size);
                    b.Generate(10);
                    // b.Print("b");

                    long time = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds();
                    
                    comm.Send(time, 3, 2);

                    comm.Send(a, 1, 0);
                    comm.Send(b, 1, 1);

                    comm.Send(a, 2, 0);
                    comm.Send(b, 2, 1);
                }
                else if (comm.Rank == 3) {
                    Matrix res1 = comm.Receive<Matrix>(1, 0);
                    Matrix res2 = comm.Receive<Matrix>(2, 1);

                    long start = comm.Receive<long>(0, 2);

                    for (int i = size / 2; i < size; i++) {
                        for (int j = 0; j < size; j++)
                            res1[i, j] = res2[i, j];
                    }
                    
                    Console.WriteLine("TIME : " + (DateTimeOffset.UtcNow.ToUnixTimeMilliseconds() - start));

                    // res1.Print("Total result");
                }
                else if (comm.Rank == 1) {
                    Matrix a = comm.Receive<Matrix>(0, 0);
                    Matrix b = comm.Receive<Matrix>(0, 1);

                    Matrix res = new SequentialAlgorithm(a, b, 0, size / 2).multiply();

                    comm.Send(res, 3, 0);
                }
                else if (comm.Rank == 2) {
                    Matrix a = comm.Receive<Matrix>(0, 0);
                    Matrix b = comm.Receive<Matrix>(0, 1);

                    Matrix res = new SequentialAlgorithm(a, b, size / 2, size).multiply();

                    comm.Send(res, 3, 1);
                }
            }
        }
    }
}