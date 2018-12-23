package shared

data class Point(val x: Int, val y: Int) {

    val up get() = Point(x, y - 1)
    val down get() = Point(x, y + 1)
    val left get() = Point(x - 1, y)
    val right get() = Point(x + 1, y)

    val upRight get() = Point(x + 1, y - 1)
    val upLeft get() = Point(x - 1, y - 1)
    val downRight get() = Point(x + 1, y + 1)
    val downLeft get() = Point(x - 1, y + 1)

    fun cardinalDirs(): List<Point> {
        return listOf(up, right, down, left)
    }

    fun ordinalDirs(): List<Point> {
        return listOf(upRight, downRight, downLeft, upLeft)
    }

    fun allDirs(): List<Point> {
        return listOf(cardinalDirs(), ordinalDirs()).flatten()
    }

    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }

    fun manhattanDistanceTo(other: Point): Float {
        return (Math.abs(other.x - this.x) + Math.abs(other.y - this.y)).toFloat()
    }

}