package day11

fun main(args: Array<String>) {
    examples()

    val serialNumber = 9005
    val allPossibleGrids = mutableListOf<Grid>()
    for (y in 1..198) {
        for (x in 1..298) {
            allPossibleGrids.add(Grid(serialNumber, x, y))
        }
    }

    val highestGrid = allPossibleGrids.maxBy { it.value() }!!
    println("highestGrid = $highestGrid, power = ${highestGrid.value()}")
}

data class Grid(val serialNumber: Int, val startX: Int, val startY: Int) {
    fun value(): Int {
        var value = 0
        for (y in startY until startY + 3) {
            for (x in startX until startX + 3) {
                value += cellValue(x, y)
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

