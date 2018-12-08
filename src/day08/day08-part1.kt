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
    val rootNode = parseNode(licenseParts,nameGenerator)

    println("rootNode = $rootNode")

    val allNodes = mutableListOf(rootNode)
    allNodes.addAll(rootNode.allChildren)

    println("allNodes = ${allNodes.map { it.name }}")

    val answer = allNodes.flatMap { it.metadata.toList() }.sum()

    println("answer = $answer")

}

fun parseNode(licenseParts: MutableList<Int>, nameGenerator: AtomicInteger): Node {
    val childrenCnt = licenseParts.removeAt(0)
    val metadataCnt = licenseParts.removeAt(0)
    val node = Node(nameGenerator.nextName())

    for (n in 0 until childrenCnt) {
        val child = parseNode(licenseParts,nameGenerator)
        node.children.add(child)
    }

    for (x in 0 until metadataCnt) {
        node.metadata.add(licenseParts.removeAt(0))
    }
    return node
}

fun AtomicInteger.nextName(): String {
    return this.getAndAdd(1).toChar().toString()
}

data class Node(val name: String) {
    val metadata: MutableList<Int> = mutableListOf()
    val children: MutableList<Node> = mutableListOf()

    val allChildren: List<Node>
        get() = children + children.flatMap { it.allChildren }

    override fun toString(): String {
        return "$name (Metadata: $metadata, children: $children"
    }
}