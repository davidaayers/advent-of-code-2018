package shared

abstract class BaseMap(val width: Int, val height: Int, val bgFill: Char = '#') {
    val map = Array(height) { CharArray(width) { bgFill } }

    fun addFeature(p: Point, feature: Char) {
        addFeature(p.x, p.y, feature)
    }

    fun addFeature(x: Int, y: Int, feature: Char) {
        map[y][x] = feature
    }

    fun feature(p: Point): Char {
        return feature(p.x, p.y)
    }

    fun feature(x: Int, y: Int): Char {
        return map[y][x]
    }

    fun adjacentTo(p: Point): List<Char> {
        return p.allDirs().mapNotNull { dir ->
            if (dir !in this) null else feature(dir)
        }.toList()
    }

    fun flatten(): List<Char> {
        return map.flatMap { line -> line.asList() }.toList()
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BaseMap

        if (width != other.width) return false
        if (height != other.height) return false
        if (!map.contentDeepEquals(other.map)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = width
        result = 31 * result + height
        result = 31 * result + map.contentDeepHashCode()
        return result
    }
}
