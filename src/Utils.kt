fun String.asResource(work: (String) -> Unit) {
    work(readEntireFile(this))
}

fun readEntireFile(fileName: String) =
    fileName.javaClass::class.java.getResource(fileName).readText()

fun String.parseInt() = if(this.startsWith("+")) {
    this.substring(1).toInt()
} else {
    this.toInt()
}

