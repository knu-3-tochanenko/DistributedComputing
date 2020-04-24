package main

import (
	"math/rand"
	"sync"
	"time"
)

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

func main() {

}
