package day14

fun main(args: Array<String>) {
    val input = 37
    val numRecipes = 864801

    val scores = input.digits().toMutableList()
    var elf1Idx = 0
    var elf2Idx = 1

    //printScores(scores, elf1Idx, elf2Idx)
    while (scores.size < numRecipes + 10) {
        val elf1Score = scores[elf1Idx]
        val elf2Score = scores[elf2Idx]

        scores.addAll((elf1Score + elf2Score).digits())
        //printScores(scores, elf1Idx, elf2Idx)

        elf1Idx = scores.move(elf1Idx, elf1Score + 1)
        elf2Idx = scores.move(elf2Idx, elf2Score + 1)
    }

    val finalScore = scores.subList(numRecipes,numRecipes+10).joinToString(separator = "")
    println("Next 10 scores after $numRecipes: $finalScore")

}

fun List<Int>.move(idx: Int, moves: Int): Int {
    var newIdx = idx
    repeat(moves) {
        newIdx++
        if (newIdx >= this.size) {
            newIdx = 0
        }
    }
    return newIdx
}

fun printScores(scores: List<Int>, elf1Idx: Int, elf2Idx: Int) {
    scores.forEachIndexed { idx, score ->
        when (idx) {
            elf1Idx -> print("($score)")
            elf2Idx -> print("[$score]")
            else -> print(" $score ")
        }
    }
    println("")
}

fun Int.digits(): List<Int> {
    return this.toString().chunked(1).map { it.toInt() }
}