package day11

import java.lang.IllegalStateException

fun examples() {
    test(Grid(57,1,1).cellValue(122,79) == -5)
    test(Grid(39,1,1).cellValue(217,196) == 0)
    test(Grid(71,1,1).cellValue(101,153) == 4)
    test(Grid(18,1,1).cellValue(33,45) == 4)
    test(Grid(18,1,1).cellValue(34,45) == 4)
    test(Grid(18,1,1).cellValue(35,45) == 4)
    test(Grid(18,1,1).cellValue(33,46) == 3)
    test(Grid(18,1,1).cellValue(34,46) == 3)
    test(Grid(18,1,1).cellValue(35,46) == 4)
}

fun test(bool: Boolean) {
    if(!bool) throw IllegalStateException()
}

