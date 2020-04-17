namespace Lab_8 {
    public class SequentialAlgorithm {
        private Matrix A;
        private Matrix B;

        private readonly int size;
        
        private int startRow;
        private int endRow;

        public SequentialAlgorithm(Matrix A, Matrix B, int startRow, int endRow) {
            this.A = A;
            this.B = B;
            this.startRow = startRow;
            this.endRow = endRow;

            size = A.GetSize();
        }

        public Matrix multiply() {
            Matrix res = new Matrix(size);
            for (int computed = startRow; computed < endRow; computed++) {
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++)
                        res[computed, i] += A[computed, j] * B[j, i];
                }
            }

            return res;
        }
    }
}