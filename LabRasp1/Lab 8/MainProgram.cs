using MPI;

namespace Lab_8 {
    public class MainProgram {
        static void Main(string[] args) {
            using (new MPI.Environment(ref args)) {
                MpiTestProgram.ComputePi();
            }
        }
    }
}