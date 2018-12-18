package day17

import readFileIntoLines

// I was struggling a bit, so I looked to
// https://github.com/tginsberg/advent-2018-kotlin/blob/master/src/main/kotlin/com/ginsberg/advent2018/Day17.kt
// for some help. I tried not to just steal his code, but rather be inspired by it
fun main(args: Array<String>) {
    val map = parseInput("/day17/input-small.txt")

    flow(map.well, map)
}

// recursive flow
fun flow(from: Point, map: Map) {

    println(map)
    println("-----")

    // if we hit the bottom, bail out
    val down = from + down
    if (down !in map) {
        return
    }

    if (map.isWall(down) || map.isRestingWater(down)) {
        // fill to the left and right
        var nextLeft = from + left
        while (map.isWall(nextLeft)) {
            map.addFeature(nextLeft,'~')
        }

    }

    if (map.isEmpty(down) || map.isWater(down)) {
        map.addFeature(down,'|')
        flow(down, map)
    }
}

fun parseInput(fileName: String): Map {
    val lines = readFileIntoLines(fileName)

    var minX = Integer.MAX_VALUE
    var maxX = Integer.MIN_VALUE
    var minY = Integer.MAX_VALUE
    var maxY = Integer.MIN_VALUE

    val mudLines = lines.map { line ->
        val parts = line.split(",").map { it.trim() }

        var startX = 0
        var endX = 0
        var startY = 0
        var endY = 0

        parts.forEach { part ->
            val coord = part.split("=")
            val axis = coord[0]
            val range = coord[1].split("..")

            val start = range[0].toInt()
            val end = if (range.size == 2) range[1].toInt() + 1 else range[0].toInt() + 1

            when (axis) {
                "x" -> {
                    startX = start
                    endX = end
                    if (startX < minX) minX = startX
                    if (endX > maxX) maxX = endX
                }
                "y" -> {
                    startY = start
                    endY = end
                    if (startY < minY) minY = startY
                    if (endY > maxY) maxY = endY
                }
            }
        }
        Mud(startX, endX, startY, endY)
    }

    val map = Map(minX, maxX + 1, minY, maxY)

    mudLines.forEach { mud ->
        for (y in mud.startY until mud.endY) {
            for (x in mud.startX until mud.endX) {
                map.map[y][x] = '#'
            }
        }
    }

    // add the well
    map.addWell(500, 0)

    return map
}

class Map(val minX: Int, val maxX: Int, val minY: Int, val maxY: Int) {
    var well = Point(0, 0)

    val map = Array(maxY) { CharArray(maxX) { '.' } }

    fun addWell(x: Int, y: Int) {
        map[y][x] = '+'
        well = Point(x, y)
    }

    fun addFeature(point: Point, feature: Char) {
        map[point.y][point.x] = feature
    }

    override fun toString(): String {
        val sb = StringBuffer()

        for (y in 0 until maxY) {
            for (x in minX - 1 until maxX) {
                sb.append(map[y][x])
            }
            sb.append("\n")
        }

        return sb.toString()
    }

    fun isWater(point: Point): Boolean {
        return map[point.y][point.x] == '|'
    }

    fun isEmpty(point: Point): Boolean {
        return map[point.y][point.x] == '.'
    }

    fun isWall(point: Point): Boolean {
        return map[point.y][point.x] == '#'
    }

    fun isRestingWater(point: Point): Boolean {
        return map[point.y][point.x] == '~'
    }

    operator fun contains(point: Point): Boolean {
        return point.x >= 0 && point.x < map[0].size && point.y >= 0 && point.y < map.size
    }
}

val up = Point(0, -1)
val down = Point(0, 1)
val right = Point(1, 0)
val left = Point(-1, 0)

data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }
}

data class Mud(val startX: Int, val endX: Int, val startY: Int, val endY: Int)