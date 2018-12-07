package day07

fun main(args: Array<String>) {
    val allNodes = parsePuzzleInput("/day07/input.txt", 60)

    // to start, find our node that has no parents, that's step 1
    val nextTasks = allNodes.values
        .filter { it.parents.isEmpty() }
        .toSortedSet(Comparator { o1, o2 -> o1.name.compareTo(o2.name) })

    var elapsedSeconds = 0

    val elves = Array(5) { Elf() }

    val completedTasks = mutableListOf<Node>()
    val workingTasks = mutableListOf<Node>()

    while (nextTasks.isNotEmpty() || workingTasks.isNotEmpty()) {

        // any elves that finished their task last minute should now be available for
        // new tasks
        // elves with tasks should work on those tasks
        val workingElves = elves.filter { it.currentTask != null }
        for (it in workingElves) {
            val currentTask = it.currentTask!!
            if (currentTask.isDone()) {
                workingTasks.remove(currentTask)
                it.currentTask = null

                // if the currentTask has children, they can now be done, so add them
                // to the list of next tasks and re-sort it
                completedTasks.add(currentTask)
                nextTasks.addAll(currentTask.children)
                nextTasks.sortedBy { it.name }
            }
        }


        // see if we have any elves available to work, this particular second
        val idleElves = elves.filter { it.currentTask == null }

        if (idleElves.isNotEmpty()) {
            // see if we have any tasks that can be performed
            val availableTasks = nextTasks.filter {
                it.parents.isEmpty() || completedTasks.containsAll(it.parents)
            }.iterator()

            val elvesIterator = idleElves.iterator()

            while (availableTasks.hasNext() && elvesIterator.hasNext()) {
                val nextElf = elvesIterator.next()
                val taskToWorkOn = availableTasks.next()
                nextElf.currentTask = taskToWorkOn
                nextTasks.remove(taskToWorkOn)
                workingTasks.add(taskToWorkOn)
            }
        }

        // elves with tasks should work on those tasks
        elves.filter { it.currentTask != null }
            .forEach { it.work() }

        // print a report of activity
        print("%03d ".format(elapsedSeconds))
        elves.forEachIndexed { idx, elf ->
            val taskName = if (elf.currentTask != null) elf.currentTask!!.name else "."
            print("$taskName ")
            if (idx == elves.size - 1) println("")
        }


        elapsedSeconds++
    }

    println("Answer is ${elapsedSeconds-1}")
}

class Elf {
    var currentTask: Node? = null

    fun work() {
        currentTask?.let {
            it.secondsLeft--
        }
    }

    override fun toString(): String {
        return "Working on: ${currentTask ?: "nothing"}"
    }
}
