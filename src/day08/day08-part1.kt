package day08

import readFileIntoLines

fun main(args: Array<String>) {

}

fun parsePuzzleInput(fileName: String) {
    val lines = readFileIntoLines(fileName)
    val licenseParts = lines
        .flatMap { it.split(" ").toList() }
        .toList()



}

data class Node(val name: String) {
    val metadata: MutableList<Int> = mutableListOf()
    val children: MutableList<Node> = mutableListOf()

    override fun toString(): String {
        return "$name (Metadata: $metadata, children: $children"
    }
}