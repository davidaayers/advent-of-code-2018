package day12

import readFileIntoLines

fun main(args: Array<String>) {
    val (initial, rules) = parseInput("/day12/input.txt")

    var pots = initial.toCharArray().map { it.toString() }.toMutableList()
    // add a few empty pots to the left & right
    repeat(5) {
        pots.add(0, ".")
        pots.add(".")
    }
    println("pots = $pots")

    val zeroIndex = pots.indexOfFirst { it == "#" }
    val gens = 20

    println("0:\t${pots.joinToString(separator = "")}")
    for (gen in 1..gens) {
        val nextGen = mutableListOf<String>()
        nextGen.addAll(pots)
        for (pot in 2 until pots.size - 2) {
            val checkPot = (pot - 2..pot + 2).joinToString(separator = "") { pots[it] }
            nextGen[pot] = rules.getOrDefault(checkPot, ".")
        }
        // do we need to add some pots to the beginning or end?
        repeat(3) {
            nextGen.add(".")
        }
        //zeroIndex += 3
        pots = nextGen
        println("$gen:\t${pots.joinToString(separator = "")}")
    }

    // get the total of our pots
    val sum = pots.mapIndexed { idx, it -> if (it == ".") 0 else idx - zeroIndex }.sum()
    println("sum = $sum")

}

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