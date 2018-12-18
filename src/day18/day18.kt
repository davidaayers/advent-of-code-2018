package day18

import shared.BaseMap
import shared.BaseMapParser
import shared.Point

var open = '.'
var trees = '|'
var lumberyard = '#'

var rules = listOf(
    Rule.Rule1,
    Rule.Rule2,
    Rule.Rule3
)

fun main(args: Array<String>) {
    var map = MapParser("/day18/input.txt").parse() as Map
    println("Initial:")
    println(map)

    for (minute in 1..10) {
        val nextMap = Map(map.width, map.height)
        map.map.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                rules.forEach { rule ->
                    val point = Point(x, y)
                    if(rule.applies(point,map)) {
                        val newFeature = rule.evaluate(point, map)
                        nextMap.addFeature(point, newFeature)
                    }
                }
            }
        }
        println("After $minute minutes:")
        println(nextMap)
        map = nextMap
    }

    val allTerrain = map.flatten()
    val numTrees = allTerrain.count { it == trees }
    val numLumberyards = allTerrain.count { it == lumberyard }

    println("Puzzle Answer = $numTrees * $numLumberyards = ${numTrees * numLumberyards}")

}

sealed class Rule {
    abstract fun applies(p: Point, map: Map): Boolean
    abstract fun evaluate(p: Point, map: Map): Char

    /*
    An open acre will become filled with trees if three or more adjacent acres contained trees.
    Otherwise, nothing happens.
     */
    object Rule1 : Rule() {
        override fun applies(p: Point, map: Map): Boolean {
            return map.feature(p) == open
        }

        override fun evaluate(p: Point, map: Map): Char {
            val numTrees = map.adjacentTo(p).count { it == trees }
            return if (numTrees >= 3) trees else open
        }
    }

    /*
    An acre filled with trees will become a lumberyard if three or more adjacent acres were lumberyards.
    Otherwise, nothing happens.
     */
    object Rule2 : Rule() {
        override fun applies(p: Point, map: Map): Boolean {
            return map.feature(p) == trees
        }

        override fun evaluate(p: Point, map: Map): Char {
            val numLumberyards = map.adjacentTo(p).count { it == lumberyard }
            return if (numLumberyards >= 3) lumberyard else trees
        }
    }

    /*
    An acre containing a lumberyard will remain a lumberyard if it was adjacent to at least one other lumberyard
    and at least one acre containing trees. Otherwise, it becomes open.
     */
    object Rule3 : Rule() {
        override fun applies(p: Point, map: Map): Boolean {
            return map.feature(p) == lumberyard
        }

        override fun evaluate(p: Point, map: Map): Char {
            val adjacent = map.adjacentTo(p)
            val numLumberyards = adjacent.count { it == lumberyard }
            val numTrees = adjacent.count { it == trees }

            return if (numLumberyards >= 1 && numTrees >= 1) lumberyard else open
        }
    }
}

class Map(width: Int, height: Int) : BaseMap(width, height, '.') {
    override fun instantiateMap(width: Int, height: Int, bgChar: Char): BaseMap {
        return Map(width, height)
    }
}

class MapParser(fileName: String) : BaseMapParser(fileName) {
    override fun instantiateMap(width: Int, height: Int, bgChar: Char): BaseMap {
        return Map(width, height)
    }
}
