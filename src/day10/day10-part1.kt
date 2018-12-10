package day10

import readFileIntoLines

fun main(args: Array<String>) {
    val coords = readFileIntoLines("/day10/input.txt").map { Coord.of(it) }

    var smallestMapWidth = Integer.MAX_VALUE

    while(true) {
        val starMap = buildStarMap(coords)
        if(starMap.size < smallestMapWidth){
            smallestMapWidth = starMap.size
        } else {
            println("Found likely word")
            break
        }
        renderStarMap(starMap)
        println("--------------------------")
        coords.forEach { it.tick() }
    }
}

fun buildStarMap(coords: List<Coord>): Array<Array<String>> {
    val maxX = coords.map { it.x }.max()!! + 1
    val maxY = coords.map { it.y }.max()!! + 1
    val minX = coords.map { it.x }.min()!!
    val minY = coords.map { it.y }.min()!!

    val starMap = Array(maxY - minY) {
        Array(maxX - minX) { "." }
    }

    for (it in coords) {
        val adjY = it.y - minY
        val adjX = it.x - minX
        starMap[adjY][adjX] = "#"
    }

    return starMap
}

private fun renderStarMap(starMap: Array<Array<String>>) {
    for (y in 0 until starMap.size) {
        for (x in 0 until starMap[y].size) {
            print(starMap[y][x])
        }
        println("")
    }
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
}

fun String.parse(): Int {
    return this.trim().toInt()
}