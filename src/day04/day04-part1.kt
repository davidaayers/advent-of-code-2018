package day04

import readFileIntoLines

fun main(args: Array<String>) {
    val lines = readFileIntoLines("/day04/input.txt").sorted()
    val elves: MutableMap<Int, Elf> = mutableMapOf()
    var currentElf: Elf? = null
    var startMinute = 0

    lines.forEach {
        //println(it)
        if (it.contains('#')) {
            val elfId = getElfId(it)
            currentElf = elves.getOrPut(elfId) { Elf(elfId) }
            println("Elf $currentElf is beginning shift")
        }
        val minutes = getMinutes(it)
        if (it.contains("falls")) {
            startMinute = minutes
        }
        if (it.contains("wakes")) {
            // record the sleeping time in the elf
            println("Elf started sleeping at $startMinute, ended at $minutes")
            for (x in startMinute until minutes) {
                currentElf!!.asleepTimes.merge(x, 1) { a, b -> a + b }
            }
            //println("asleep times = ${currentElf!!.asleepTimes}, total sleep time = ${currentElf!!.totalSleepTime()}")
        }
    }

    elves.forEach { k, v -> println("Elf: $v, total sleep time: ${v.totalSleepTime()}") }

    // find the elf that slept the most
    val sleepiestElf = elves.map { it.value }
        .toList()
        .sortedByDescending { it.totalSleepTime() }
        .first()

    val minute = sleepiestElf.greatestSleepMinute()

    println("sleepiestElf = $sleepiestElf, slept the most at $minute")
    val answer = sleepiestElf.id * minute
    println("Answer (Strategy 1) to puzzle is $answer")

    val sleepiestElf2 = elves.map { it.value }
        .toList()
        .sortedByDescending { it.greatestNumberOfTimesSleptInAMinute() }
        .first()

    val minute2 = sleepiestElf2.greatestSleepMinute()
    println("sleepiestElf2 = $sleepiestElf2, slept the most at $minute2 " +
            "(total of ${sleepiestElf2.greatestNumberOfTimesSleptInAMinute()} times)")
    val answer2 = sleepiestElf2.id * minute2
    println("Answer (Strategy 2) to puzzle is $answer2")

}

fun getElfId(input: String): Int {
    val regex = """.*#(\d+) .*""".toRegex()
    val matchResult = regex.find(input)
    val (id) = matchResult!!.destructured
    return id.toInt()
}

fun getMinutes(input: String): Int {
    val regex = """\[.* \d{2}:(\d{2})].*""".toRegex()
    val matchResult = regex.find(input)
    val (minutes) = matchResult!!.destructured
    return minutes.toInt()
}

data class Elf(val id: Int) {
    val asleepTimes: MutableMap<Int, Int> = mutableMapOf()

    fun totalSleepTime(): Int {
        return asleepTimes.map { it.value }.sum()
    }

    fun greatestSleepMinute(): Int {
        val (minute, _) = findMostSleepyMinute()
        return minute
    }

    fun greatestNumberOfTimesSleptInAMinute(): Int {
        val (_, times) = findMostSleepyMinute()
        return times
    }

    private fun findMostSleepyMinute(): Pair<Int, Int> {
        return asleepTimes
            .toList()
            .sortedByDescending { (_, value) -> value }
            .firstOrNull() ?: Pair(0,0)
    }

}
