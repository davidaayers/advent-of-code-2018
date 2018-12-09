package day09

fun main(args: Array<String>) {
    part1()
    println("-------------------------------------------")
    part2()
}

fun part1() {
    playGame(419, 72164)
}

fun part2() {
    playGame(419, 7216400)
}

private fun playGame(
    numPlayers: Int,
    numMarbles: Int
) {
    val scores = LongArray(numPlayers)
    var currentMarble = Node(0)
    val circle = Circle(currentMarble)

    var player = 0
    for (m in 1..numMarbles) {
        if (m % 23 == 0) {
            // time to score
            scores[player] += m.toLong()

            // also remove the marble 7 counter-clockwise and
            // add to score
            val otherMarble = circle.navigateClockwise(currentMarble, 7)
            currentMarble = circle.remove(otherMarble).next!!

            scores[player] += otherMarble.score.toLong()

//            println(
//                "player: $player scores [marble = $m, " +
//                        "otherMarble = ${otherMarble.score}]: ${scores[player]}"
//            )
        } else {
            currentMarble = circle.insert(currentMarble.next!!, Node(m))
            //println("player: $player places $currentMarble circle = $circle")
        }
        player++
        if (player == numPlayers) {
            player = 0
        }
    }

    val winningScore = scores.max()
    println("winningScore = $winningScore")
}

data class Node(val score: Int, var next: Node? = null, var prev: Node? = null) {
    override fun toString(): String {
        return "[$score]"
    }
}

class Circle(private val head: Node) {

    init {
        head.next = head
        head.prev = head
    }

    fun insert(after: Node, new: Node): Node {
        new.next = after.next
        after.next!!.prev = new
        new.prev = after
        after.next = new
        return new
    }

    fun remove(toRemove: Node): Node {
        toRemove.prev!!.next = toRemove.next
        toRemove.next!!.prev = toRemove.prev
        return toRemove
    }

    fun navigateClockwise(fromNode: Node, times: Int): Node {
        var result = fromNode
        repeat(times) {
            result = result.prev!!
        }
        return result
    }

    override fun toString(): String {
        val sb = StringBuffer()
        var current = head
        sb.append("$current -> ")
        do {
            current = current.next!!
            sb.append("$current -> ")
        } while (current.next != head)
        return sb.toString()
    }
}