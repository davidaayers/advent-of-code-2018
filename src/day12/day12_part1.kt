package day12

import readFileIntoLines

fun main(args: Array<String>) {
    val (initial, rules) = parseInput("/day12/input.txt")

    var pots = initial.toCharArray().map { it.toString() }.toMutableList()
    // add a few empty pots to the left & right
    repeat(4) {
        pots.add(0, ".")
        pots.add(".")
    }
    //println("pots = $pots")

    val zeroIndex = pots.indexOfFirst { it == "#" }
    val gens = 50000000000

    var lastDiffs = mutableListOf<Int>()

    println("0:\t${pots.joinToString(separator = "")}")
    var prevGenSum = sum(pots, zeroIndex)
    for (gen in 1..gens) {
        val nextGen = mutableListOf<String>()
        nextGen.addAll(pots)
        for (pot in 2 until pots.size - 2) {
            val checkPot = (pot - 2..pot + 2).joinToString(separator = "") { pots[it] }
            nextGen[pot] = rules.getOrDefault(checkPot, ".")
        }

        // add some pots to the end?
        if (nextGen.lastIndexOf("#") + 4 > nextGen.size) {
            repeat(3) { nextGen.add(".") }
        }

        pots = nextGen
        //println("$gen:\t${pots.joinToString(separator = "")}")

        val sum = sum(pots, zeroIndex)
        var diff = sum - prevGenSum
        prevGenSum = sum
        println("sum for gen $gen = $sum, diff = $diff")

        if (gen % 20.toLong() == 0.toLong()) {
            println("Answer to part1 = $sum")
        }

        lastDiffs.add(0, diff)
        if (lastDiffs.size > 10) {
            lastDiffs = lastDiffs.subList(0, 10)

            if (lastDiffs.distinct().toList().size == 1) {
                var finalAnswer = sum + ((gens - gen) * diff)
                println("list hasn't changed in 10 iterations, achieved stability...answer to part 2= $finalAnswer")
                break
            }
        }

    }

}

private fun sum(pots: MutableList<String>, zeroIndex: Int) =
    pots.mapIndexed { idx, it -> if (it == ".") 0 else idx - zeroIndex }.sum()

fun parseInput(fileName: String): Pair<String, MutableMap<String, String>> {
    val iterator = readFileIntoLines(fileName).iterator()

    val initial = iterator.next().substring(15)

    val rules = mutableMapOf<String, String>()
    iterator.forEachRemaining {
        if (it.trim().isNotEmpty()) {
            val matchResult = """(\W{5}) => (\W)""".toRegex().find(it)
            val (rule, result) = matchResult!!.destructured
            if ("#" == result) {
                rules[rule] = result
            }
        }
    }

    return Pair(initial, rules)
}