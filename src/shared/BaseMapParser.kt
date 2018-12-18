package shared

import readFileIntoLines

abstract class BaseMapParser(val fileName: String, val bgChar: Char = '#') {
    fun parse(): BaseMap {
        val lines = readFileIntoLines(fileName).map { it.trim() }
        val width = lines[0].length
        val height = lines.size
        val map = instantiateMap(width, height, bgChar)
        lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                addFeature(map, x, y, c)
            }
        }
        return map
    }

    abstract protected fun instantiateMap(width: Int, height: Int, bgChar: Char): BaseMap

    protected fun addFeature(map: BaseMap, x: Int, y: Int, c: Char) {
        map.addFeature(x, y, c)
    }
}