package day02

import readEntireFile

fun main(args: Array<String>) {
    val lines: List<String> = readEntireFile("/day02/input.txt").trim().split("\n")
    var cnt = 0
    var foundAnswer = false
    outerloop@ for (first in lines) {
        innerloop@ for (second in lines) {
            if (first == second) continue
            cnt++

            var numDiff = 0
            var diffPos = -1
            for (idx in 0 until first.length) {
                if (first[idx] != second[idx]) {
                    diffPos = idx
                    numDiff++
                    if (numDiff > 1) {
                        continue@innerloop
                    }
                }
            }

            val answer = first.substring(0,diffPos) + first.substring(diffPos+1)
            println("Found answer: $answer first: $first, second: $second after looking at $cnt possibilities")
            foundAnswer = true
            break@outerloop
        }
    }

    if (!foundAnswer) println("Didn't find answer after looking at $cnt possibilities")
}