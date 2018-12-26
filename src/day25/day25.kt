package day25

import readFileIntoLines

fun main(args: Array<String>) {
    val points = parseData("/day25/input-small1.txt")
    println("points = ${points}")
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