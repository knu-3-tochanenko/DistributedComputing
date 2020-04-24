package main

import (
	"fmt"
	"math/rand"
	"os"
	"strconv"
	"sync"
	"time"
)

var (
	Black   = Color("\033[1;30m%s\033[0m")
	Red     = Color("\033[1;31m%s\033[0m")
	Green   = Color("\033[1;32m%s\033[0m")
	Yellow  = Color("\033[1;33m%s\033[0m")
	Purple  = Color("\033[1;34m%s\033[0m")
	Magenta = Color("\033[1;35m%s\033[0m")
	Teal    = Color("\033[1;36m%s\033[0m")
	White   = Color("\033[1;37m%s\033[0m")
)

func Color(colorString string) func(...interface{}) string {
	sprint := func(args ...interface{}) string {
		return fmt.Sprintf(colorString,
			fmt.Sprint(args...))
	}
	return sprint
}

func generateMatrix(size int, maxValue float32) [][]float32 {
	r := rand.New(rand.NewSource(time.Now().UnixNano()))
	matrix := make([][]float32, size)

	for i := range matrix {
		matrix[i] = make([]float32, size)
		for j := range matrix[i] {
			matrix[i][j] = maxValue * r.Float32()
		}
	}

	return matrix
}

func stripesMethod(A, B [][]float32, threads int) [][]float32 {
	C := make([][]float32, len(A))

	for i := range C {
		C[i] = make([]float32, len(A))
	}

	var waitgroup sync.WaitGroup

	var taskSize int = len(A) / threads

	for i := 0; i < threads; i++ {
		var rowStart, rowEnd, colStart, colEnd int

		waitgroup.Add(threads)

		for j := 0; j < threads; j++ {
			rowStart = j * taskSize
			if j == threads-1 {
				rowEnd = len(A)
			} else {
				rowEnd = (j + 1) * taskSize
			}

			colStart = ((i + j) % threads) * taskSize
			if colStart/taskSize == threads-1 {
				colEnd = len(A)
			} else {
				colEnd = colStart + taskSize
			}

			go compute(A, B, C, rowStart, rowEnd, colStart, colEnd, &waitgroup)
		}

		waitgroup.Wait()
	}

	return C
}

func compute(A, B, C [][]float32, rowStart, rowEnd, colStart, colEnd int, waitgroup *sync.WaitGroup) {
	for i := rowStart; i < rowEnd; i++ {
		for j := colStart; j < colEnd; j++ {
			C[i][j] = computeCell(A, B, i, j)
		}
	}
	waitgroup.Done()
}

func computeCell(A, B [][]float32, i, j int) float32 {
	var result float32
	result = 0.0
	for k := 0; k < len(B); k++ {
		result += A[i][k] * B[k][j]
	}

	return result
}

func calculate(size, threadsNumber int) float32 {
	A := generateMatrix(size, 100)
	B := generateMatrix(size, 100)

	startTime := time.Now().UnixNano()
	stripesMethod(A, B, threadsNumber)
	finishTime := time.Now().UnixNano()

	return (float32)(finishTime-startTime) / 1000000000
}

func startHTML(name string) *os.File {
	f, err := os.Create(name + ".html")
	if err != nil {
		fmt.Println(err)
		return nil
	}

	f.WriteString(`<html lang="en">
	<head>
		<title>Report - Part 2 (GO)</title>
		<style>
			body {
				font-family: Calibri;
			}

			table, th, td {
				border: 1px solid black;
				border-collapse: collapse;
				padding: 8px;
			}
		</style>
	</head>
	<body>
	<table>
    <tr>
        <th rowspan="3">Matrix size</th>
        <th rowspan="3">Sequential algorithm</th>
        <th colspan="4">Parallel algorithm</th>
    </tr>
    <tr>
        <th colspan="2">2 processes</th>
        <th colspan="2">4 processes</th>
    </tr>
    <tr>
        <th>Time</th>
        <th>Acceleration</th>
        <th>Time</th>
        <th>Acceleration</th>
	</tr>`)

	return f
}

func printRes(f *os.File, matrixSize int, sequentialTime float32, results []FloatPair) {
	resultString := "<tr>"
	resultString += "<td>" + strconv.Itoa(matrixSize) + "</td>"
	resultString += "<td>" + fmt.Sprintf("%f", sequentialTime) + "</td>"
	for i := range results {
		resultString += "<td>" + fmt.Sprintf("%f", results[i].first) + "</td>"
		resultString += "<td>" + fmt.Sprintf("%f", results[i].second) + "</td>"
	}
	resultString += "</tr>"
	_, err := f.WriteString(resultString)

	if err != nil {
		fmt.Println(err)
		f.Close()
		return
	}
}

func closeHTML(f *os.File) {
	_, err := f.WriteString("\n\t</body>\n</html>")

	if err != nil {
		fmt.Println(err)
		f.Close()
		return
	}

	err = f.Close()
	if err != nil {
		fmt.Println(err)
		return
	}
}

// FloatPair of a pair of float
type FloatPair struct {
	first, second float32
}

func test(name string) {
	sizes := []int{100, 500, 1000, 1500, 2000, 2500, 3000}
	var sequentialTime float32
	var time, acceleration float32

	f := startHTML(name)

	for i := range sizes {
		fmt.Println("Computing matrix size of", Yellow(sizes[i]), "...")
		results := make([]FloatPair, 2)
		sequentialTime = (calculate(sizes[i], 1))
		fmt.Println(Yellow("\t1 core\t"), sequentialTime)

		time = (calculate(sizes[i], 2))
		acceleration = sequentialTime / time
		results[0] = FloatPair{time, acceleration}
		fmt.Println(Yellow("\t2 cores\t"), time)

		time = (calculate(sizes[i], 4))
		acceleration = sequentialTime / time
		results[1] = FloatPair{time, acceleration}
		fmt.Println(Yellow("\t4 cores\t"), time, "\n")

		printRes(f, sizes[i], sequentialTime, results)
	}
	closeHTML(f)
}

func main() {
	test("index")
}
