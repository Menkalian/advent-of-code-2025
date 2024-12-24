fun main() {
    fun Array<Long?>.checksum(): Long {
        return this
            .withIndex()
            .sumOf { it.value?.let { value -> value * it.index } ?: 0 }
    }

    fun part1(input: List<String>): Long {
        val inputNumbers = input.first().toCharArray().map { it.digitToInt().toLong() }
        val reqSize = inputNumbers.sum()
        val memory = Array<Long?>(reqSize.toInt(), { null })
        var sectorID = 0L
        var memoryIndex = 0
        inputNumbers.forEachIndexed { index, digit ->
            if (index % 2 == 0) {
                for (i in 1..digit) {
                    memory[memoryIndex] = sectorID
                    memoryIndex++
                }
                sectorID++
            } else {
                memoryIndex += digit.toInt()
            }
        }

        // Partition
        while (memory.indexOfFirst { it == null } < memory.indexOfLast { it != null }) {
            val lastBlock = memory.indexOfLast { it != null }
            val firstSpace = memory.indexOfFirst { it == null }
            memory[firstSpace] = memory[lastBlock]
            memory[lastBlock] = null
        }

        return memory.checksum()
    }

    fun part2(input: List<String>): Long {
        val inputNumbers = input.first().toCharArray().map { it.digitToInt().toLong() }
            .mapIndexed { index, l -> (index % 2 == 0) to (index to l) }
        var workCopy = inputNumbers.toList()
        val reqSize = inputNumbers.sumOf { it.second.second }
        val memory = Array<Long?>(reqSize.toInt(), { null })

        // Defragment
        inputNumbers.reversed()
            .filter { it.first }
            .forEachIndexed { moveBlockIdx, moveBlock ->
                val targetBlock = workCopy.firstOrNull {
                    !it.first
                            && it.second.first < moveBlock.second.first
                            && it.second.second >= moveBlock.second.second
                }
                if (targetBlock != null) {
                    workCopy = workCopy.flatMap {
                        if (it.second.first == targetBlock.second.first) {
                            if (moveBlock.second.second == targetBlock.second.second) {
                                listOf(moveBlock)
                            } else {
                                listOf(
                                    moveBlock,
                                    targetBlock.first to (targetBlock.second.first to (targetBlock.second.second - moveBlock.second.second))
                                )
                            }
                        } else if (it.second.first == moveBlock.second.first) {
                            // Just put some blank space there.
                            listOf(
                                false to (Int.MAX_VALUE to moveBlock.second.second),
                            )
                        } else {
                            // Keep unchanged.
                            listOf(it)
                        }
                    }
                }
            }

        // Build actual memory out of it.
        var memoryIndex = 0
        workCopy.forEachIndexed { index, block ->
            if (block.first) {
                val sectorID = block.second.first / 2
                for (i in 1..block.second.second) {
                    memory[memoryIndex] = sectorID.toLong()
                    memoryIndex++
                }
            } else {
                memoryIndex += block.second.second.toInt()
            }
        }

        return memory.checksum()
    }

    val testInput = readInput("Day09_test")
    if (testInput.isNotEmpty()) {
        check(part1(testInput), 1928L)
        check(part2(testInput), 2858L)
    }

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}