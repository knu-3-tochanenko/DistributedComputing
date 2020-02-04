package main

import (
	"fmt"
	"math/rand"
	"time"
)

const N int = 8
const M int = 10

func generateMatrix() [N][M]int {
	r := rand.New(rand.NewSource(time.Now().UnixNano()))
	// this will
	matrix := [N][M]int{}
	matrix[r.Intn(N)][r.Intn(M)] = 1
	return matrix
}

func main() {
	var matrix = generateMatrix()
	fmt.Println(matrix)
	fmt.Printf("END")
}
