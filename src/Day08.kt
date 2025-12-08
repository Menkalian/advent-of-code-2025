import kotlin.math.sqrt

data class Day08Point(
    val id: Int,
    val x: Long,
    val y: Long,
    val z: Long
) {
    operator fun rangeTo(other: Day08Point): Double {
        return sqrt(
            (
                    squareDistance(other)
                    ).toDouble()
        )
    }

    private fun squareDistance(other: Day08Point): Long {
        val result = (this.x - other.x) * (this.x - other.x) +
                (this.y - other.y) * (this.y - other.y) +
                (this.z - other.z) * (this.z - other.z)
        assert(result > 0)
        return result
    }
}

fun Array<DoubleArray>.lowestNElements(n: Int): List<Pair<Int, Int>> {
    return this
        .withIndex()
        .flatMap { (i, value) -> value.withIndex().map { (j, distance) -> (i to j) to distance } }
        .sortedBy { it.second }
        .take(n)
        .map { it.first }
}

fun main() {
    fun part1(input: List<String>, connections: Int = 1000): Long {
        val points = input
            .withIndex()
            .map { (i, str) ->
                val split = str.split(",")
                    .map { it.toLong() }
                Day08Point(i, split[0], split[1], split[2])
            }

        // Precompute distances (should be feasible with given input size)
        val distanceGrid = Array(points.size) { DoubleArray(points.size) { Double.MAX_VALUE } }
        for ((i, A) in points.withIndex()) {
            for (B in points.drop(i + 1)) {
                distanceGrid[A.id][B.id] = A..B
            }
        }

        val connections = distanceGrid.lowestNElements(connections)
        val circuits = points.map { setOf(it.id) }.toMutableList()

        for (connection in connections) {
            val i = connection.first
            val j = connection.second
            val individuals = circuits.filter { i in it || j in it }
            if (individuals.size > 1) {
                circuits.removeAll(individuals)
                circuits.add(individuals.reduce { a, b -> a + b })
            }
        }

        return circuits.sortedByDescending { it.size }
            .take(3)
            .map { it.size.toLong() }
            .reduce { a, b -> a * b }
    }

    fun part2(input: List<String>): Long {
        val points = input
            .withIndex()
            .map { (i, str) ->
                val split = str.split(",")
                    .map { it.toLong() }
                Day08Point(i, split[0], split[1], split[2])
            }

        // Precompute distances (should be feasible with given input size)
        val distanceGrid = Array(points.size) { DoubleArray(points.size) { Double.MAX_VALUE } }
        for ((i, A) in points.withIndex()) {
            for (B in points.drop(i + 1)) {
                distanceGrid[A.id][B.id] = A..B
            }
        }

        val connections = distanceGrid
            .withIndex()
            .flatMap { (i, value) -> value.withIndex().map { (j, distance) -> (i to j) to distance } }
            .sortedBy { it.second }
            .map { it.first }
        val circuits = points.map { setOf(it.id) }.toMutableList()

        for (connection in connections) {
            val i = connection.first
            val j = connection.second

            val individuals = circuits.filter { i in it || j in it }
            if (individuals.size > 1) {
                circuits.removeAll(individuals)
                circuits.add(individuals.reduce { a, b -> a + b })
            }

            if (circuits.size == 1) {
                return points[i].x * points[j].x
            }
        }

        return 0L
    }

    val testInput = readInput("Day08_test")
    if (testInput.isNotEmpty()) {
        check(part1(testInput, 10), 40L)
        check(part2(testInput), 25272L)
    }

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}