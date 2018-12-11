package day11

fun main(args: Array<String>) {
    examples()
    part1()
    part2()
}

private fun part1() {
    val serialNumber = 9005
    var winningX = 0
    var winningY = 0
    var winningValue = 0
    val grid = Grid(serialNumber)

    for (y in 1..298) {
        for (x in 1..298) {
            val value = grid.value(x, y)
            if (value > winningValue) {
                winningValue = value
                winningX = x
                winningY = y
            }
        }
    }

    println("winning grid for part 1 is $winningX,$winningY with power of $winningValue")
}


private fun part2() {
    val serialNumber = 9005
    var winningX = 0
    var winningY = 0
    var winningValue = 0
    var winningSize = 0
    val grid = Grid(serialNumber)

    var size = 2
    while (size < 298) {
        for (y in 1..300 - size) {
            for (x in 1..300 - size) {
                val value = grid.value(x, y, size)
                if (value > winningValue) {
                    winningValue = value
                    winningX = x
                    winningY = y
                    winningSize = size
                }
            }
        }
        size++
        println("size = ${size}")
    }
    println("winning grid for part 2 is $winningX,$winningY with power of $winningValue and a size of $winningSize")
}

data class Grid(val serialNumber: Int) {

    private val grid = Array(302) { Array(302) { 0 } }

    init {
        // build grid
        for (y in 1..300) {
            for (x in 1..300) {
                grid[y][x] = cellValue(x, y)
            }
        }
    }

    fun value(startY: Int, startX: Int, size: Int = 3): Int {
        var value = 0
        for (y in startY until startY + size) {
            for (x in startX until startX + size) {
                value += grid[x][y]
            }
        }
        return value
    }

    fun cellValue(x: Int, y: Int): Int {
        val rackId = x + 10
        var powerLevel = rackId * y
        powerLevel += serialNumber
        powerLevel *= rackId
        powerLevel = if (powerLevel > 100) {
            val parts = powerLevel.toString().toCharArray().map { it.toString() }
            val partsReversed = parts.reversed()
            val thirdDigit = partsReversed[2]
            thirdDigit.toInt()
        } else {
            0
        }
        powerLevel -= 5

        return powerLevel
    }
}



