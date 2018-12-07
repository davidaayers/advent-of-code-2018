package day07

import readFileIntoLines

fun main(args: Array<String>) {
    val allNodes = parsePuzzleInput("/day07/input.txt")

    println("allNodes = $allNodes")

    // to start, find our node that has no parents, that's step 1
    val nextNodes = allNodes.values
        .filter { it.parents.isEmpty() }
        .toSortedSet(Comparator { o1, o2 -> o1.name.compareTo(o2.name) })

    println("nextNodes = $nextNodes")

    val solutionList = mutableListOf<Node>()

    while (nextNodes.isNotEmpty()) {
        // remove the next node on the list, add it's children, then re-sort
        val nextNode = nextNodes.first {
            it.parents.isEmpty() || solutionList.containsAll(it.parents)
        }

        solutionList.add(nextNode)
        nextNodes.remove(nextNode)
        nextNodes.addAll(nextNode.children)
        nextNodes.sortedBy { it.name }
    }

    println("solutionList = $solutionList")

    val solution = solutionList.map { it.name }.toList().joinToString(separator = "")

    println("solution = $solution")

}

fun parsePuzzleInput(fileName: String, initialSeconds: Int = 60): MutableMap<String, Node> {
    val lines = readFileIntoLines(fileName)

    val allNodes = mutableMapOf<String, Node>()

    lines.forEach {
        val (parentName, childName) = it.parseToPair()
        val parent = allNodes.getOrPut(parentName) { Node(parentName, initialSeconds) }
        val child = allNodes.getOrPut(childName) { Node(childName, initialSeconds) }
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

data class Node(val name: String, val initialSeconds:Int = 60) {
    val parents: MutableList<Node> = mutableListOf()
    val children: MutableList<Node> = mutableListOf()
    var secondsLeft: Int = secondsToPerform()

    fun secondsToPerform() : Int {
        return initialSeconds + ( name[0] - 'A' + 1)
    }

    fun isDone() : Boolean {
        return secondsLeft == 0
    }

    override fun toString(): String {
        val parentsStr = parents.map { it.name }.toList().joinToString()
        val childrenStr = children.map { it.name }.toList().joinToString()
        return "$name: (${secondsToPerform()}:$secondsLeft) parents[$parentsStr] children[$childrenStr]"
    }
}