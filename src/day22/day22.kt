package day22

import shared.AStarNode
import shared.AStarSearch
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

val climbingGear = Tool("Climbing Gear", "$rocky$wet")
val torch = Tool("Torch", "$rocky$narrow")
val nothing = Tool("Nothing", "$wet$narrow")

val tools = listOf(
    climbingGear,
    torch,
    nothing
)

fun main(args: Array<String>) {
//    val testCase = calcTotalRisk(510, Point(10, 10))
//    println("testCase = ${testCase}")
//
//    val puzzleAnswer = calcTotalRisk(9171, Point(7, 721))
//    println("Part 1 answer is $puzzleAnswer")

    findPath(510, Point(10, 10))
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

fun findPath(depth: Int, target: Point) {
    val cave = generateCave(depth, target)


    val start = CaveNode(Point(0, 0), cave, torch)
    val end = CaveNode(target, cave, null)

    val search = AStarSearch<Point>()

    val path = search.findPath(start, end).map { it.obj }

    println("path = $path")

    println("$cave")

    path.forEach {
        cave.addFeature(it, 'X')
    }

    println("$cave")
}

fun generateCave(depth: Int, target: Point): Cave {

    val width = target.x * 2
    val height = target.y * 2

    val erosionLevels = Array(height) { IntArray(width) { 0 } }

    val cave = Cave(width, height)
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

class CaveNode(val point: Point, val cave: Cave, val tool: Tool?) : AStarNode<Point>(point) {
    override fun neighbors(): List<AStarNode<Point>> {

        val neighbors = mutableListOf<AStarNode<Point>>()

        point.cardinalDirs().filter { it in cave }.forEach { p ->
            tools.forEach { tool ->
                val region = cave.feature(p)
                if (tool.validRegions.contains(region)) {
                    val neighbor = CaveNode(p, cave, tool)
                    neighbors.add(neighbor)
                }
            }
        }

        return neighbors
    }

    override fun costToNeighbor(neighbor: AStarNode<Point>): Float {
        neighbor as CaveNode
        return if (this.tool == neighbor.tool) 1F else 7F
    }

    override fun estimatedCostToGoal(other: AStarNode<Point>): Float {
        return obj.manhattanDistanceTo(other.obj)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CaveNode

        if (point != other.point) return false

        return true
    }

    override fun hashCode(): Int {
        return 31 * point.hashCode()
    }
}

data class Tool(val name: String, val validRegions: String)