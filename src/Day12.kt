data class Shape(
    val appearance: Array<Array<Boolean>>,
) {
    val occupiesCount: Int = appearance.sumOf { it.count { it } }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Shape

        if (!appearance.contentDeepEquals(other.appearance)) return false

        return true
    }

    override fun hashCode(): Int {
        return appearance.contentDeepHashCode()
    }

    fun flipped(): Shape {
        return Shape(appearance.map { it.reversed().toTypedArray() }.toTypedArray())
    }

    fun rotated(): Shape {
        return Shape(
            arrayOf(
                arrayOf(appearance[0][0], appearance[1][0], appearance[2][0]),
                arrayOf(appearance[0][1], appearance[1][1], appearance[2][1]),
                arrayOf(appearance[0][2], appearance[1][2], appearance[2][2]),
            )
        )
    }

    fun allVariants(): List<Shape> {
        val flipped = flipped()
        return listOf(
            this,
            this.rotated(),
            this.rotated().rotated(),
            this.rotated().rotated().rotated(),
            flipped,
            flipped.rotated(),
            flipped.rotated().rotated(),
            flipped.rotated().rotated().rotated(),
        )
    }

    fun fitsAt(space: Array<BooleanArray>, x: Int, y: Int): Boolean {
        try {
            return appearance.indices.all { y1 ->
                appearance[y1].indices.all { x1 ->
                    !appearance[y1][x1] || !space[y + y1][x + x1]
                }
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            return false
        }
    }

    fun fitScore(space: Array<BooleanArray>, x: Int, y: Int): Int {
        try {
            return appearance.indices.sumOf { y1 ->
                appearance[y1].indices.count { x1 ->
                    if (appearance[y1][x1] && space[y + y1][x + x1]) {
                        return Int.MIN_VALUE
                    }
                    appearance[y1][x1] xor space[y + y1][x + x1]
                }
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            return Int.MIN_VALUE
        }
    }

    fun insert(space: Array<BooleanArray>, x: Int, y: Int) {
        appearance.indices.forEach { y1 ->
            appearance[y1].indices.forEach { x1 ->
                if (appearance[y1][x1]) {
                    space[y + y1][x + x1] = true
                }
            }
        }
    }
}

data class TaskSpace(
    val width: Int,
    val height: Int,
    val required: List<Int>,
    val shapes: List<Shape>,
) {
    val space = Array(height) { BooleanArray(width) { false } }

    val totalSize: Int = width * height

    fun feasible(): Boolean {
        val requiredSpace = required.zip(shapes).sumOf { (count, shape) -> count * shape.occupiesCount }
        if (requiredSpace > totalSize) return false
        if ((width / 3) * (height / 3) > required.sum()) return true

        val allShapeVariants = shapes.map { it.allVariants() }
        val workingCounter = required.toMutableList()

        val possiblePlacements = (0 until width).flatMap { x ->
            (0 until height).flatMap { y ->
                (0..7).map { v ->
                    Triple(x, y, v)
                }
            }
        }

        while (workingCounter.sum() > 0) {
            val nextShape = workingCounter.indexOf(workingCounter.max())
            val shapeVariants = allShapeVariants[nextShape]
            val (x,y,v) = possiblePlacements.map { (x, y, v) ->
                Triple(x, y, v) to shapeVariants[v].fitScore(space, x, y)
            }.filter { (_, s) -> s > 0 }
                .maxByOrNull { (_, s) -> s }
                ?.first ?: return false

            shapeVariants[v].insert(space, x, y)
            workingCounter[nextShape] -= 1
        }

        return true
    }
}

fun parseShapes(vararg lines: List<String>): List<Shape> {
    return lines.map {
        Shape(
            it.drop(1)
                .map {
                    it.map { c -> c == '#' }.toTypedArray()
                }.toTypedArray()
        )
    }
}

fun parseTask(inputLine: String, shapes: List<Shape>): TaskSpace {
    val regex = "(\\d+)x(\\d+): (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+)".toRegex()
    return regex.matchEntire(inputLine)!!.let { match ->
        TaskSpace(
            match.groupValues[1].toInt(),
            match.groupValues[2].toInt(),
            match.groupValues.subList(3, match.groupValues.size).map { it.toInt() },
            shapes
        )
    }
}

fun main() {
    fun part1(input: List<String>): Long {
        val shapes = parseShapes(
            input.subList(0, 4),
            input.subList(5, 9),
            input.subList(10, 14),
            input.subList(15, 19),
            input.subList(20, 24),
            input.subList(25, 29),
        )
        val treespaces = input.drop(30)
            .map { parseTask(it, shapes) }
        return treespaces.count { it.feasible() }.toLong()
    }

    fun part2(input: List<String>): Long {
        return 0L
    }

    val testInput = readInput("Day12_test")
    if (testInput.isNotEmpty()) {
        //check(part1(testInput), 2L)
        check(part2(testInput), 0L)
    }

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}