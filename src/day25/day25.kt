package day25

import readFileIntoLines

fun main(args: Array<String>) {
    if (find(parseData("/day25/input-small1.txt")).size != 2) {
        throw IllegalStateException()
    }
    if (find(parseData("/day25/input-small2.txt")).size != 4) {
        throw IllegalStateException()
    }
    if (find(parseData("/day25/input-small3.txt")).size != 3) {
        throw IllegalStateException()
    }

    val answer = find(parseData("/day25/input.txt")).size
    println("answer = ${answer}")

}

private fun find(points: List<Point4d>): MutableList<Constellation> {
    return findConstellations(findNeighbors(points))
}

private fun findConstellations(neighbors: MutableMap<Point4d, List<Point4d>>): MutableList<Constellation> {
    val allPoints = neighbors.keys.toMutableList()
    val allConstellations = mutableListOf<Constellation>()
    while (allPoints.isNotEmpty()) {
        val currentPoint = allPoints.removeAt(0)
        val con = Constellation().apply { this.points.add(currentPoint) }
        val exploring = ArrayList<Point4d>(neighbors.getValue(currentPoint))
        while (exploring.isNotEmpty()) {
            val nextNeighbor = exploring.removeAt(0)
            allPoints.remove(nextNeighbor)
            con.points.add(nextNeighbor)
            exploring.addAll(neighbors.getValue(nextNeighbor).filter { it !in con })
        }
        allConstellations.add(con)
    }

    return allConstellations
}

private fun findNeighbors(points: List<Point4d>): MutableMap<Point4d, List<Point4d>> {
    val neighbors = mutableMapOf<Point4d, List<Point4d>>()
    points.forEach { point ->
        neighbors[point] = points
            .filter { it != point }
            .filter { it.manhattanDistanceTo(point) <= 3 }
            .toList()
    }
    return neighbors
}

fun parseData(fileName: String): List<Point4d> {
    val lines = readFileIntoLines(fileName)
    return lines.map {
        val parts = it.split(",")
        Point4d(parts[0].trim().toInt(), parts[1].trim().toInt(), parts[2].trim().toInt(), parts[3].trim().toInt())
    }
}

data class Point4d(val x: Int, val y: Int, val z: Int, val t: Int) {
    fun manhattanDistanceTo(other: Point4d): Int {
        return (Math.abs(other.x - this.x) +
                Math.abs(other.y - this.y) +
                Math.abs(other.z - this.z) +
                Math.abs(other.t - this.t))
    }
}

class Constellation {
    val points = mutableListOf<Point4d>()
    operator fun contains(point: Point4d): Boolean {
        return points.contains(point)
    }
}