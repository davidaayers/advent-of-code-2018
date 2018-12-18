package day18

import shared.BaseMap
import shared.BaseMapParser

fun main(args: Array<String>) {
    val map = MapParser("/day18/input-small.txt").parse()
    println(map)
}

class Map(width: Int, height: Int) : BaseMap(width, height, '.')

class MapParser(fileName: String) : BaseMapParser(fileName) {
    override fun instantiateMap(width: Int, height: Int, bgChar: Char): BaseMap {
        return Map(width, height)
    }
}

