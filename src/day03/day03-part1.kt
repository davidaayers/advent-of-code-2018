package day03

import readFileIntoLines

data class Claim private constructor(val claimId: Int, val x: Int, val y: Int, val width: Int, val height: Int) {

    private constructor(builder: Builder) : this(
        builder.claimId,
        builder.x,
        builder.y,
        builder.width,
        builder.height
    )

    class Builder {
        var claimId: Int = 0
        var x: Int = 0
        var y: Int = 0
        var width: Int = 0
        var height: Int = 0

        fun input(input: String) {
            val regex = """#(\d+) @ (\d+),(\d+): (\d+)x(\d+)""".toRegex()
            val matchResult = regex.find(input)
            val (claimId, x, y, width, height) = matchResult!!.destructured
            this.claimId = claimId.toInt()
            this.x = x.toInt()
            this.y = y.toInt()
            this.width = width.toInt()
            this.height = height.toInt()
        }

        fun build() = Claim(this)
    }
}

fun claim(block: Claim.Builder.() -> Unit): Claim =
    Claim.Builder().apply(block).build()

fun main(args: Array<String>) {
    //countOverlaps(readFileIntoLines("/day03/small-input.txt"), 20, 20)
    countOverlaps(readFileIntoLines("/day03/input.txt"), 1000, 1000)
}

private fun countOverlaps(lines: List<String>, maxX: Int, maxY: Int) {
    val claims = lines.map {
        claim {
            input(it)
        }
    }

    // create our "cloth" array
    val cloth = Array(maxX) { Array(maxY) { 0 } }

    // go through out claims and add them to the list of claims for each square "inch"
    claims.forEach { claim ->
        println("Examining claim: $claim")
        for (x in claim.x until claim.x + claim.width) {
            for (y in claim.y until claim.y + claim.height) {
                cloth[x][y]++
            }
        }
    }

    for (x in 0 until maxX) {
        for (y in 0 until maxY) {
            print("[${if (cloth[x][y] == 0) " " else cloth[x][y]}]")
        }
        println("")
    }

    val numOverlapInches = cloth.flatMap { it.asList() }.count { it > 1 }
    println("numOverlapInches = $numOverlapInches")
}