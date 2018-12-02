package day02

import readEntireFile

fun main(args: Array<String>) {
    val lines: List<String> = readEntireFile("/day02/input.txt").trim().split("\n")
    var numDoubles = 0
    var numTriples = 0
    lines.forEach { line ->
        val countByLetter = line.toCharArray().toList().groupingBy { it }.eachCount()
        val hasDoubles = countByLetter.entries.any { entry -> entry.value == 2 }
        val hasTriples = countByLetter.entries.any { entry -> entry.value == 3 }
        numDoubles += if (hasDoubles) 1 else 0
        numTriples += if (hasTriples) 1 else 0
        println("Box ID is: $line HasDoubles: $hasDoubles HasTriples: $hasTriples " +
                "NumDoubles: $numDoubles NumTriples: $numTriples")
    }
    val checksum = numDoubles * numTriples
    println("Checksum: $checksum")
}