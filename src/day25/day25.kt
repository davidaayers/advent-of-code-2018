package day25

import readFileIntoLines

fun main(args: Array<String>) {
    if (findAllConstellations(parseData("/day25/input-small1.txt")).size != 2) {
        throw IllegalStateException()
    }
    if (findAllConstellations(parseData("/day25/input-small2.txt")).size != 4) {
        throw IllegalStateException()
    }
    val constellations = findAllConstellations(parseData("/day25/input-small3.txt"))
    if (constellations.size != 3) {
        throw IllegalStateException()
    }

}

private fun findAllConstellations(points: List<Point4d>): MutableList<Constellation> {
    val constellations = mutableListOf<Constellation>()

    points.forEach { point ->
        val existing = constellations.find { point in it }
        if (existing != null) {
            existing.points.add(point)
        } else {
            constellations.add(Constellation().apply { this.points.add(point) })
        }
    }
    return constellations
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

    // a point is "in" the constellation if at least one of the
    // other points is within manhattan distance of 3
    operator fun contains(point: Point4d): Boolean {
        return points.any { it.manhattanDistanceTo(point) <= 3 }
    }

}