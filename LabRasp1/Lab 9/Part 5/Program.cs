using System;

namespace Part_5 {
    class Program {
        static void Main(string[] args) {
            var c = new Calculator("./resources/index.html");
            c.AddTask(100);
            c.AddTask(500);
            c.AddTask(1000);
            c.AddTask(1500);
            c.AddTask(2000);
            c.AddTask(2500);
            c.AddTask(3000);
            c.Run();
        }
    }
}