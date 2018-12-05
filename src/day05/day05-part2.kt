package day05

import readEntireFile

fun main(args: Array<String>) {
    val polymer = readEntireFile("/day05/input.txt").trim()
    //var polymer = "dabAcCaCBAcCcaDA"

    var polymers = mutableListOf<String>()

    val distinct = polymer.toCharArray().map { it.toLowerCase() }.distinct().sorted()
    println("distinct = $distinct")

    //var alphabet = CharArray(26) { (it + 97).toChar() }.joinToString("")
    distinct.forEach {
        println("Removing $it")
        polymers.add(fullyReactPolymer(polymer.replace(it.toString(), "", true)))
    }

    polymers.sortBy { it.length }

    var shortest = polymers[0]
    println("Reached end, shortest polymer is $shortest")
    println("With a length of ${shortest.length}")
}
