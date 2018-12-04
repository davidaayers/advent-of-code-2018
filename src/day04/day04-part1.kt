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

    // what minute did he sleep the most?
    val (minute, _) = sleepiestElf.asleepTimes
        .toList()
        .sortedByDescending { (_, value) -> value }
        .first()

    println("sleepiestElf = $sleepiestElf, slept the most at $minute")
    val answer = sleepiestElf.id * minute
    println("Answer to puzzle is $answer")

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
}
