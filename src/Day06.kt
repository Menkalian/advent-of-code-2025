fun main() {
    fun part1(input: List<String>): Long {
        val operands = input.dropLast(1)
            .map { it.split("\\s+".toRegex()).dropWhile { it.isEmpty() }.dropLastWhile { it.isEmpty() }.map(String::toLong) }
        val ops = input.last().split("\\s+".toRegex())

        return ops.withIndex().sumOf { (i, op) ->
            if (op == "*") {
                operands.map { it[i] }
                    .reduce { a, b -> a * b }
            } else if (op == "+") {
                operands.sumOf { it[i] }
            } else {
                0L
            }
        }
    }

    fun part2(input: List<String>): Long {
        val opRow = input.last()
        val numRows = input.dropLast(1)

        var sum = 0L
        var operator: String? = null
        val operands = mutableListOf<Long>()

        for (i in input[0].indices) {
            if (numRows.all { it[i] == ' ' } && opRow[i] == ' ' ) {
                val logstr = StringBuilder()
                logstr.append(operands.joinToString(separator = " $operator ") { it.toString() })
                val intermediate_result = if (operator == "*") {
                    operands.reduce { a, b -> a * b }
                } else if (operator == "+") {
                    operands.sum()
                } else {
                    0L
                }
                logstr.append(" = ")
                logstr.append(intermediate_result)
                println(logstr.toString())
                sum += intermediate_result

                operator = null
                operands.clear()
            } else {
                operands.add(
                    numRows.map { it[i] }
                        .filter { it != ' ' }
                        .joinToString(separator = "")
                        .toLong()
                )
                if (operator == null && opRow[i] != ' ') {
                    operator = opRow[i].toString()
                }
            }
        }
        val logstr = StringBuilder()
        logstr.append(operands.joinToString(separator = " $operator ") { it.toString() })
        val intermediate_result = if (operator == "*") {
            operands.reduce { a, b -> a * b }
        } else if (operator == "+") {
            operands.sum()
        } else {
            0L
        }
        logstr.append(" = ")
        logstr.append(intermediate_result)
        println(logstr.toString())
        sum += intermediate_result

        return sum
    }

    val testInput = readInput("Day06_test")
    if (testInput.isNotEmpty()) {
        check(part1(testInput), 4277556L)
        check(part2(testInput), 3263827L)
    }

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}