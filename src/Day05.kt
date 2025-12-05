import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Long {
        val splitpoint = input.indexOf("")
        val freshIdRange = input.subList(0, splitpoint)
            .map { it.split("-").let { it[0].toLong() .. it[1].toLong() } }
        val ingredients = input.subList(splitpoint + 1, input.size).map { it.toLong() }
        return ingredients.count { id -> freshIdRange.any { range -> id in range } }.toLong()
    }

    fun part2(input: List<String>): Long {
        val splitpoint = input.indexOf("")
        val freshIdRange = input.subList(0, splitpoint)
            .map { it.split("-").let { it[0].toLong() .. it[1].toLong() } }

        val cutRanges = mutableListOf<LongRange>()
        for (range in freshIdRange) {
            var newRange = range
            val trm = mutableListOf<LongRange>()
            for (procRange in cutRanges) {
                if (newRange.start in procRange) {
                    trm.add(procRange)
                    newRange = procRange.start .. max(procRange.last, newRange.last)
                } else if (newRange.last in procRange) {
                    trm.add(procRange)
                    newRange = min(procRange.start, newRange.start)..procRange.last
                } else if (procRange.start in newRange) {
                    // fully enclosed
                    trm.add(procRange)
                }
            }
            cutRanges.removeAll(trm)
            cutRanges.add(newRange)
        }

        return cutRanges.sumOf { range -> range.last - range.first + 1 }
    }

    val testInput = readInput("Day05_test")
    if (testInput.isNotEmpty()) {
        check(part1(testInput), 3L)
        check(part2(testInput), 14L)
    }

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}