package day06

fun main(args: Array<String>) {
    val (points, grid) = readPuzzleInput("/day06/input.txt")

    printGrid(grid)
    println("------")

    for (y in 0 until grid.size) {
        for (x in 0 until grid.size) {
            val sumOfDistances = points.map {
                Distance(it, manhattanDistance(it.point.first, x, it.point.second, y))
            }.toList()
                .sumBy { it.distance }

            println("$x,$y sumOfDistances = $sumOfDistances")
            if (sumOfDistances < 10000) {
                grid[y][x] = '#'
            }
        }
    }

    printGrid(grid)
    println("------")

    val hashesOnMap = grid.flatMap { it.asList() }
        .filter { it == '#' }
        .count()

    println("Answer is $hashesOnMap")

}