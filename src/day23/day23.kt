package day23

import readFileIntoLines

fun main(args: Array<String>) {
    val bots = parseInput("/day23/input.txt")

    val strongestBot = bots.maxBy { it.signalStrength }!!
    println("strongestBot = $strongestBot")

    val inRange = bots.filter { it.distanceFrom(strongestBot) <= strongestBot.signalStrength }.count()
    println("inRange: $inRange")
}

fun parseInput(fileName: String): List<Bot> {
    val lines = readFileIntoLines(fileName)
    val regex = """pos=<(-*\d*),(-*\d*),(-*\d*)>, r=(\d*)""".toRegex()

    return lines.map {
        val (x, y, z, ss) = regex.find(it)!!.destructured
        Bot(x.toInt(), y.toInt(), z.toInt(), ss.toInt())
    }.toList()
}

data class Bot(val x: Int, val y: Int, val z: Int, val signalStrength: Int) {
    fun distanceFrom(other: Bot): Int {
        return (Math.abs(other.x - this.x) + Math.abs(other.y - this.y) + Math.abs(other.z - this.z))
    }
}