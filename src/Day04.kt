fun main() {
    fun List<List<Boolean>>.safeGet(r: Int, c: Int): Boolean {
        return this.getOrNull(r)?.getOrNull(c) ?: false
    }

    fun List<List<Boolean>>.countAdjacent(r: Int, c: Int): Int {
        val grid = this
        return buildList {
            add(grid.safeGet(r-1,c-1))
            add(grid.safeGet(r-1,c))
            add(grid.safeGet(r-1,c+1))
            add(grid.safeGet(r,c-1))
            add(grid.safeGet(r,c+1))
            add(grid.safeGet(r+1,c-1))
            add(grid.safeGet(r+1,c))
            add(grid.safeGet(r+1,c+1))
        }.count { it }
    }

    fun part1(input: List<String>): Long {
        val grid = input.map { it.toCharArray().map { if (it == '@') true else false } }
        var movableCount = 0L
        for ((r, row) in grid.withIndex()) {
            for ((c, element) in row.withIndex()) {
                if (element && grid.countAdjacent(r, c) < 4) {
                    movableCount += 1
                }
            }
        }
        return movableCount
    }

    fun part2(input: List<String>): Long {
        val grid = input.map { it.toCharArray().map { if (it == '@') true else false }.toMutableList() }.toMutableList()
        var movableCount = 0L
        var lastRoundCount = -1
        while (lastRoundCount != 0) {
            lastRoundCount = 0
            val overrides = mutableListOf<Pair<Int, Int>>()
            for ((r, row) in grid.withIndex()) {
                for ((c, element) in row.withIndex()) {
                    if (element && grid.countAdjacent(r, c) < 4) {
                        movableCount += 1
                        lastRoundCount += 1
                        overrides += Pair(r,c)
                    }
                }
            }
            for ((r,c) in overrides) {
                grid[r][c] = false
            }
        }
        return movableCount
    }

    val testInput = readInput("Day04_test")
    if (testInput.isNotEmpty()) {
        check(part1(testInput), 13L)
        check(part2(testInput), 43L)
    }

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}