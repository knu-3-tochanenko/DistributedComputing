#pragma once

#include <random>

class MatrixGenerator
{
public:
	MatrixGenerator(int size, double maxValue);
	MatrixGenerator(int rowSize, int columnSize, double maxValue);

	double** generate();
private:
	int rowSize;
	int columnSize;
	double maxValue;
};

