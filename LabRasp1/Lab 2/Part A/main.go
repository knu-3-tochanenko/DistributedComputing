package main

import (
	"fmt"
	"math/rand"
	"time"
)

const (
	// N is number of Rows
	N int = 8
	// M is number of Columns
	M int = 10
)

func generateMatrix() [N][M]bool {
	r := rand.New(rand.NewSource(time.Now().UnixNano()))
	// this will
	matrix := [N][M]bool{}
	matrix[r.Intn(N)][r.Intn(M)] = true
	return matrix
}

func drawMatrix(matrix [N][M]bool) {
	for i := range matrix {
		for j := range matrix[i] {
			if matrix[i][j] {
				fmt.Print("X")
			} else {
				fmt.Print("-")
			}
		}
		fmt.Println()
	}
}

func main() {
	var matrix = generateMatrix()
	drawMatrix(matrix)
	fmt.Printf("END")
}
