package day02

import readFileIntoLines

fun main(args: Array<String>) {
    val lines: List<String> = readFileIntoLines("/day02/input.txt");
    var cnt = 0
    var foundAnswer = false

    lines.forEachIndexed { firstIdx, first ->
        lines.subList(firstIdx, lines.size).forEach {
            cnt++

            var numDiff = 0
            var diffPos = -1
            for (idx in 0 until first.length) {
                if (first[idx] != it[idx]) {
                    diffPos = idx
                    numDiff++
                    if (numDiff > 1) {
                        return@forEach
                    }
                }
            }

            if (numDiff == 1) {
                val answer = first.substring(0, diffPos) + first.substring(diffPos + 1)
                println("Found answer: $answer first: $first, second: $it after looking at $cnt possibilities")
                foundAnswer = true
                return@forEachIndexed
            }
        }
    }

    if (!foundAnswer) println("Didn't find answer after looking at $cnt possibilities")
}