package day12

import readFileIntoLines

fun main(args: Array<String>) {
    val (initial, rules) = parseInput("/day12/input-small.txt")

    val pots = initial.toCharArray().map { it.toString() }.toMutableList()
    // add a few empty pots to the left & right
    repeat(4) {
        pots.add(0,".")
        pots.add(".")
    }
    println("pots = $pots")

}

fun parseInput(fileName: String): Pair<String, MutableMap<String, String>> {
    val iterator = readFileIntoLines(fileName).iterator()

    val initial = iterator.next().substring(15)
    println("initial = [${initial}]")

    val rules = mutableMapOf<String, String>()
    iterator.forEachRemaining {
        if (it.trim().isNotEmpty()) {
            val matchResult = """(\W{4}) => (\W)""".toRegex().find(it)
            val (rule, result) = matchResult!!.destructured
            println("line = $it")
            println("rule = ${rule} result = $result")
            if ("#".equals(result)) {
                rules[rule] = result
            }
        }
    }

    return Pair(initial, rules)
}