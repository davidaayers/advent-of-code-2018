package day10

import readFileIntoLines

fun main(args: Array<String>) {
    val coords = readFileIntoLines("/day10/input.txt").map { Coord.of(it) }

    var smallestMapWidth = Integer.MAX_VALUE

    while (true) {
        val sizes = renderStarMap(coords)
        if (sizes.first < smallestMapWidth) {
            smallestMapWidth = sizes.first
        } else {
            println("Found likely word:")
            println("--------------------------")
            coords.forEach { it.untick() }
            renderStarMap(coords, true)
            println("--------------------------")
            break
        }

        coords.forEach { it.tick() }
    }
}

fun renderStarMap(coords: List<Coord>, printIt: Boolean = false): Pair<Int, Int> {
    val maxX = coords.map { it.x }.max()!! + 1
    val maxY = coords.map { it.y }.max()!! + 1
    val minX = coords.map { it.x }.min()!!
    val minY = coords.map { it.y }.min()!!

    val ySize = maxY - minY
    val xSize = maxX - minX

    if (printIt) {
        for (y in 0 until ySize) {
            for (x in 0 until xSize) {
                val matching = coords.find {
                    val adjY = it.y - minY
                    val adjX = it.x - minX
                    adjX == x && adjY == y
                }
                if (matching != null) {
                    print("#")
                } else {
                    print(".")
                }
            }
            println("")
        }
    }

    return Pair(xSize, ySize)
}

data class Coord(var x: Int, var y: Int, var xv: Int, var yv: Int) {
    companion object {
        fun of(input: String): Coord {
            val (x, y, xv, yv) = """position=<(\s*-*\w+), (\s*-*\w+)> velocity=<(\s*-*\w+), (\s*-*\w+)>""".toRegex()
                .find(input)!!
                .destructured
            return Coord(x.parse(), y.parse(), xv.parse(), yv.parse())
        }
    }

    fun tick() {
        x += xv
        y += yv
    }

    fun untick() {
        x -= xv
        y -= yv
    }
}

fun String.parse(): Int {
    return this.trim().toInt()
}