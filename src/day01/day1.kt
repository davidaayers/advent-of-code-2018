package day01

import asResource
import parseInt

fun main(args: Array<String>) {
    var frequency = 0
    "/day01/input.txt".asResource { contents ->
        val lines = contents.trim().split("\n")
        lines.forEach { line ->
            frequency += line.parseInt()
        }
    }
    println(frequency)
}