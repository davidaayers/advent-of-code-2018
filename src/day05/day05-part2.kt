package day05

import readEntireFile

fun main(args: Array<String>) {
    val polymer = readEntireFile("/day05/input.txt").trim()
    //var polymer = "dabAcCaCBAcCcaDA"

    val shortest = ('a'..'z').map { fullyReactPolymer(polymer.replace(it.toString(), "", true)) }
        .sortedBy { it.length }
        .first()

    println("Reached end, shortest polymer is $shortest")
    println("With a length of ${shortest.length}")
}
