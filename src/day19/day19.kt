package day19

import readFileIntoLines

fun main(args: Array<String>) {
    val opCodes = day16.part1().map { it.value.name to it.value }.toMap()

    val (ipRegister, instructions) = parseInput("/day19/input.txt")

    val registers = intArrayOf(1, 0, 0, 0, 0, 0)
    var ip = 0

    var instructionCnt = 0
    while (true) {
        instructionCnt++
        if(instructionCnt % 100 == 0){
            println("instructionCnt: $instructionCnt")
        }
        // execute the instruction based on the pointer
        val i = instructions[ip]

        // write the value of the instruction pointer
        registers[ipRegister] = ip

        print("ip=$ip ${registers.toList()} ")

        opCodes[i.op]?.operate(registers, i.operation())

        println("${i.op} ${i.a} ${i.b} ${i.c} ${registers.toList()}")

        // increment the instruction pointer
        ip = registers[ipRegister] + 1

        if (ip >= instructions.size) {
            break
        }
    }

    println("Final registers = ${registers.toList()}")

}

fun parseInput(fileName: String): Pair<Int, List<Instruction>> {
    val lines = readFileIntoLines(fileName).toMutableList()

    // instruction pointer is line 1
    val instructionPointer = lines.removeAt(0).last().toString().toInt()

    val instructions = lines.map {
        val parts = it.split(" ")
        Instruction(parts[0], parts[1].toInt(), parts[2].toInt(), parts[3].toInt())
    }

    return Pair(instructionPointer, instructions)
}

data class Instruction(val op: String, val a: Int, val b: Int, val c: Int) {
    fun operation(): IntArray {
        return intArrayOf(0, a, b, c)
    }
}