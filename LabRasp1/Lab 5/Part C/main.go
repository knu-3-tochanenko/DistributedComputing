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

var random = rand.New(rand.NewSource(time.Now().UnixNano()))

type Condition struct {
	array1 *[]int
	array2 *[]int
	array3 *[]int
}

type CyclicBarrier struct {
	generation  int
	count       int
	parties     int
	finishState chan bool
	trip        *sync.Cond
	condition   *Condition
}

func (b *CyclicBarrier) nextGeneration() {
	fmt.Println(Yellow("Barrier reached. Step #", b.generation))
	state := (*b.condition).check()
	for i := 0; i < b.parties; i++ {
		b.finishState <- state
	}
	b.trip.Broadcast()
	b.count = b.parties
	b.generation++
}

func (b *CyclicBarrier) Await() {
	b.trip.L.Lock()
	defer b.trip.L.Unlock()

	generation := b.generation

	b.count--
	index := b.count

	if index == 0 {
		b.nextGeneration()
	} else {
		for generation == b.generation {
			//wait for current generation complete
			b.trip.Wait()
		}
	}
}

func NewCyclicBarrier(num int, condition *Condition) *CyclicBarrier {
	b := CyclicBarrier{}
	b.count = num
	b.parties = num
	b.trip = sync.NewCond(&sync.Mutex{})
	b.finishState = make(chan bool, 3)
	b.condition = condition

	return &b
}

func NewCondition(array1, array2, array3 *[]int) *Condition {
	c := Condition{}
	c.array1 = array1
	c.array2 = array2
	c.array3 = array3

	return &c
}

func arrayModifier(id int, array *[]int, barrier *CyclicBarrier, waitGroup *sync.WaitGroup) {
	defer (*waitGroup).Done()
	for {
		element := random.Intn(len(*array))
		if random.Intn(2)%2 == 0 && (*array)[element] > 0 {
			(*array)[element]--
		} else if (*array)[element] < 10 {
			(*array)[element]++
		}
		fmt.Println("Thread #", Yellow(id), "modified array:", Yellow(*array), Green("Sum = ", getSum(array)))
		barrier.Await()
		finish := <-barrier.finishState
		if finish {
			fmt.Println(Red("Thread #", id, " finished"))
			break
		}
	}
}

func generateArray(size int) []int {
	array := make([]int, size)
	for i := range array {
		array[i] = random.Intn(10)
	}
	return array
}

func (b *Condition) check() bool {
	sum1 := getSum(b.array1)
	sum2 := getSum(b.array2)
	sum3 := getSum(b.array3)

	if sum1 == sum2 && sum2 == sum3 {
		return true
	}
	return false
}

func getSum(array *[]int) int {
	sum := 0
	for i := range *array {
		sum += (*array)[i]
	}
	return sum
}

func main() {
	waitGroup := sync.WaitGroup{}
	array1 := generateArray(5)
	fmt.Println("Array 1: ", Yellow(array1))
	array2 := generateArray(5)
	fmt.Println("Array 2: ", Yellow(array2))
	array3 := generateArray(5)
	fmt.Println("Array 3: ", Yellow(array3))

	condition := NewCondition(&array1, &array2, &array3)

	barrier := NewCyclicBarrier(3, condition)

	waitGroup.Add(3)
	go arrayModifier(1, &array1, barrier, &waitGroup)
	go arrayModifier(2, &array2, barrier, &waitGroup)
	go arrayModifier(3, &array3, barrier, &waitGroup)

	waitGroup.Wait()
}
