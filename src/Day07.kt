fun main() {
    fun part1(input: List<String>): Long {
        var state = input.first().toCharArray().map { it == 'S' }
        val propagationRows = input.drop(1)

        var splitCount = 0L

        for (i in propagationRows.indices) {
            val prevState = state
            state = propagationRows[i].indices.map {
                if (propagationRows[i][it] == '^') {
                    if (prevState[it]) {
                        splitCount++
                    }

                    false
                } else {
                    prevState[it]
                            || (propagationRows[i].getOrElse(it - 1) { '.' } == '^' && prevState[it - 1])
                            || (propagationRows[i].getOrElse(it + 1) { '.' } == '^' && prevState[it + 1])
                }
            }
            //println(state.indices.joinToString("") { if (state[it]) "|" else propagationRows[i][it].toString() })
        }

        return splitCount
    }

    fun part2(input: List<String>): Long {
        var state = input.first().toCharArray().map { if (it == 'S') 1L else 0L }
        val propagationRows = input.drop(1)

        for (i in propagationRows.indices) {
            val prevState = state
            state = propagationRows[i].indices.map {
                if (propagationRows[i][it] == '^') {
                    0L
                } else {
                    prevState[it] +
                            if (propagationRows[i].getOrElse(it - 1) { '.' } == '^') {
                                prevState[it - 1]
                            } else {
                                0L
                            } +
                            if (propagationRows[i].getOrElse(it + 1) { '.' } == '^') {
                                prevState[it + 1]
                            } else {
                                0L
                            }
                }
            }
            println(state.indices.joinToString("") { if (propagationRows[i][it] == '^') "^" else state[it].toString() })
        }

        return state.sum()
    }

    val testInput = readInput("Day07_test")
    if (testInput.isNotEmpty()) {
        check(part1(testInput), 21L)
        check(part2(testInput), 40L)
    }

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}