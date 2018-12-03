package day02

import readFileIntoLines

data class Result(val index: Int, val answer: String?)

fun main(args: Array<String>) {
    val lines: List<String> = readFileIntoLines("/day02/input.txt");
    var cnt = 0
    var foundAnswer = false

    lines.forEachIndexed { firstIdx, first ->
        lines.subList(firstIdx + 1, lines.size).forEach { second ->
            cnt++

            val (numDiffs, answer) = compareStrings(first, second)
            if (numDiffs == 1) {
                println("Found answer: $answer first: $first, second: $second after looking at $cnt possibilities")
                foundAnswer = true
            }
        }
    }

    if (!foundAnswer) println("Didn't find answer after looking at $cnt possibilities")
}

fun compareStrings(first: String, second: String): Result {
    var numDiff = 0
    var diffPos = -1
    for (idx in 0 until first.length) {
        if (first[idx] != second[idx]) {
            diffPos = idx
            numDiff++
            if (numDiff > 1) {
                return Result(numDiff, null)
            }
        }
    }
    return Result(numDiff, first.substring(0, diffPos) + first.substring(diffPos + 1))
}
