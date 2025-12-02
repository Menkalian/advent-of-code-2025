import kotlin.math.absoluteValue
import kotlin.math.sign

fun main() {
    fun Int.rotate(amount: Int): Int {
        var target = this + amount
        while (target > 99) {
            target -= 100
        }
        while (target < 0) {
            target += 100
        }
        return target
    }
    fun Int.rotateAndCount(amount: Int): Pair<Int, Int> {
        var target = this
        val modifier = amount.sign
        var passings = 0

        for (i in 1..amount.absoluteValue) {
            target += modifier
            if (target == 100) {
                target = 0
            }
            if (target == -1) {
                target = 99
            }
            if (target == 0) {
                passings += 1
            }
        }

        return Pair(target, passings)
    }

    fun part1(input: List<String>): Long {
        var location = 50
        val rotations = input
            .map { (if (it[0] == 'L') -1 else +1) * it.substring(1).toInt() }
        var zeroes = 0L

        for (rot in rotations) {
            location = location.rotate(rot)
            if (location == 0) {
                zeroes++
            }
        }

        return zeroes
    }

    fun part2(input: List<String>): Long {
        var location = 50
        val rotations = input
            .map { (if (it[0] == 'L') -1 else +1) * it.substring(1).toInt() }
        var zeroes = 0L

        for (rot in rotations) {
            val (newLocation, passings) = location.rotateAndCount(rot)
            location = newLocation
            zeroes += passings
        }

        return zeroes
    }

    val testInput = readInput("Day01_test")
    if (testInput.isNotEmpty()) {
        check(part1(testInput), 3L)
        check(part2(testInput), 6L)
    }

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}