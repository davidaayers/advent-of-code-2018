package day05

import readEntireFile

fun main(args: Array<String>) {
    var polymer = readEntireFile("/day05/input.txt").trim()
    //var polymer = "dabAcCaCBAcCcaDA"

    var moreMatches = true

    while(moreMatches){
        var foundMatch = false
        for( idx in 0 until polymer.length-1) {
            val first = polymer[idx]
            val next = polymer[idx + 1]
            if(first.toLowerCase() == next.toLowerCase()) {
                // found a match, remove it from the string
                println("Found $first$next")

                // test for one upper & one lower
                if(((first.isLowerCase() && next.isUpperCase()) ||
                            first.isUpperCase() && next.isLowerCase())){
                    polymer = polymer.substring(0, idx ) + polymer.substring(idx+2)
                    println("Polymer after removing:             $polymer")
                    foundMatch = true
                    break
                }
            }
        }
        moreMatches = foundMatch
    }

    println("Reached end, final polymer is $polymer")
    println("With a length of ${polymer.length}")
}