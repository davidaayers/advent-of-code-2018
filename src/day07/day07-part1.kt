package day07

import readFileIntoLines

fun main(args: Array<String>) {
    val allNodes = parsePuzzleInput("/day07/input.txt")

    println("allNodes = $allNodes")

    // to start, find our node that has no parents, that's step 1
    val nextNodes = allNodes.values
        .filter { it.parents.isEmpty() }
        .sortedBy { it.name }
        .toMutableList()

    println("nextNodes = $nextNodes")

    val solutionList = mutableListOf<Node>()

    while (nextNodes.isNotEmpty()) {
        // remove the next node on the list, add it's children, then re-sort
        val nextNode = nextNodes.first {
            it.parents.isEmpty() || solutionList.containsAll(it.parents)
        }

        solutionList.add(nextNode)
        nextNodes.remove(nextNode)
        nextNode.children.forEach { if (it !in nextNodes) nextNodes.add(it) }
        nextNodes.sortBy { it.name }
    }

    println("solutionList = $solutionList")

    val solution = solutionList.map { it.name }.toList().joinToString(separator = "")

    println("solution = $solution")

}

private fun parsePuzzleInput(fileName: String): MutableMap<String, Node> {
    val lines = readFileIntoLines(fileName)

    val allNodes = mutableMapOf<String, Node>()

    lines.forEach {
        val (parentName, childName) = it.parseToPair()
        val parent = allNodes.getOrPut(parentName) { Node(parentName) }
        val child = allNodes.getOrPut(childName) { Node(childName) }
        parent.children.add(child)
        child.parents.add(parent)
    }
    return allNodes
}

fun String.parseToPair(): Pair<String, String> {
    val (parent, child) = """Step (\w) must be finished before step (\w) can begin.""".toRegex()
        .find(this)!!
        .destructured
    return Pair(parent, child)
}

data class Node(val name: String) {
    val parents: MutableList<Node> = mutableListOf()
    val children: MutableList<Node> = mutableListOf()

    override fun toString(): String {
        val parentsStr = parents.map { it.name }.toList().joinToString()
        val childrenStr = children.map { it.name }.toList().joinToString()
        return "$name: parents[$parentsStr] children[$childrenStr]"
    }
}