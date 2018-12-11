package day11

import java.lang.IllegalStateException

fun examples() {
    var grid = Grid(57)
    test(grid.cellValue(122,79) == -5)
    grid = Grid(39)
    test(grid.cellValue(217,196) == 0)
    grid = Grid(71)
    test(grid.cellValue(101,153) == 4)
    grid = Grid(18)
    test(grid.cellValue(33,45) == 4)
    test(grid.cellValue(34,45) == 4)
    test(grid.cellValue(35,45) == 4)
    test(grid.cellValue(33,46) == 3)
    test(grid.cellValue(34,46) == 3)
    test(grid.cellValue(35,46) == 4)
}

fun test(bool: Boolean) {
    if(!bool) throw IllegalStateException()
}

