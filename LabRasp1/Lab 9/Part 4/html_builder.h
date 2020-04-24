#pragma once

#include <string>
#include <fstream>
#include <vector>
#include <utility>

class HtmlBuilder
{
public:
	HtmlBuilder(std::string path);

	HtmlBuilder* createHtml();
	HtmlBuilder* addHead();
	HtmlBuilder* startBody();
	HtmlBuilder* createTable();
	HtmlBuilder* addResult(int matrixSize, double sequentialTime, std::vector<std::pair<double, double>> results);
	HtmlBuilder* finishTable();
	HtmlBuilder* finishBody();

	void finishHtml();

	std::ofstream* getHtml();

private:
	std::string path;
	std::ofstream html;

	void addTableHeading();
};

