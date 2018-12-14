package day14

fun main(args: Array<String>) {
    val input = 37
    val puzzleInput = 864801
    val searchStr = puzzleInput.digits().joinToString(separator = "")

    println("searchStr = $searchStr")

    val scores = input.digits().toMutableList()
    var elf1Idx = 0
    var elf2Idx = 1

    while (searchStr !in (scores.takeLast(10).joinToString(separator = ""))) {
        val elf1Score = scores[elf1Idx]
        val elf2Score = scores[elf2Idx]

        scores.addAll((elf1Score + elf2Score).digits())
        //printScores(scores, elf1Idx, elf2Idx)

        elf1Idx = scores.move(elf1Idx, elf1Score + 1)
        elf2Idx = scores.move(elf2Idx, elf2Score + 1)
    }

    val actualIndex = scores.joinToString(separator = "").indexOf(searchStr)
    println("Part 2 answer: $actualIndex ")
}