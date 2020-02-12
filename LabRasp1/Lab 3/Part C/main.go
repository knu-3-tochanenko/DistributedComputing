package main

import (
	"fmt"
	"math/rand"
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

const (
	// smokerCount - number of cmokers
	smokerCount = 3
)

func numToElement(num int) string {
	switch num {
	case 0:
		return "tabacco"
	case 1:
		return "paper"
	case 2:
		return "matches"
	default:
		return "wtf is that?"
	}
}

func smoker(
	component int,
	table *[]bool,
	readSemaphore,
	writeSemaphore chan bool,
	waitGroup *sync.WaitGroup) {
	defer waitGroup.Done()
	for {
		readSemaphore <- true
		fmt.Println(Teal("Checking table #", component),
			" ("+Yellow(numToElement(component))+" dealer)")

		if !(*table)[component] {
			fmt.Println(Red("Smoking #", component, " ..."))
			for i := range *table {
				if (*table)[i] {
					fmt.Println(Yellow(numToElement(i)) + Green(" is on the table!"))
				}
				(*table)[i] = false
			}
			writeSemaphore <- true
		} else {
			<-readSemaphore
			time.Sleep(time.Second)
		}
	}
}

func observer(table *[]bool, readSemaphore, writeSemaphore chan bool, waitGroup *sync.WaitGroup) {
	defer waitGroup.Done()
	for {
		<-writeSemaphore
		var first, second = getRandomCigaretteComponent()
		fmt.Println("Observer put items:", Yellow(numToElement(first)),
			"and", Yellow(numToElement(second)))
		(*table)[first] = true
		(*table)[second] = true
		<-readSemaphore
	}
}

func getRandomCigaretteComponent() (int, int) {
	r := rand.New(rand.NewSource(time.Now().UnixNano()))

	stuff1 := r.Intn(3)
	stuff2 := r.Intn(3)
	for stuff2 == stuff1 {
		stuff2 = r.Intn(3)
	}

	return stuff1, stuff2
}

func main() {
	var waitGroup sync.WaitGroup
	var table = make([]bool, smokerCount)

	var readSemaphore = make(chan bool)
	var writeSemaphore = make(chan bool, 1)

	writeSemaphore <- true
	waitGroup.Add(1)
	go observer(&table, readSemaphore, writeSemaphore, &waitGroup)

	for i := 0; i < smokerCount; i++ {
		waitGroup.Add(1)
		go smoker(i, &table, readSemaphore, writeSemaphore, &waitGroup)
	}
	waitGroup.Wait()
}
