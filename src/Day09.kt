import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

fun main() {
    fun List<Pair<Long, Long>>.removeContainedPoints(p1: Pair<Long, Long>, p2: Pair<Long, Long>): List<Pair<Long, Long>> {
        val xMin = min(p1.first, p2.first)
        val xMax = max(p1.first, p2.first)
        val yMin = min(p1.second, p2.second)
        val yMax = max(p1.second, p2.second)

        val xRange = xMin + 1 until xMax
        val yRange = yMin + 1 until yMax

        return this
            .filter { (it.first in xRange && it.second in yRange).not() }
    }

    fun area(p1: Pair<Long, Long>, p2: Pair<Long, Long>): Long {
        return ((p1.first - p2.first).absoluteValue + 1) * ((p1.second - p2.second).absoluteValue + 1)
    }

    fun part1(input: List<String>): Long {
        val points = input.map { it.split(",").map { it.toLong() } }
            .map { it[0] to it[1] }
        val xMin = points.minOf { it.first }
        val xMax = points.maxOf { it.first }
        val yMin = points.minOf { it.second }
        val yMax = points.maxOf { it.second }

        // Eliminate some points... (although largely pointless...)
        val filteredPoints = points
            .removeContainedPoints(
                points.filter { it.first == xMin }.minBy { it.second },
                points.filter { it.first == xMax }.maxBy { it.second }
            )
            .removeContainedPoints(
                points.filter { it.first == xMin }.maxBy { it.second },
                points.filter { it.first == xMax }.minBy { it.second }
            )
            .removeContainedPoints(
                points.filter { it.second == yMin }.maxBy { it.first },
                points.filter { it.second == yMax }.minBy { it.first }
            )
            .removeContainedPoints(
                points.filter { it.second == yMin }.maxBy { it.first },
                points.filter { it.second == yMax }.minBy { it.first }
            )

        return filteredPoints.indices
            .flatMap { i1 -> (i1 + 1..filteredPoints.indices.last).map { i2 -> area(filteredPoints[i1], filteredPoints[i2]) } }
            .max()
    }

    fun part2(input: List<String>): Long {
        fun intersect(r1: Pair<Pair<Long, Long>, Pair<Long, Long>>, r2: Pair<Pair<Long, Long>, Pair<Long, Long>>): Boolean {
            val r1XMin = min(r1.first.first, r1.second.first)
            val r1XMax = max(r1.first.first, r1.second.first)
            val r1YMin = min(r1.first.second, r1.second.second)
            val r1YMax = max(r1.first.second, r1.second.second)

            val r2XMin = min(r2.first.first, r2.second.first)
            val r2XMax = max(r2.first.first, r2.second.first)
            val r2YMin = min(r2.first.second, r2.second.second)
            val r2YMax = max(r2.first.second, r2.second.second)

            val fullyOutside = r2XMin > r1XMax || r1XMin > r2XMax
                    || r2YMin > r1YMax || r1YMin > r2YMax
            return !fullyOutside
        }

        fun isValid(points: List<Pair<Long, Long>>, lines: List<Pair<Pair<Long, Long>, Pair<Long, Long>>>, p1: Pair<Long, Long>, p2: Pair<Long, Long>): Boolean {
            val xMin = min(p1.first, p2.first)
            val xMax = max(p1.first, p2.first)
            val yMin = min(p1.second, p2.second)
            val yMax = max(p1.second, p2.second)

            val innerXRange = xMin + 1 until xMax
            val innerYRange = yMin + 1 until yMax

            return points.none { (x, y) ->
                x in innerXRange && y in innerYRange
            }
                    && points.any { (x, y) -> x <= xMin && y <= yMin }
                    && points.any { (x, y) -> x >= xMax && y <= yMin }
                    && points.any { (x, y) -> x <= xMin && y >= yMax }
                    && points.any { (x, y) -> x >= xMax && y >= yMax }
                    && lines.none { intersect(it, (xMin+1 to yMin+1) to (xMax-1 to yMax-1)) }

        }

        val points = input.map { it.split(",").map { it.toLong() } }
            .map { it[0] to it[1] }
        val lines = points.windowed(2).map { it[0] to it[1] } + listOf(points.first() to points.last())

        return points.indices
            .flatMap { i1 -> (i1 + 1..points.indices.last).map { i2 -> points[i1] to points[i2] } }
            .filter { (p1, p2) -> isValid(points, lines, p1, p2) }
            .maxOf { (p1, p2) -> area(p1, p2) }
    }

    val testInput = readInput("Day09_test")
    if (testInput.isNotEmpty()) {
        check(part1(testInput), 50L)
        check(part2(testInput), 24L)
    }

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}