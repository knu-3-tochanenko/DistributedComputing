using System;
using System.Diagnostics.Tracing;
using MPI;

namespace Lab_8 {
    class MpiTestProgram {
        public static void DisplayRanks() {
            Intracommunicator comm = Communicator.world;
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

        public static void CircleMessaging() {
            Intracommunicator comm = Communicator.world;
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

        public static void Barrier() {
            Intracommunicator comm = Communicator.world;
            for (int i = 0; i < 5; i++) {
                Console.WriteLine("Iteration #" + i + " from process " + comm.Rank);
                comm.Barrier();
                if (comm.Rank == 0) {
                    Console.WriteLine("Barrier reached for " + i);
                }
            }
        }

        public static void GatheringData() {
            Intracommunicator comm = Communicator.world;
            String[] hostNames = comm.Gather(MPI.Environment.ProcessorName, 0);
            if (comm.Rank == 0) {
                Array.Sort(hostNames);
                foreach (String host in hostNames)
                    Console.WriteLine(host);
            }
        }

        public static void MessageSpreading() {
            Intracommunicator comm = Communicator.world;
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

        private static string RandomCommand() {
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

        public static void AllToAll() {
            Intracommunicator comm = Communicator.world;
            string[] data = new string[comm.Size];
            for (int destination = 0; destination < comm.Size; destination++)
                data[destination] = "From " + comm.Rank + " to " + destination;

            string[] results = comm.Alltoall(data);

            foreach (var str in results)
                Console.WriteLine(comm.Rank + ": " + str);
        }

        public static void ComputePi() {
            Intracommunicator comm = Communicator.world;
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