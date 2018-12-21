package day21

import day19.parseInput
import day19.runInstructions

fun main(args: Array<String>) {
    val opCodes = day16.opcodes.map { it.name to it }.toMap()

    val (ipRegister, instructions) = parseInput("/day21/input.txt")

    val registers = intArrayOf(0, 0, 0, 0, 0, 0)
    val instructionCnt = runInstructions(instructions, registers, ipRegister, opCodes, true)

    println("Final registers = ${registers.toList()} after $instructionCnt instructions")

}