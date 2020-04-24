#include "calculator.h"
#include <iostream>

int main()
{
	auto calculator = new Calculator("index.html");

	calculator->addTask(100);
	calculator->addTask(500);
	calculator->addTask(1000);
	calculator->addTask(1500);
	calculator->addTask(2000);
	calculator->addTask(2500);
	calculator->addTask(3000);

	calculator->run();
}