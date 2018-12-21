package day21

import day16.OpCode
import day19.Instruction
import day19.parseInput

fun main(args: Array<String>) {
    val opCodes = day16.opcodes.map { it.name to it }.toMap()

    val (ipRegister, instructions) = parseInput("/day21/input.txt")

    val registers = intArrayOf(0, 0, 0, 0, 0, 0)
    val instructionCnt = runInstructions(instructions, registers, ipRegister, opCodes, false)

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

    val uniqueValues = mutableSetOf<Int>()
    var lastValue = 0

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

        if (ip == 28) {
            if (uniqueValues.isEmpty()) {
                println("Part 1 is reg 5 is ${registers[5]}")
            }
            if (uniqueValues.contains(registers[5])) {
                println("Part 2 is reg 5 is $lastValue")
                break
            }
            lastValue = registers[5]
            uniqueValues.add(registers[5])
        }

        // increment the instruction pointer
        ip = registers[ipRegister] + 1

        if (ip >= instructions.size) {
            break
        }
    }

    return instructionCnt
}