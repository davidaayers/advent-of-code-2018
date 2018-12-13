package day13

import readFileIntoLines

val dirs = mapOf(
    '>' to Direction('>', 1, 0),
    '<' to Direction('<', -1, 0),
    'v' to Direction('v', 0, 1),
    '^' to Direction('^', 0, -1)
)

fun main(args: Array<String>) {
    val (map, carts) = parseInput("/day13/input-small.txt")
    println("carts = ${carts}")
}

fun parseInput(fileName: String): Pair<Array<CharArray>, MutableList<Cart>> {
    val lines = readFileIntoLines(fileName)
    val map = Array(lines.size) { CharArray(lines[0].length) { ' ' } }
    val carts = mutableListOf<Cart>()
    lines.forEachIndexed { idx, line ->
        map[idx] = line.toCharArray()

        // find any carts that are in this map line
        map[idx].forEachIndexed { lineIdx, c ->
            val direction = dirs.get(c)
            if (direction != null) {
                carts.add(Cart(lineIdx, idx, direction))
            }
        }
    }
    return Pair(map, carts)
}


data class Cart(var x: Int, var y: Int, var dir: Direction)
data class Direction(val char: Char, val dx: Int, val dy: Int)