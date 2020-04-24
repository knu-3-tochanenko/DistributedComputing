#include "calculator.h"

Calculator::Calculator(std::string path)
{
	builder = new HtmlBuilder(path);
	builder->createHtml()->addHead()->startBody()->createTable();

	sizes = std::vector<int>();
}

void Calculator::addTask(int size)
{
	sizes.push_back(size);
}

void Calculator::run()
{
	double sequentialTime;
	double time, acceleration;

	for (auto i : sizes) {
		std::cout << "Calculating matrix size of " << i << " ..." << std::endl;
		std::vector<std::pair<double, double>> results = std::vector<std::pair<double, double>>();
		sequentialTime = calculate(i, 1);
		std::cout << "\t1 core\t" << sequentialTime << std::endl;

		time = calculate(i, 2);
		acceleration = sequentialTime / time;
		results.push_back(std::pair<double, double>(time, acceleration));
		std::cout << "\t2 cores\t" << time << std::endl;

		time = calculate(i, 4);
		acceleration = sequentialTime / time;
		results.push_back(std::pair<double, double>(time, acceleration));
		std::cout << "\t4 cores\t" << time << std::endl;

		builder->addResult(i, sequentialTime, results);
	}
}

double Calculator::calculate(int size, int threadsNumber)
{
	auto matrixGenerator = MatrixGenerator(size, 100.0);
	auto A = matrixGenerator.generate();
	auto B = matrixGenerator.generate();

	auto stripesShema = StripesDivider(A, B, size, size, threadsNumber);

	auto start = std::chrono::steady_clock::now();

	auto C = stripesShema.calculateProduct();

	auto end = std::chrono::steady_clock::now();
	std::chrono::duration<double> elapsedSeconds = end - start;

	return elapsedSeconds.count();
}

void Calculator::finish()
{
	builder->finishTable()->finishBody()->finishHtml();
}
