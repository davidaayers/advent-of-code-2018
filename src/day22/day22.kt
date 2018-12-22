package day22

import shared.BaseMap
import shared.Point

const val rocky = '.'
const val wet = '='
const val narrow = '|'

val risks = mapOf(
    rocky to 0,
    wet to 1,
    narrow to 2
)

fun main(args: Array<String>) {
    val testCase = calcTotalRisk(510, Point(10, 10))
    println("testCase = ${testCase}")

    val puzzleAnswer = calcTotalRisk(9171, Point(7, 721))
    println("Part 1 answer is $puzzleAnswer")
}

fun calcTotalRisk(depth: Int, target: Point): Int {
    val cave = generateCave(depth, target)
    println("$cave")

    var totalRisk = 0

    for (y in 0..target.y) {
        for (x in 0..target.x) {
            totalRisk += risks[cave.map[y][x]]!!
        }
    }

    return totalRisk
}

fun generateCave(depth: Int, target: Point): Cave {

    val erosionLevels = Array(target.y + 1) { IntArray(target.x + 1) { 0 } }

    val cave = Cave(target.x + 1, target.y + 1)
    erosionLevels.forEachIndexed { y, line ->
        line.forEachIndexed { x, _ ->
            val geologicalIndex = when {
                x == 0 && y == 0 -> 0
                x == target.x && y == target.y -> 0
                y == 0 -> x * 16807
                x == 0 -> y * 48271
                else -> {
                    val a = erosionLevels[y][x - 1]
                    val b = erosionLevels[y - 1][x]
                    a * b
                }
            }
            val erosionLevel = calcErosionLevel(geologicalIndex, depth)
            erosionLevels[y][x] = erosionLevel
            val type = when {
                erosionLevel % 3 == 0 -> rocky
                erosionLevel % 3 == 1 -> wet
                else -> narrow
            }
            cave.addFeature(x, y, type)
        }
    }

    return cave
}

private fun calcErosionLevel(geologicalIndex: Int, depth: Int): Int {
    return (geologicalIndex + depth) % 20183
}

class Cave(width: Int, height: Int) : BaseMap(width, height, '.') {
    override fun instantiateMap(width: Int, height: Int, bgChar: Char): BaseMap {
        return Cave(height, width)
    }
}