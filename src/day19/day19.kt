package day19

import day16.OpCode
import readFileIntoLines

fun main(args: Array<String>) {
    val opCodes = day16.opcodes.map { it.name to it }.toMap()

    val (ipRegister, instructions) = parseInput("/day19/input.txt")

    val registers = intArrayOf(0, 0, 0, 0, 0, 0)
    val instructionCnt = runInstructions(instructions, registers, ipRegister, opCodes)

    println("Final registers = ${registers.toList()} after $instructionCnt instructions")

}

fun runInstructions(
    instructions: List<Instruction>,
    registers: IntArray,
    ipRegister: Int,
    opCodes: Map<String, OpCode>,
    debug: Boolean = false
): Int {
    var ip = 0

    var instructionCnt = 0
    while (true) {
        instructionCnt++
        if (instructionCnt % 100 == 0 && debug) {
            println("instructionCnt: $instructionCnt")
        }

        // execute the instruction based on the pointer
        val i = instructions[ip]

        // write the value of the instruction pointer
        registers[ipRegister] = ip

        if (debug) print("ip=$ip ${registers.toList()} ")

        opCodes[i.op]?.operate(registers, i.operation())

        if (debug) println("${i.op} ${i.a} ${i.b} ${i.c} ${registers.toList()}")

        // increment the instruction pointer
        ip = registers[ipRegister] + 1

        if (ip >= instructions.size) {
            break
        }
    }

    return instructionCnt
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