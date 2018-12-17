package day16

import readFileIntoLines

val opcodes = listOf(
    OpCode.Addr(),
    OpCode.Addi(),
    OpCode.Mulr(),
    OpCode.Muli(),
    OpCode.Banr(),
    OpCode.Bani(),
    OpCode.Borr(),
    OpCode.Bori(),
    OpCode.Setr(),
    OpCode.Seti(),
    OpCode.Gtir(),
    OpCode.Gtri(),
    OpCode.Gtrr(),
    OpCode.Eqir(),
    OpCode.Eqri(),
    OpCode.Eqrr()
)

fun main(args: Array<String>) {
    val allTestData = parseInput("/day16/input.txt")
    val counts = allTestData.map { testData ->
        val matchingOpcodes = opcodes.filter { testData.matches(it) }

        println("TestData: $testData")
        println("Matching Opcodes: $matchingOpcodes")
        matchingOpcodes.size
    }.filter { it >= 3 }

    println("\nCounts = $counts")
    println("Part 1 answer is ${counts.size}")
}

fun parseInput(fileName: String): List<TestData> {
    val lines = readFileIntoLines(fileName).toMutableList()
    val regex = """\w+:\s+\[(\d+), (\d+), (\d+), (\d+)\].*""".toRegex()
    val allTestData = mutableListOf<TestData>()
    while (lines.isNotEmpty()) {
        val (b1, b2, b3, b4) = regex.find(lines.removeAt(0))!!.destructured
        val (o1, o2, o3, o4) = """(\d+) (\d+) (\d+) (\d+)""".toRegex().find(lines.removeAt(0))!!.destructured
        val (a1, a2, a3, a4) = regex.find(lines.removeAt(0))!!.destructured

        if (lines.isNotEmpty()) {
            lines.removeAt(0)
        }

        val before = intArrayOf(b1.toInt(), b2.toInt(), b3.toInt(), b4.toInt())
        val ops = intArrayOf(o1.toInt(), o2.toInt(), o3.toInt(), o4.toInt())
        val after = intArrayOf(a1.toInt(), a2.toInt(), a3.toInt(), a4.toInt())

        allTestData.add(TestData(before, ops, after))
    }
    return allTestData
}

data class TestData(val before: IntArray, val operations: IntArray, val after: IntArray) {
    fun matches(opcode: OpCode): Boolean {
        val registers = before.copyOf()
        opcode.operate(registers, operations)
        return registers contentEquals after
    }
}

sealed class OpCode(var name: String) {

    protected val a = 1
    protected val b = 2
    protected val c = 3

    abstract fun operate(registers: IntArray, operation: IntArray)

    override fun toString(): String {
        return name
    }

    /*
    addr (add register) stores into register C the result of adding register A and register B.
    */
    class Addr : OpCode("addr") {
        override fun operate(registers: IntArray, operation: IntArray) {
            registers[operation[c]] = registers[operation[a]] + registers[operation[b]]
        }
    }

    /*
    addi (add immediate) stores into register C the result of adding register A and value B.
     */
    class Addi : OpCode("addi") {
        override fun operate(registers: IntArray, operation: IntArray) {
            registers[operation[c]] = registers[operation[a]] + operation[b]
        }
    }

    /*
    mulr (multiply register) stores into register C the result of multiplying register A and register B.
     */
    class Mulr : OpCode("mulr") {
        override fun operate(registers: IntArray, operation: IntArray) {
            registers[operation[c]] = registers[operation[a]] * registers[operation[b]]
        }
    }

    /*
    muli (multiply immediate) stores into register C the result of multiplying register A and value B.
     */
    class Muli : OpCode("muli") {
        override fun operate(registers: IntArray, operation: IntArray) {
            registers[operation[c]] = registers[operation[a]] * operation[b]
        }
    }

    /*
    banr (bitwise AND register) stores into register C the result of the bitwise AND of register A and register B.
     */
    class Banr : OpCode("banr") {
        override fun operate(registers: IntArray, operation: IntArray) {
            registers[operation[c]] = registers[operation[a]] and registers[operation[b]]
        }
    }

    /*
    bani (bitwise AND immediate) stores into register C the result of the bitwise AND of register A and value B.
     */
    class Bani : OpCode("bani") {
        override fun operate(registers: IntArray, operation: IntArray) {
            registers[operation[c]] = registers[operation[a]] and operation[b]
        }
    }

    /*
    borr (bitwise OR register) stores into register C the result of the bitwise OR of register A and register B.
     */
    class Borr : OpCode("borr") {
        override fun operate(registers: IntArray, operation: IntArray) {
            registers[operation[c]] = registers[operation[a]] or registers[operation[b]]
        }
    }

    /*
    bori (bitwise OR immediate) stores into register C the result of the bitwise OR of register A and value B.
     */
    class Bori : OpCode("bori") {
        override fun operate(registers: IntArray, operation: IntArray) {
            registers[operation[c]] = registers[operation[a]] or operation[b]
        }
    }

    /*
    setr (set register) copies the contents of register A into register C. (Input B is ignored.)
     */
    class Setr : OpCode("setr") {
        override fun operate(registers: IntArray, operation: IntArray) {
            registers[operation[c]] = registers[operation[a]]
        }
    }

    /*
    seti (set immediate) stores value A into register C. (Input B is ignored.)
     */
    class Seti : OpCode("seti") {
        override fun operate(registers: IntArray, operation: IntArray) {
            registers[operation[c]] = operation[a]
        }
    }

    /*
    gtir (greater-than immediate/register) sets register C to 1 if value A is greater than register B.
    Otherwise, register C is set to 0.
     */
    class Gtir : OpCode("gtir") {
        override fun operate(registers: IntArray, operation: IntArray) {
            if (operation[a] > registers[operation[b]]) registers[operation[c]] = 1
            else registers[operation[c]] = 0
        }
    }

    /*
    gtri (greater-than register/immediate) sets register C to 1 if register A is greater than value B.
    Otherwise, register C is set to 0.
     */
    class Gtri : OpCode("gtri") {
        override fun operate(registers: IntArray, operation: IntArray) {
            if (registers[operation[a]] > operation[b]) registers[operation[c]] = 1
            else registers[operation[c]] = 0
        }
    }

    /*
    gtrr (greater-than register/register) sets register C to 1 if register A is greater than register B.
    Otherwise, register C is set to 0.
     */
    class Gtrr : OpCode("gtrr") {
        override fun operate(registers: IntArray, operation: IntArray) {
            if (registers[operation[a]] > registers[operation[b]]) registers[operation[c]] = 1
            else registers[operation[c]] = 0
        }
    }

    /*
    eqir (equal immediate/register) sets register C to 1 if value A is equal to register B.
    Otherwise, register C is set to 0.
     */
    class Eqir : OpCode("eqir") {
        override fun operate(registers: IntArray, operation: IntArray) {
            if (operation[a] == registers[operation[b]]) registers[operation[c]] = 1
            else registers[operation[c]] = 0
        }
    }

    /*
    eqri (equal register/immediate) sets register C to 1 if register A is equal to value B.
    Otherwise, register C is set to 0.
     */
    class Eqri : OpCode("eqri") {
        override fun operate(registers: IntArray, operation: IntArray) {
            if (registers[operation[a]] == operation[b]) registers[operation[c]] = 1
            else registers[operation[c]] = 0
        }
    }

    /*
    eqrr (equal register/register) sets register C to 1 if register A is equal to register B.
    Otherwise, register C is set to 0.
     */
    class Eqrr : OpCode("eqrr") {
        override fun operate(registers: IntArray, operation: IntArray) {
            if (registers[operation[a]] == registers[operation[b]]) registers[operation[c]] = 1
            else registers[operation[c]] = 0
        }
    }
}