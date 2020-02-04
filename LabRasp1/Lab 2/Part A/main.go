package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

const (
	// N is number of Rows
	N int = 10
	// M is number of Columns
	M int = 10
	// BEEZ is number of BEEEEEEZ
	BEEZ = 5
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

func findBear(id int,
	waitGroup *sync.WaitGroup,
	sectors <-chan [M]bool,
	isBearFound chan bool) {

	defer waitGroup.Done()

	for sector := range sectors {
		select {

		case <-isBearFound:
			isBearFound <- true
			fmt.Println("...")
		default:
			fmt.Println("Beez group ", id, " is searching in sector", sector)

			for index, element := range sector {
				time.Sleep(time.Microsecond * 100)
				if element {
					fmt.Println("Beez group ", id, " found bear in sector ", sector, " in ", index,
						". The bear was punished!")
					isBearFound <- true
					return
				}
			}
		}
		fmt.Println("Beez group ", id, " returned")
	}

}

func main() {
	var matrix = generateMatrix()
	drawMatrix(matrix)
	sectors := make(chan [M]bool, M)
	isBearFound := make(chan bool, 1)

	var waitGroup sync.WaitGroup

	for i := 1; i < BEEZ; i++ {
		waitGroup.Add(1)
		go findBear(i, &waitGroup, sectors, isBearFound)
	}

	for i := 1; i < M; i++ {
		sectors <- matrix[i]
	}

	close(sectors)
	waitGroup.Wait()

	fmt.Printf("END")
}
