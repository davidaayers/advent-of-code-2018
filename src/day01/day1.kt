package day01

fun String.asResource(work: (String) -> Unit) {
    val content = object {}.javaClass::class.java.getResource(this).readText()
    work(content)
}

fun main(args: Array<String>) {
    var frequency = 0
    "/day01/input.txt".asResource { contents ->
        val lines = contents.trim().split("\n")
        lines.forEach { line ->
            println(line)
            frequency += if(line.startsWith("+")) {
                line.substring(1).toInt()
            } else {
                line.toInt()
            }
        }
    }
    println(frequency)
}