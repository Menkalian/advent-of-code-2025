fun main() {
    fun part1(input: List<String>): Long {
        val graph = input.map {
            it.split(" ")
        }.associate {
            it[0].removeSuffix(":") to it.drop(1).toSet()
        }

        fun iterateThroughGraph(location: String = "you"): Long {
            val nextNodes = graph.getOrElse(location) { emptySet() }
            val subPathExploration = nextNodes.filter { it != "out" }
                .map { iterateThroughGraph(it) }
                .sum()
            if ("out" in nextNodes) {
                return subPathExploration + 1
            } else {
                return subPathExploration
            }
        }

        return iterateThroughGraph()
    }

    fun part2(input: List<String>): Long {
        val graph = input.map {
            it.split(" ")
        }.associate {
            it[0].removeSuffix(":") to it.drop(1).toSet()
        }
        val cache = mutableMapOf<Triple<String, Boolean, Boolean>, Long>()

        fun iterateThroughGraph(location: String = "svr", goal: String = "out", history: Set<String> = emptySet()): Long {
            if (location in history) {
                return 0L // Ignore cycles
            }
            val cacheKey = Triple(location, "dac" in history, "fft" in history)
            if (cacheKey in cache) {
                return cache[cacheKey]!!
            }
            val nextNodes = graph.getOrElse(location) { emptySet() }
            val subPathExploration = nextNodes.filter { it != goal }
                .map { iterateThroughGraph(it, goal, history + location) }
                .sum()
            val result = if (goal in nextNodes && "dac" in history && "fft" in history) {
                subPathExploration + 1
            } else {
                subPathExploration
            }
            cache[cacheKey] = result
            return result
        }

        return iterateThroughGraph()
    }

    val testInput = readInput("Day11_test")
    val testInput2 = readInput("Day11_test2")
    if (testInput.isNotEmpty()) {
        check(part1(testInput), 5L)
        check(part2(testInput2), 2L)
    }

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}