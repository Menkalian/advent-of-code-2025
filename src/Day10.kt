import java.lang.Math.pow
import kotlin.collections.foldIndexed
import kotlin.io.path.absolutePathString
import kotlin.io.path.createTempFile
import kotlin.io.path.writeText

fun main() {
    fun part1(input: List<String>): Long {
        return input
            .sumOf {
                val manual = it.split(" ")
                val targetState = manual[0].toCharArray()
                    .drop(1)
                    .dropLast(1)
                    .map { it == '#' }
                    .foldIndexed(0L) { idx, acc, value -> acc or ((if (value) 1L else 0L) shl idx) }
                val buttons = manual.subList(1, manual.size - 1)
                    .map {
                        it.substring(1, it.length - 1)
                            .split(",")
                            .map { it.toLong() }
                            .fold(0L) { acc, i -> acc or (1L shl i.toInt()) }
                    }
                //val joltage = manual.last()

                val configurations = 0L until pow(2.0, buttons.size.toDouble()).toLong()

                configurations
                    .filter { cfg ->
                        var sum = 0L
                        for (i in buttons.indices) {
                            if (cfg and (1L shl i) != 0L) {
                                sum = sum xor buttons[i]
                            }
                        }
                        sum == targetState
                    }
                    .minOf { it.toString(2).count { it == '1' }.toLong() }
            }
    }

    fun part2(input: List<String>): Long {
        val regex = "Objective value = (\\d+)\\.0".toRegex()
        return input
            .sumOf {
                val manual = it.split(" ")
                val joltage = manual.last()
                    .drop(1).dropLast(1)
                    .split(",")
                    .map { it.toLong() }
                    .toTypedArray() // [y]
                val buttons = manual.subList(1, manual.size - 1)
                    .map {
                        it.substring(1, it.length - 1)
                            .split(",")
                            .map { it.toInt() }
                            .fold(LongArray(joltage.size)) { acc, value -> acc[value] = 1L; acc }
                    }.toTypedArray() // [x][y]

                val juliaTemplate = """
                    using JuMP
                    using HiGHS
                    
                    btns = [
                        %s
                    ]'
                    
                    target = [%s]
                    
                    m = Model(HiGHS.Optimizer)
                    
                    @variable(m, x[1:size(btns,2)] >= 0, Int)
                    @constraint(
                            m,
                            btns * x .== target
                           )
                    @objective(m, Min, sum(x))

                    optimize!(m)

                    println("Objective value = ", objective_value(m))
                    println("Solution:")
                    for i in 1:length(x)
                        println("  x[${'$'}i] = ", value(x[i]))
                    end
                """.trimIndent().format(
                    buttons.joinToString(";\n") { it.joinToString(" ")},
                    joltage.joinToString(",")
                )

                val tmpFile = createTempFile(suffix = ".jl")
                tmpFile.writeText(juliaTemplate)

                val subprocess = ProcessBuilder("julia", tmpFile.absolutePathString())
                    .start()
                subprocess.waitFor()
                val output = subprocess.inputReader().readText()
                val match = regex.find(output)
                if (match == null) {
                    println(output)
                    throw RuntimeException()
                }
                match.groupValues[1].toLong()
            }
    }

    val testInput = readInput("Day10_test")
    if (testInput.isNotEmpty()) {
        check(part1(testInput), 7L)
        check(part2(testInput), 33L)
    }

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}