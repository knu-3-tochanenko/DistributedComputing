#pragma once

#include "matrix_generator.h"
#include "stripes_divider.h"
#include "html_builder.h"
#include <vector>
#include <string>
#include <utility>
#include <iostream>
#include <chrono>

class Calculator
{
public:
	Calculator(std::string path);

	void addTask(int size);
	void run();
private:
	std::vector<int> sizes;
	HtmlBuilder* builder;

	double calculate(int size, int threadsNumber);
	void finish();
};

