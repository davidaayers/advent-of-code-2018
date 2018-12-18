package day17

import readFileIntoLines

// I was struggling a bit, so I looked to
// https://github.com/tginsberg/advent-2018-kotlin/blob/master/src/main/kotlin/com/ginsberg/advent2018/Day17.kt
// for some help. I tried not to just steal his code, but rather be inspired by it
fun main(args: Array<String>) {
    val map = parseInput("/day17/input.txt")

    flow(map.well, map)

    println("Final map:")
    println(map)

    var count = 0
    var countStanding = 0
    for (y in map.minY until map.maxY) {
        for (x in map.minX until map.maxX) {
            if (map.map[y][x] in "|~") count++
            if (map.map[y][x] == '~') countStanding++
        }
    }

    println("count = $count countStanding=$countStanding iters = $iters")
}

var iters = 0

// recursive flow
fun flow(from: Point, map: Map) {
    iters++

//    if (iters % 400 == 0) {
//        println("----- $iters -----")
//        println(map)
//        println("-------------------")
//    }

    Thread.sleep(1)

    // if we hit the bottom, bail out
    val down = from + d_down
    val left = from + d_left
    val right = from + d_right

    if (down !in map) {
        return
    }

    if (map.isEmpty(down)) {
        map.addFeature(down, '|')
        flow(down, map)
    }

    if ((map.isWall(down) || map.isRestingWater(down)) && map.isEmpty(left)) {
        map.addFeature(left, '|')
        flow(left, map)
    }

    if ((map.isWall(down) || map.isRestingWater(down)) && map.isEmpty(right)) {
        map.addFeature(right, '|')
        flow(right, map)
    }

    if ((map.isWall(down) || map.isRestingWater(down)) && map.wallsBothWaysFrom(from)) {
        // fill to the left and d_right
        var nextLeft = from
        while (!map.isWall(nextLeft)) {
            map.addFeature(nextLeft, '~')
            nextLeft += d_left
        }

        var nextRight = from
        while (!map.isWall(nextRight)) {
            map.addFeature(nextRight, '~')
            nextRight += d_right
        }
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

    val map = Map(minX, maxX + 2, minY, maxY)

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

    /*
    we're looking for a bowl shape
    #   #
    #####
    so, not only does there need to be walls on either side of points, but there
    has to be walls below it too
     */
    fun wallsBothWaysFrom(point: Point): Boolean {
        val y = point.y
        var leftX = point.x
        var rightX = point.x
        var rightWallFound = false

        var leftWallFound = false
        while (leftX > 0 && !leftWallFound) {
            val tile = map[y][leftX]
            val beneath = map[y + 1][leftX]
            if (beneath !in "#~") return false
            when (tile) {
                in "~.|" -> leftX--
                '#' -> {
                    leftWallFound = true
                }
            }
        }

        while (rightX < map[0].size && !rightWallFound) {
            val tile = map[y][rightX]
            val beneath = map[y + 1][rightX]
            if (beneath !in "#~") return false
            when (tile) {
                in "~.|" -> rightX++
                '#' -> {
                    rightWallFound = true
                }
            }
        }

        return leftWallFound && rightWallFound
    }
}

val d_down = Point(0, 1)
val d_right = Point(1, 0)
val d_left = Point(-1, 0)

data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }
}

data class Mud(val startX: Int, val endX: Int, val startY: Int, val endY: Int)