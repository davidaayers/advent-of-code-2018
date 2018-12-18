package day13

import readEntireFileNoTrim

val dirs = mapOf(
    '>' to Direction('>', 1, 0),
    '<' to Direction('<', -1, 0),
    'v' to Direction('v', 0, 1),
    '^' to Direction('^', 0, -1)
)

const val debugging = false

fun main(args: Array<String>) {
    val (map, carts) = parseInput("/day13/input.txt")

    if (debugging) {
        println("carts = ${carts}")
        printMap(map, carts)
        println("---------------")
    }

    do {
        // move the carts 1 at a time, from top to bottom, left to right
        val sortedCarts = carts.sortedWith(compareBy({ it.y }, { it.x }))
        sortedCarts.forEach { cart ->
            cart.moveInCurrentDirection(map)

            if (debugging) {
                printMap(map, carts)
                println("---------------")
            }

            // see if this movement caused a collision
            val other = carts.filter { c -> c != cart }
                .firstOrNull { c -> c.x == cart.x && c.y == cart.y }

            if (other != null) {
                println("Cart #${cart.cartId} collided with Cart #${other.cartId} at ${cart.x},${cart.y}")
                // remove them both
                carts.remove(cart)
                carts.remove(other)
            }
        }

    } while (carts.size != 1)

    println("Last remaining cart: $carts[0]")
}

private fun printMap(map: Array<CharArray>, carts: List<Cart>) {
    val newMap = map.copy()
    carts.forEach { cart ->
        newMap[cart.y][cart.x] = cart.dir.char
    }

    newMap.forEach { yArr ->
        yArr.forEach { actualChar ->
            print("$actualChar")
        }
        println("")
    }
}

fun properTrackCharAt(x: Int, y: Int, map: Array<CharArray>): Char {
    if (map[y][x] == ' ') return ' '

    // based on adjacent characters, figure out what character we need to put here
    val up = if (y > 0) map[y - 1][x] else 'z'
    val down = if (y + 1 < map.size) map[y + 1][x] else 'z'
    val right = if (x + 1 < map[0].size) map[y][x + 1] else 'z'
    val left = if (x > 0) map[y][x - 1] else 'z'

    val t = ">v^<"

    return when {
        up in "\\/- z$t" && right in "+-\\/$t" && down in "z\\/- $t" && left in "\\/+-$t" -> '-'
        up in "\\/|+$t" && right in "z|\\/ $t" && down in "\\/|+$t" && left in "z\\/| $t" -> '|'
        else -> throw IllegalStateException()
    }
}

fun parseInput(fileName: String): Pair<Array<CharArray>, MutableList<Cart>> {
    val lines = readEntireFileNoTrim(fileName).split("\n")
    val longestLine = lines.maxBy { it.length }!!.length
    val map = Array(lines.size) { CharArray(longestLine) { ' ' } }
    val carts = mutableListOf<Cart>()
    var cartId = 0
    lines.forEachIndexed { y, line ->
        line.toCharArray().forEachIndexed { x, c ->
            map[y][x] = c
            dirs[c]?.let {
                carts.add(Cart(cartId++, x, y, it))
            }
        }
    }

    // replace all of the carts on the track with actual track chars
    carts.forEach { cart ->
        map[cart.y][cart.x] = properTrackCharAt(cart.x, cart.y, map)
    }

    return Pair(map, carts)
}

data class Cart(val cartId: Int, var x: Int, var y: Int, var dir: Direction, var moves: Int = 0) {

    var moveInstructions = mutableListOf(
        "left", "straight", "right"
    )

    fun moveInCurrentDirection(map: Array<CharArray>) {
        moves++
        // evaluate the move, but remember the previous square
        val oldChar = map[y][x]
        val oldDir = dir

        y += dir.dy
        x += dir.dx

        // see what the next char in dir is
        val newChar = map[y][x]
        if (newChar == '+') {
            // make a decision
            val instruction = moveInstructions.removeAt(0)
            when (instruction) {
                "left" -> dir = dir.leftTurn()
                "right" -> dir = dir.rightTurn()
            }
            moveInstructions.add(instruction)
        }

        if (newChar == '/') {
            dir = if (oldDir == dirs['^'] || oldDir == dirs['v']) dir.rightTurn() else dir.leftTurn()
        }

        if (newChar == '\\') {
            dir = if (oldDir == dirs['v'] || oldDir == dirs['^']) dir.leftTurn() else dir.rightTurn()
        }
    }
}

data class Direction(val char: Char, val dx: Int, val dy: Int) {
    fun leftTurn(): Direction {
        return when (char) {
            '^' -> dirs['<']!!
            '>' -> dirs['^']!!
            'v' -> dirs['>']!!
            '<' -> dirs['v']!!
            else -> {
                throw java.lang.IllegalStateException()
            }
        }
    }

    fun rightTurn(): Direction {
        return when (char) {
            '^' -> dirs['>']!!
            '>' -> dirs['v']!!
            'v' -> dirs['<']!!
            '<' -> dirs['^']!!
            else -> {
                throw java.lang.IllegalStateException()
            }
        }
    }
}

fun Array<CharArray>.copy() = map { it.clone() }.toTypedArray()