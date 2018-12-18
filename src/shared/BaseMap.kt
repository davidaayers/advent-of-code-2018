package shared

abstract class BaseMap(val width: Int, val height: Int, val bgFill: Char = '#') {
    val map = Array(height) { CharArray(width) { bgFill } }

    fun addFeature(x: Int, y: Int, feature: Char) {
        map[y][x] = feature
    }

    override fun toString(): String {
        val sb = StringBuffer()
        map.forEachIndexed { y, line ->
            beforeLine(y, line, sb)
            line.forEachIndexed { x, c ->
                char(x, y, c, sb)
            }
            afterLine(y, line, sb)
        }
        return sb.toString()
    }

    protected fun beforeLine(y: Int, line: CharArray, sb: StringBuffer) {
    }

    protected fun char(x: Int, y: Int, c: Char, sb: StringBuffer) {
        sb.append(c)
    }

    protected fun afterLine(y: Int, line: CharArray, sb: StringBuffer) {
        sb.append("\n")
    }

    fun clone(): BaseMap {
        val clone = instantiateMap(width, height, bgFill)
        map.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                clone.addFeature(x, y, c)
            }
        }
        return clone
    }

    abstract protected fun instantiateMap(width: Int, height: Int, bgChar: Char): BaseMap

    operator fun contains(point: Point): Boolean {
        return point.x >= 0 && point.x < map[0].size && point.y >= 0 && point.y < map.size
    }
}
