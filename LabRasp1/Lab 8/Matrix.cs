using System;
using System.Linq;

namespace Lab_8 {
    [Serializable]
    public class Matrix {
        private int[,] matrix;

        private int size;

        public int GetSize() {
            return size;
        }

        public Matrix(int size) {
            this.size = size;
            matrix = new int[size, size];
        }

        public void Generate(int maxValue) {
            Random random = new Random((int) DateTime.Now.Ticks);
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++)
                    matrix[i, j] = random.Next(maxValue);
            }
        }

        public int this[int i, int j] {
            get => matrix[i, j];
            set => matrix[i, j] = value;
        }

        public void Print() {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++)
                    Console.Write(matrix[i, j] + " ");
                Console.WriteLine();
            }
        }

        public void Print(String name) {
            Console.WriteLine("Matrix " + name + ": ");
            Print();
            Console.WriteLine(String.Concat(Enumerable.Repeat("-", size)));
        }
    }
}