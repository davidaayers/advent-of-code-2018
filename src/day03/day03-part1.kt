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
    val lines = readFileIntoLines("/day03/small-input.txt")
    val claims = lines.map {
        claim {
            input(it)
        }
    }
    claims.forEach { println(it) }
}