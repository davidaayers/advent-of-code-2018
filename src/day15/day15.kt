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
    var atkPower = 0
    var powerIncrement = 5
    var elvesWon = false

    var highestLoss = -1
    var lowestWin = Integer.MAX_VALUE

    while (true) {
        atkPower += powerIncrement
        print("Attack Power $atkPower...")
        elvesWon = simulateBattle("/day15/input.txt", atkPower, false)
        println("elves won: $elvesWon")

        if (elvesWon) {
            lowestWin = atkPower

            if (lowestWin == highestLoss + 1) {
                break
            }
            powerIncrement = -1
        } else {
            if(powerIncrement == -1) {
                atkPower++
                break
            }
            highestLoss = atkPower
        }

    }

    println("Winning Attack Power Was = $atkPower")
}

private fun simulateBattle(fileName: String, elfAttackPower: Int, allowElfDeaths: Boolean = true): Boolean {
    val map = parseMap(fileName, elfAttackPower)
    val numStartingElves = map.units.count { it.type == 'E' }
    var turn = 1
    loop@ while (true) {
        //println("TURN $turn START")
        val units = map.unitsInReadingOrder()
        for (it in units) {
            //println("BeforeTurn ($it) ->\n$map")
            it.takeTurn(map)
            map.removeDeadUnits()
            //println("AfterTurn ->\n$map")

            val nonDeadEnemies = map.nonDeadUnitsOfType(it.enemy)

            //println("nonDeadEnemies = ${nonDeadEnemies}")
            if (nonDeadEnemies == 0) {
                break@loop
            }
        }
        //println("TURN $turn OVER:\n$map")
        turn++
    }

    // don't count the last turn
    turn--

    println("Combat ended after $turn turns, final map\n$map")
    val remainingHp = map.units.sumBy { it.hitPoints }
    println("Answer to puzzle is $remainingHp * $turn = ${remainingHp * turn}")

    val numSurvivingElves = map.units.count { it.type == 'E' }
    println("numStartingElves = $numStartingElves numSurvivingElves = $numSurvivingElves")

    if (numSurvivingElves == 0) {
        return false
    }

    if (!allowElfDeaths) {
        return numStartingElves == numSurvivingElves
    }

    return true
}

fun parseMap(fileName: String, elftAttackPower: Int): Map {
    val lines = readFileIntoLines(fileName).map { it.trim() }
    val map = Map(lines[0].length, lines.size)

    var fighterId = 1
    lines.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            when (c) {
                '#', '.' -> map.addFeature(x, y, c)
                'E', 'G' -> {
                    val enemy = if (c == 'E') 'G' else 'E'
                    val atk = if (c == 'E') elftAttackPower else 3
                    map.addFeature(x, y, '.')
                    map.units.add(Fighter(fighterId++, c, enemy, x, y, atk))
                }
            }
        }
    }

    return map
}

class Map(width: Int, height: Int) {
    val map = Array(height) { CharArray(width) { '#' } }
    val units = mutableListOf<Fighter>()

    fun nonDeadUnitsOfType(type: Char): Int {
        return units.count { it.type == type && !it.isDead() }
    }

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

            sb.append("   ")

            // add all units for this line
            units.filter { it.y == y }.sortedBy { it.x }.forEach {
                sb.append("(${it.type}[${it.id}]:${it.hitPoints}) ")
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
            //println("frontier.size = ${frontier.size}")
            val exploring = frontier.poll()

            if (visited.hasOneLike(exploring)) continue
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
                        if (canMove(checkX, checkY) && !visited.hasOneLike(node)) {
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

    fun removeDeadUnits() {
        units.removeAll { it.isDead() }
    }
}

fun MutableSet<Node>.hasOneLike(other: Node): Boolean {
    return this.any { it.x == other.x && it.y == other.y }
}

data class Fighter(
    val id: Int,
    val type: Char,
    val enemy: Char,
    var x: Int,
    var y: Int,
    val attackPower: Int = 3,
    var hitPoints: Int = 200
) {
    fun takeTurn(map: Map) {
        // if we're dead, don't do anything
        if (isDead()) return

        // see if we're adjacent to any enemies
        var adjacentEnemies = map.adjacentUnitsOfType(this, enemy)

        if (adjacentEnemies.isEmpty()) {
            maybeMove(map)
        }

        adjacentEnemies = map.adjacentUnitsOfType(this, enemy)
        if (adjacentEnemies.isNotEmpty()) {
            attack(adjacentEnemies)
        }
    }

    private fun attack(enemies: List<Fighter>) {
        // sort the list of enemies by hp, then reading order
        val sortedEnemies = enemies
            .sortedWith(compareBy({ it.hitPoints }, { it.y }, { it.x }))
        val enemyToAttack = sortedEnemies
            .first()

        enemyToAttack.hitPoints -= attackPower
    }

    private fun maybeMove(map: Map) {
        // path to all enemies
        val enemies = map.units.filter { it.type == enemy }

        // for each enemy, find *all* possible paths to the available, adjacent squares
        val enemyPaths = mutableMapOf<Fighter, List<Pair<Int, Int>>>()
        for (enemy in enemies) {
            val endPoints = dirs.mapNotNull { (_, dir) ->
                val checkX = enemy.x + dir.dx
                val checkY = enemy.y + dir.dy
                if (map.canMove(checkX, checkY)) Pair(checkX, checkY) else null
            }

            val allPaths = mutableListOf<List<Pair<Int, Int>>>()
            for (point in endPoints) {
                val path = map.path(Pair(x, y), point)
                allPaths.add(
                    path
                )
            }

            if (allPaths.isNotEmpty()) {
                val correctPath = allPaths
                    .filter { it.isNotEmpty() }
                    .sortedWith(
                        compareBy(
                            { it.size },
                            { it.last().second },
                            { it.last().first },
                            { it.first().second },
                            { it.first().first })
                    ).firstOrNull()

                if (correctPath != null) {
                    enemyPaths[enemy] = correctPath
                }
            }
        }

        val movePath = enemyPaths
            .map { it.key to it.value }
            .sortedWith(compareBy({ it.second.size }, { it.first.y }, { it.first.x }))
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