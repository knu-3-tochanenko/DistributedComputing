using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;

namespace Part_5 {
    public class Calculator {
        private List<int> sizes;
        private HtmlBuilder _builder;

        public Calculator(string path) {
            _builder = new HtmlBuilder(path);
            _builder.CreateHtml().AddHead().CreateTable();

            this.sizes = new List<int>();
        }

        public void AddTask(int size) {
            sizes.Add(size);
        }

        public void Run() {
            double sequentialTime;
            double time, acceleration;
            List<KeyValuePair<double, double>> results;

            foreach (var i in sizes) {
                results = new List<KeyValuePair<double, double>>();
                sequentialTime = Calculate(i, 1) / 1000.0;

                time = Calculate(i, 2) / 1000.0;
                acceleration = sequentialTime / time;
                results.Add(new KeyValuePair<double, double>(time, acceleration));

                time = Calculate(i, 4) / 1000.0;
                acceleration = sequentialTime / time;
                results.Add(new KeyValuePair<double, double>(time, acceleration));

                _builder.AddResult(i, sequentialTime, results);

                Console.WriteLine(i);
            }

            Finish();
        }

        private int Calculate(int size, int threadsNumber) {
            var matrixGenerator = new MatrixGenerator(size, 100);
            var A = matrixGenerator.Generate();
            var B = matrixGenerator.Generate();

            var stripesSchema = new StripesDivider(A, B, threadsNumber);
            var stopwatch = new Stopwatch();
            stopwatch.Start();

            var C = stripesSchema.CalculateProduct();

            stopwatch.Stop();

            return stopwatch.Elapsed.Milliseconds;
        }

        private void Finish() {
            _builder.FinishTable().Finish();
        }
    }
}