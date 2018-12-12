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

    val zeroIndex = pots.indexOfFirst { it == "#" }
    val gens = 20

    var lastDiffs = mutableListOf<Int>()

    println("0:\t${pots.joinToString(separator = "")}")
    var prevGenSum = sum(pots, zeroIndex)
    for (gen in 1..gens) {
        pots = pots.mapIndexed { idx, _ ->
            rules.getOrDefault(pots.surroundingPots(idx), ".")
        }.toMutableList()

        // add some pots to the end?
        if (pots.lastIndexOf("#") + 4 > pots.size) {
            repeat(3) { pots.add(".") }
        }

        //println("$gen:\t${pots.joinToString(separator = "")}")

        val sum = sum(pots, zeroIndex)
        val diff = sum - prevGenSum
        prevGenSum = sum

        if (gen % 20.toLong() == 0.toLong()) {
            println("Answer to part1 = $sum")
        }

        lastDiffs.add(0, diff)
        if (lastDiffs.size > 10) {
            lastDiffs = lastDiffs.subList(0, 10)

            if (lastDiffs.distinct().toList().size == 1) {
                val finalAnswer = sum + ((gens - gen) * diff)
                println("list hasn't changed in 10 iterations, achieved stability...answer to part 2= $finalAnswer")
                break
            }
        }

    }

}

private fun List<String>.surroundingPots(idx: Int): String {
    val start = if (idx - 2 < 0) 0 else idx - 2
    val end = if (idx + 2 > this.size - 1) this.size - 1 else idx + 2
    return (start..end).joinToString(separator = "") { this[it] }
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