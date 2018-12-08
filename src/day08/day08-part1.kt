package day08

import readFileIntoLines
import java.util.concurrent.atomic.AtomicInteger

fun main(args: Array<String>) {
    parsePuzzleInput("/day08/input.txt")
}

fun parsePuzzleInput(fileName: String) {
    val lines = readFileIntoLines(fileName)
    val licenseParts = lines
        .flatMap { line -> line.split(" ").toList().map { it.toInt() } }
        .toMutableList()

    val nameGenerator = AtomicInteger('A'.toInt())
    val rootNode = parseNode(licenseParts, nameGenerator)

    println("rootNode = $rootNode")

    val allNodes = mutableListOf(rootNode)
    allNodes.addAll(rootNode.allChildren)

    println("allNodes = ${allNodes.map { it.name }}")

    val answer = allNodes.flatMap { it.metadata.toList() }.sum()

    println("Part 1 answer = $answer")

    println("Part 2 answer = ${rootNode.value()}")

}

fun parseNode(licenseParts: MutableList<Int>, nameGenerator: AtomicInteger): Node {
    val childrenCnt = licenseParts.removeAt(0)
    val metadataCnt = licenseParts.removeAt(0)
    val node = Node(nameGenerator.nextName())

    node.children = (0 until childrenCnt)
        .map { parseNode(licenseParts, nameGenerator) }
        .toList()

    node.metadata = (0 until metadataCnt)
        .map { licenseParts.removeAt(0) }
        .toList()

    return node
}

fun AtomicInteger.nextName(): String {
    return this.getAndAdd(1).toChar().toString()
}

data class Node(val name: String) {
    var metadata: List<Int> = listOf()
    var children: List<Node> = listOf()

    val allChildren: List<Node>
        get() = children + children.flatMap { it.allChildren }

    fun value(): Int {
        return when {
            children.isEmpty() -> metadata.sum()
            else -> metadata.sumBy {
                children.getOrNull(it - 1)?.value() ?: 0
            }
        }
    }

    override fun toString(): String {
        return "$name (Metadata: $metadata, children: $children"
    }
}