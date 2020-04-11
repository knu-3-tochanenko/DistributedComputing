using System;
using System.Diagnostics.Tracing;
using MPI;

namespace Lab_8 {
    class MpiTestProgram {
        public static void Test(string[] args) {
            using (new MPI.Environment(ref args)) {
                Intracommunicator comm = Communicator.world;
                // DisplayRanks(comm);
                // CircleMessaging(comm);
                // Barrier(comm);
                // GatheringData(comm);
                // MessageSpreading(comm);
                // AllToAll(comm);
                ComputePi(comm);
            }
        }

        static void DisplayRanks(Intracommunicator comm) {
            if (comm.Rank == 0) {
                comm.Send("<", 1, 0);

                String msg = comm.Receive<String>(Communicator.anySource, 0);
                Console.WriteLine("Rank : " + comm.Rank + ". Message : " + msg + ".");
            }
            else {
                String msg = comm.Receive<String>(comm.Rank - 1, 0);
                Console.WriteLine("Rank : " + comm.Rank + ". Message : " + msg + ".");
                comm.Send(msg + " - " + comm.Rank, (comm.Rank + 1) % comm.Size, 0);
            }
        }

        static void CircleMessaging(Intracommunicator comm) {
            if (comm.Rank == 0) {
                int[] dat = {1, 3, 2, 5};
                comm.Send(dat, 1, 0);
            }
            else if (comm.Rank == 1) {
                int[] dat = new int[10];
                comm.Receive(0, 0, ref dat);
                foreach (var t in dat)
                    Console.Write(t + " ");
            }
        }

        static void Barrier(Intracommunicator comm) {
            for (int i = 0; i < 5; i++) {
                Console.WriteLine("Iteration #" + i + " from process " + comm.Rank);
                comm.Barrier();
                if (comm.Rank == 0) {
                    Console.WriteLine("Barrier reached for " + i);
                }
            }
        }

        static void GatheringData(Intracommunicator comm) {
            String[] hostNames = comm.Gather(MPI.Environment.ProcessorName, 0);
            if (comm.Rank == 0) {
                Array.Sort(hostNames);
                foreach (String host in hostNames)
                    Console.WriteLine(host);
            }
        }

        static void MessageSpreading(Intracommunicator comm) {
            string command = "null";

            do {
                if (comm.Rank == 0) {
                    command = RandomCommand();
                    Console.WriteLine("ADDED COMMAND " + command);
                }

                comm.Broadcast(ref command, 0);

                if (command == "add" && comm.Rank == 1)
                    Console.WriteLine("Process 1  - add");
                else if (command == "rem" && comm.Rank == 2)
                    Console.WriteLine("Process 2 - rem");
                else if (command == "exit" && comm.Rank == 3)
                    Console.WriteLine("Process 3 - exit");
            } while (command != "exit");
        }

        static string RandomCommand() {
            Random random = new Random((int) DateTime.Now.Ticks);
            switch (random.Next(5)) {
                case 0:
                case 1:
                    return "add";
                case 2:
                case 3:
                    return "rem";
                case 4:
                    return "exit";
            }

            return "none";
        }

        static void AllToAll(Intracommunicator comm) {
            string[] data = new string[comm.Size];
            for (int destination = 0; destination < comm.Size; destination++)
                data[destination] = "From " + comm.Rank + " to " + destination;

            string[] results = comm.Alltoall(data);

            foreach (var str in results)
                Console.WriteLine(comm.Rank + ": " + str);
        }

        static void ComputePi(Intracommunicator comm) {
            int dartsPerProcessor = 10000;
            Random random = new Random((int) DateTime.Now.Ticks);

            int dartsInCircle = 0;
            double x, y;
            
            for (int i = 0; i < dartsPerProcessor; i++) {
                x = (random.NextDouble() - 0.5) * 2;
                y = (random.NextDouble() - 0.5) * 2;
                if (x * x + y * y <= 1.0)
                    ++dartsInCircle;
            }

            int totalDartsInCircle = comm.Reduce(dartsInCircle, Operation<int>.Add, 0);
            if (comm.Rank == 0) {
                Console.WriteLine("Pi is approximately {0:F15}",
                    4 * (double) totalDartsInCircle / (comm.Size * (double) dartsPerProcessor));
            }
        }
        
    }
}