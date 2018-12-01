package day01

import parseInt
import readEntireFile

fun main(args: Array<String>) {
    val lines: List<String> = readEntireFile("/day01/input.txt").trim().split("\n")

    // we need to go through the list N times until we have found a winner
    var foundWinner = false
    val counts = mutableMapOf<Int, Int>()
    var frequency = 0
    var timesThrough = 0

    while(!foundWinner){
        timesThrough++
        for (it in lines) {
            frequency += it.parseInt()
            println("Frequency Change: $it Frequency: $frequency")
            counts.merge(frequency, 1, Int::plus)
            if(counts[frequency] == 2){
                foundWinner = true
                println("Winner: $frequency; Times through list: $timesThrough")
                break
            }
        }
    }

}