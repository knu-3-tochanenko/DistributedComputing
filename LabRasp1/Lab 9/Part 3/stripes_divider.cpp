#include "stripes_divider.h"

StripesDivider::StripesDivider(double** A, double** B, int rows, int columns, int threadsNumber) :A(A), B(B), rows(rows), columns(columns), threadsNumber(threadsNumber)
{
	C = new double* [rows];
	for (int i = 0; i < rows; i++) {
		C[i] = new double[columns];
	}
}

double** StripesDivider::calculateProduct()
{
	int taskSize = rows / threadsNumber;
	tbb::task_scheduler_init init(threadsNumber);

	for (int i = 0; i < threadsNumber; i++) {
		tbb::parallel_for(0, threadsNumber, 1, [&](int j) {
			int rowStart, rowEnd, columnStart, columnEnd;

			rowStart = j * taskSize;
			rowEnd = (j == threadsNumber - 1) ? rows : (j + 1) * taskSize;

			columnStart = ((i + j) % threadsNumber) * taskSize;
			columnEnd = (columnStart / taskSize == threadsNumber - 1) ? rows : (columnStart + taskSize);

			compute(rowStart, rowEnd, columnStart, columnEnd, columns);
			}
		);
	}


	return C;
}

void StripesDivider::compute(int rowStart, int rowEnd, int columnStart, int columnEnd, int size)
{
	for (int i = rowStart; i < rowEnd; i++) {
		for (int j = columnStart; j < columnEnd; j++) {
			C[i][j] = calculateEntry(i, j, size);
		}
	}
}

double StripesDivider::calculateEntry(int i, int j, int size)
{
	double result = 0.0;
	for (int k = 0; k < size; k++) {
		result += A[i][k] * B[k][j];
	}
	return result;
}

