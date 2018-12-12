package day12

import readFileIntoLines

fun main(args: Array<String>) {
    val (initial, rules) = parseInput("/day12/input-small.txt")

    var pots = initial.toCharArray().map { it.toString() }.toMutableList()
    // add a few empty pots to the left & right
    repeat(4) {
        pots.add(0, ".")
        pots.add(".")
    }
    //println("pots = $pots")

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

        // add some pots to the end?
        if (nextGen.lastIndexOf("#") + 4 > nextGen.size) {
            repeat(3) { nextGen.add(".") }
        }

        pots = nextGen
        println("$gen:\t${pots.joinToString(separator = "")}")

        val sum = pots.mapIndexed { idx, it -> if (it == ".") 0 else idx - zeroIndex }.sum()
        println("sum for gen $gen = $sum")
    }

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