#include "html_builder.h"

HtmlBuilder::HtmlBuilder(std::string path) :path(path) {
}

HtmlBuilder* HtmlBuilder::createHtml()
{
	html.open(path);
	html << "<html xmlns=\"http://www.w3.org/1999/xhtml \">" << std::endl;

	return this;
}

HtmlBuilder* HtmlBuilder::addHead()
{
	html << "<head>" << std::endl;
	html << "<title>Report - Part 4 (OpenMP)</title>" << std::endl;
	html << "<link rel=\"stylesheet\" href=\"style.css\">" << std::endl;
	html << "</head>" << std::endl;
	return this;
}

HtmlBuilder* HtmlBuilder::startBody()
{
	html << "<body>" << std::endl;
	return this;
}

HtmlBuilder* HtmlBuilder::createTable()
{
	html << "<table>" << std::endl;
	addTableHeading();
	return nullptr;
}

HtmlBuilder* HtmlBuilder::addResult(int matrixSize, double sequentialTime, std::vector<std::pair<double, double>> results)
{
	html << "<tr>" << std::endl;
	html << "<td>" << matrixSize << "</td>" << std::endl;
	html << "<td>" << sequentialTime << "</td>" << std::endl;

	for (auto i : results) {
		html << "<td>" << i.first << "</td>" << std::endl;
		html << "<td>" << i.second << "</td>" << std::endl;
	}

	html << "</tr>" << std::endl;

	return this;
}

HtmlBuilder* HtmlBuilder::finishTable()
{
	html << "</table>" << std::endl;
	return nullptr;
}

HtmlBuilder* HtmlBuilder::finishBody()
{
	html << "</body>" << std::endl;
	return this;
}

void HtmlBuilder::finishHtml()
{
	html << "</html>" << std::endl;
	html.close();
}

std::ofstream* HtmlBuilder::getHtml()
{
	return &html;
}

void HtmlBuilder::addTableHeading()
{
	html << "<tr>" << std::endl;
	html << "<th rowspan=\"3\">Matrix size</th>" << std::endl;
	html << "<th rowspan=\"3\">Sequential algorithm</th>" << std::endl;
	html << "<th colspan=\"4\">Parallel algorithm</th>" << std::endl;
	html << "</tr>" << std::endl;

	html << "<tr>" << std::endl;
	html << "<th colspan=\"2\">2 processes</th>" << std::endl;
	html << "<th colspan=\"2\">4 processes</th>" << std::endl;
	html << "</tr>" << std::endl;

	html << "<tr>" << std::endl;
	for (int i = 0; i < 2; i++) {
		html << "<th>Time</th>" << std::endl;
		html << "<th>Acceleration</th>" << std::endl;
	}
	html << "</tr>" << std::endl;

}
