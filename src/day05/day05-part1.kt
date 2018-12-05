package day05

import readEntireFile

fun main(args: Array<String>) {
    var polymer = readEntireFile("/day05/input.txt").trim()
    //var polymer = "dabAcCaCBAcCcaDA"

    polymer = fullyReactPolymer(polymer)

    println("Reached end, final polymer is $polymer")
    println("With a length of ${polymer.length}")
}

fun fullyReactPolymer(polymer: String): String {
    var polymer1 = polymer
    var moreMatches = true

    while (moreMatches) {
        var foundMatch = false
        for (idx in 0 until polymer1.length - 1) {
            val first = polymer1[idx]
            val next = polymer1[idx + 1]
            if (first.toLowerCase() == next.toLowerCase()) {
                // found a match, remove it from the string
                //println("Found $first$next")

                // test for one upper & one lower
                if (((first.isLowerCase() && next.isUpperCase()) ||
                            first.isUpperCase() && next.isLowerCase())
                ) {
                    polymer1 = polymer1.substring(0, idx) + polymer1.substring(idx + 2)
                    //println("Polymer after removing:             $polymer1")
                    foundMatch = true
                    break
                }
            }
        }
        moreMatches = foundMatch
    }
    return polymer1
}