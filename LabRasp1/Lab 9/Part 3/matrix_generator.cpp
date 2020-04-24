#include "matrix_generator.h"

MatrixGenerator::MatrixGenerator(int size, double maxValue) :MatrixGenerator(size, size, maxValue)
{
}

MatrixGenerator::MatrixGenerator(int rowSize, int columnSize, double maxValue) : rowSize(rowSize), columnSize(columnSize), maxValue(maxValue)
{
}

double** MatrixGenerator::generate()
{
	double** matrix = new double* [rowSize];

	for (int i = 0; i < rowSize; i++) {
		matrix[i] = new double[columnSize];
		for (int j = 0; j < columnSize; j++) {
			std::random_device rd;
			std::mt19937 gen(rd());
			std::uniform_real_distribution<> unif(0.0, maxValue);
			matrix[i][j] = unif(gen);
		}
	}
	return matrix;
}
