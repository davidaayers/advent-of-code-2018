package day06

import readFileIntoLines

fun main(args: Array<String>) {
    val lines = readFileIntoLines("/day06/input.txt")
    var name = 'A'
    val points = lines.map {
        val pair = it.parseToPair()
        val namedPoint = NamedPoint(name++, pair)
        println("Assigned name: ${namedPoint.name}")
        namedPoint
    }

    println("points = $points")

    // find the greatest number, we'll use that to build our grid
    val max = points.flatMap { it.point.toList() }
        .toList()
        .max()!!

    println("max = $max")

    val grid = Array(max + 2) { Array(max + 2) { '.' } }

    // add all the points to the grid
    points.forEach {
        grid[it.point.second][it.point.first] = it.name
    }

    printGrid(grid)
    println("------")

    // now we need to go through every point on the grid and determine
    // which point is closest
    val edges = mutableSetOf<Char>()
    for (y in 0 until grid.size) {
        for (x in 0 until grid.size) {
            val distances = points.map {
                Distance(it, manhattanDistance(it.point.first, x, it.point.second, y))
            }.toList()
                .sortedBy { it.distance }

            when {
                distances[0].distance == 0 -> grid[y][x] = distances[0].namedPoint.name
                distances[0].distance == distances[1].distance -> grid[y][x] = '.'
                else -> grid[y][x] = distances[0].namedPoint.name
            }

            if (x == 0 || y == 0 || x == grid.size - 1 || y == grid.size - 1) {
                edges.add(grid[y][x])
            }
        }
    }

    printGrid(grid)

    // now find the greatest area (probably not right due to the infinite thing

    val greatestNonInfinity = grid.flatMap { it.asList() }
        .filter { it != '.' }
        .filter { !edges.contains(it) }
        .groupingBy { it }
        .eachCount()
        .maxBy { it.value }

    // now find that point in our list of points
    val winningPoint = points.firstOrNull { it.name == greatestNonInfinity!!.key }

    println("greatestNonInfinity = $greatestNonInfinity, winningPoint = $winningPoint")

}

fun manhattanDistance(x0: Int, x1: Int, y0: Int, y1: Int): Int {
    return Math.abs(x1 - x0) + Math.abs(y1 - y0)
}

fun printGrid(grid: Array<Array<Char>>) {
    grid.flatMap { it.asList() }.forEachIndexed { idx, c ->
        when {
            (idx + 1) % grid.size == 0 -> println("$c")
            else -> print("$c")
        }
    }
}

data class NamedPoint(var name: Char, var point: Pair<Int, Int>)
data class Distance(var namedPoint: NamedPoint, var distance: Int)

// extension function to parseToPair the line
fun String.parseToPair(): Pair<Int, Int> {
    val (x, y) = """(\d+), (\d+)""".toRegex()
        .find(this)!!
        .destructured
    return Pair(x.toInt(), y.toInt())
}