fun String.asResource(work: (String) -> Unit) {
    work(readEntireFile(this))
}

fun readEntireFile(fileName: String) =
    readEntireFileNoTrim(fileName).trim()

fun readEntireFileNoTrim(fileName: String) =
    fileName.javaClass::class.java.getResource(fileName).readText()

fun readFileIntoLines(fileName: String): List<String> =
    readEntireFile(fileName).split("\n")

fun String.parseInt() = if(this.startsWith("+")) {
    this.substring(1).toInt()
} else {
    this.toInt()
}

fun String.chunked(size: Int): List<String> {
    val nChunks = length / size
    return (0 until nChunks).map { substring(it * size, (it + 1) * size) }
}