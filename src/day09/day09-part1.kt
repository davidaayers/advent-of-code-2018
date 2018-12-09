package day09

fun main(args: Array<String>) {
    val numMarbles = 72164
    val scores = Array(419) { 0 }

    val marbles = (1..numMarbles).toMutableList()

    var currentMarble = Node(0)
    val circle = Circle(currentMarble)

    while (marbles.isNotEmpty()) {
        for (player in 0 until scores.size) {
            if (marbles.isNotEmpty()) {
                val marble = marbles.removeAt(0)
                if (marble % 23 == 0) {
                    // time to score
                    scores[player] += marble

                    // also remove the marble 7 counter-clockwise and
                    // add to score
                    val otherMarble = circle.navigateClockwise(currentMarble, 7)
                    currentMarble = circle.remove(otherMarble).next!!

                    scores[player] += otherMarble.score

                    println(
                        "player: $player scores [marble = $marble, " +
                                "otherMarble = ${otherMarble.score}]: ${scores[player]}"
                    )
                    //println("circle = $circle")
                } else {
                    currentMarble = circle.insert(currentMarble.next!!, Node(marble))
                    //println("player: $player places $currentMarble circle = $circle")
                }
            }
        }
    }

    //println("scores = ${scores.joinToString()}")
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
        for (it in 0 until times) {
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