package day15

import readFileIntoLines
import java.util.*

val dirs = mapOf(
    "Up" to Direction(0, -1),
    "Left" to Direction(-1, 0),
    "Right" to Direction(1, 0),
    "Down" to Direction(0, 1)
)

fun main(args: Array<String>) {
    val map = parseMap("/day15/input-small.txt")
    println("Starting map:")
    println("$map")
    println("------------------------------")

    var turn = 1

    while ( turn <= 10 ){
        println("TURN $turn START")
        val units = map.unitsInReadingOrder()
        units.forEach {
            println("BeforeTurn ->\n$map")
            it.takeTurn(map)
            println("AfterTurn ->\n$map")
        }
        println("TURN $turn OVER")
        turn++
    }
}

fun parseMap(fileName: String): Map {
    val lines = readFileIntoLines(fileName).map { it.trim() }
    val map = Map(lines[0].length, lines.size)

    var fighterId = 1
    lines.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            when (c) {
                '#', '.' -> map.addFeature(x, y, c)
                'E', 'G' -> {
                    val enemy = if (c == 'E') 'G' else 'E'
                    map.addFeature(x, y, '.')
                    map.units.add(Fighter(fighterId++, c, enemy, x, y))
                }
            }
        }
    }

    return map
}

class Map(width: Int, height: Int) {
    val map = Array(height) { CharArray(width) { '#' } }
    val units = mutableListOf<Fighter>()

    fun addFeature(x: Int, y: Int, feature: Char) {
        map[y][x] = feature
    }

    fun unitsInReadingOrder(): List<Fighter> {
        return units.sortedWith(compareBy(Fighter::y, Fighter::x)).toList()
    }

    override fun toString(): String {
        val sb = StringBuffer()
        map.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                sb.append(units.find { it.x == x && it.y == y }?.type ?: c)
            }
            sb.append("\n")
        }
        return sb.toString()
    }

    fun adjacentUnitsOfType(fighter: Fighter, enemy: Char): List<Fighter> {
        return dirs.values.mapNotNull {
            units.firstOrNull { u ->
                u.x == fighter.x + it.dx &&
                        u.y == fighter.y + it.dy &&
                        u.type == enemy
            }
        }
    }

    fun path(from: Pair<Int, Int>, to: Pair<Int, Int>): List<Pair<Int, Int>> {
        val visited = mutableSetOf<Node>()
        val frontier: Queue<Node> = LinkedList<Node>()
        frontier.add(Node(null, from.first, from.second))

        while (frontier.isNotEmpty()) {
            val exploring = frontier.poll()
            visited.add(exploring)

            if (exploring.x == to.first && exploring.y == to.second) {
                val path = mutableListOf<Pair<Int, Int>>()

                var node = exploring
                while (node.parent != null) {
                    path.add(0, Pair(node.x, node.y))
                    node = node.parent
                }

                return path
            } else {
                // add surrounding squares to search
                frontier.addAll(
                    dirs.values.mapNotNull {
                        val checkX = exploring.x + it.dx
                        val checkY = exploring.y + it.dy
                        val node = Node(exploring, checkX, checkY)
                        // if node if the destination, add it anyway
                        if (checkX == to.first && checkY == to.second) {
                            node
                        } else if (canMove(checkX, checkY) && !visited.contains(node)) {
                            node
                        } else {
                            null
                        }
                    })
            }
        }

        // no path
        return mutableListOf()
    }

    fun canMove(x: Int, y: Int): Boolean {
        // bounds
        if (x < 0 || x >= map[0].size || y < 0 || y >= map.size) return false

        // walls
        if (map[y][x] == '#') return false

        // units
        return units.none { it.x == x && it.y == y }
    }
}

data class Fighter(
    val id: Int,
    val type: Char,
    val enemy: Char,
    var x: Int,
    var y: Int,
    val attack: Int = 3,
    var hitPoints: Int = 200
) {
    fun takeTurn(map: Map) {
        println("Fighter: $this taking turn -->")

        // if we're dead, don't do anything
        if (isDead()) return

        // see if we're adjacent to any enemies
        val adjacentEnemies = map.adjacentUnitsOfType(this, enemy)

        if (adjacentEnemies.isNotEmpty()) {
            attack(adjacentEnemies)
        } else {
            move(map)
        }
    }

    private fun attack(enemies: List<Fighter>) {

    }

    private fun move(map: Map) {
        // path to all enemies
        val enemies = map.units.filter { it.type == enemy }

        // for each enemy, find *all* possible paths to the available, adjacent squares
        val enemyPaths = mutableMapOf<Fighter, List<Pair<Int, Int>>>()
        enemies.forEach { enemy ->
            val endPoints = dirs.mapNotNull { (_, dir) ->
                val checkX = enemy.x + dir.dx
                val checkY = enemy.y + dir.dy
                if (map.canMove(checkX, checkY)) Pair(checkX, checkY) else null
            }

            val allPaths = endPoints.map { point ->
                map.path(Pair(x, y), point)
            }

            // find the shortest, and then sort by readingOrder of the first step
            val min = allPaths.groupingBy { it.size }.eachCount().minBy { it.key }?.key ?: 0

            if (min > 0) {
                val shortestPathsByReadingOrder = allPaths
                    .filter { it.size == min }
                    .sortedWith(compareBy({ it[0].second }, { it[0].first }))
                enemyPaths[enemy] = shortestPathsByReadingOrder[0]
            }
        }

        val sortedByReadingOrder = enemyPaths
            .map { it.key to it.value }
            .sortedWith(compareBy({ it.second.size }, { it.first.y }, { it.first.x }))


        val movePath = sortedByReadingOrder
            .map { it.second }
            .firstOrNull()

        if (movePath != null) {
            val firstStep = movePath[0]
            x = firstStep.first
            y = firstStep.second
        }
    }

    fun isDead(): Boolean {
        return hitPoints <= 0
    }
}

data class Direction(val dx: Int, val dy: Int)

data class Node(val parent: Node?, val x: Int, val y: Int, val children: List<Node> = mutableListOf())