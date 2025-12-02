fun main() {
    fun invalidId1(id: Long) : Boolean{
        val str = id.toString()
        if (str.length % 2 != 0) {
            return false
        }
        return str.take(str.length / 2) == str.takeLast(str.length / 2)
    }

    fun invalidId2(id: Long) : Boolean{
        val str = id.toString()
        for (patternLength in 1..(str.length/2)) {
            if (str.length % patternLength == 0) {
                val chunked = str.chunked(patternLength)
                if (chunked.all { it == chunked[0] }) {
                    return true
                }
            }
        }
        return false
    }

    fun part1(input: List<String>): Long {
        return input.first()
            .split(",")
            .map { it.split("-") }
            .flatMap { (it[0].toLong()..it[1].toLong()).toList() }
            .filter { invalidId1(it) }
            .sum()
    }

    fun part2(input: List<String>): Long {
        return input.first()
            .split(",")
            .map { it.split("-") }
            .flatMap { (it[0].toLong()..it[1].toLong()).toList() }
            .filter { invalidId2(it) }
            .sum()
    }

    val testInput = readInput("Day02_test")
    if (testInput.isNotEmpty()) {
        check(part1(testInput), 1227775554L)
        check(part2(testInput), 4174379265L)
    }

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}