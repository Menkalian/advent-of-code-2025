import kotlin.math.pow

fun main() {
    fun getMaxJoltage(batteries: List<Int>): Long {
        val tenDigit = batteries.dropLast(1).max()
        val tenIndex = batteries.indexOf(tenDigit)
        val onesDigit = batteries.drop(tenIndex + 1).max()
        return tenDigit * 10L + onesDigit
    }

    fun getUnlimitedJoltage(batteries: List<Int>, requiredNums: Int = 12): Long {
        if (requiredNums <= 1) {
            return batteries.max().toLong()
        }
        val digit = batteries.dropLast(requiredNums - 1).max()
        val index = batteries.indexOf(digit)
        val subsequentNumber = getUnlimitedJoltage(batteries.drop(index + 1), requiredNums - 1)
        return digit * 10.0.pow(requiredNums - 1).toLong() + subsequentNumber
    }

    fun part1(input: List<String>): Long {
        return input
            .map { it.toCharArray().map { it.digitToInt() } }
            .sumOf { getMaxJoltage(it) }
    }

    fun part2(input: List<String>): Long {
        return input
            .map { it.toCharArray().map { it.digitToInt() } }
            .sumOf { getUnlimitedJoltage(it) }
    }

    val testInput = readInput("Day03_test")
    if (testInput.isNotEmpty()) {
        check(part1(testInput), 357L)
        check(part2(testInput), 3121910778619L)
    }

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}