#pragma once

#include <omp.h>
#include <iostream>

class StripesDivider
{
public:
	StripesDivider(double** A, double** B, int rows, int columns, int threadsNumber);
	double** calculateProduct();

private:
	double** A;
	double** B;

	double** C;

	int rows;
	int columns;

	int threadsNumber;

	void compute(int rowStart, int rowEnd, int columnStart, int columnEnd, int size);
	double calculateEntry(int i, int j, int size);
};

