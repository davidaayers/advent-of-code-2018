package day06

import readFileIntoLines

fun main(args: Array<String>) {
    val lines = readFileIntoLines("/day06/input-small.txt")
    var name = 'A'
    val points = lines.map {
        val pair = it.parseToPair()
        NamedPoint(name++, pair)
    }

    println("points = ${points}")

    // find the greatest number, we'll use that to build our grid
    val max = points.flatMap { it.point.toList() }
        .toList()
        .max() ?: throw IllegalStateException()

    println("max = $max")

    val grid = Array(max + 2) { Array(max + 2) { '.' } }

    // add all the points to the grid
    points.forEach {
        grid[it.point.first][it.point.second] = it.name
    }

    printGrid(grid)
}

fun printGrid(grid: Array<Array<Char>>) {
    for (x in 0 until grid.size) {
        for (y in 0 until grid.size) {
            print("${grid[x][y]}")
        }
        println("")
    }
}

data class NamedPoint(var name: Char, var point: Pair<Int, Int>)

// extension function to parseToPair the line
fun String.parseToPair(): Pair<Int, Int> {
    val (x, y) = """(\d+), (\d+)""".toRegex()
        .find(this)!!
        .destructured
    return Pair(x.toInt(), y.toInt())
}