import java.io.File

fun main(args: Array<String>) {
    println("2022 Advent of Code day 17")

    // Setup - Define the rocks and read the wind currents
    val rockPile = RockPile(listOf(
        Rock(listOf(Point(0, 0), Point(1, 0), Point(2, 0), Point(3, 0))),
        Rock(listOf(Point(1, 0), Point(0, 1), Point(1, 1), Point(2, 1), Point(1, 2))),
        Rock(listOf(Point(0, 0), Point(1, 0), Point(2, 0), Point(2, 1), Point(2, 2))),
        Rock(listOf(Point(0, 0), Point(0, 1), Point(0, 2), Point(0, 3))),
        Rock(listOf(Point(0, 0), Point(1, 0), Point(0, 1), Point(1, 1))),
    ))
    val currents = Currents(File("day17test").readText().trim())

    // Part 1 - Drop 2022 rocks in the chamber and find the height
    val chamber = Chamber(7, rockPile, currents)
    repeat(10000) { chamber.dropRock() }
    println(chamber.highestRow)
}

enum class AirCurrent { Left, Right, Unknown }
class Currents(val currents: String) {
    var loc = 0
    fun next(): AirCurrent {
        val result = when (currents[loc++]) {
            '<' -> AirCurrent.Left
            '>' -> AirCurrent.Right
            else -> AirCurrent.Unknown
        }
        if (loc >= currents.length) loc = 0
        if (result == AirCurrent.Unknown) throw Exception("Unknown current direction")
        return result
    }
    fun reset() { loc = 0 }
}

class RockPile(val rocks: List<Rock>) {
    var loc = 0
    fun next(): Rock {
        val rock = rocks[loc++]
        if (loc >= rocks.size) loc = 0
        return rock
    }
    fun reset() { loc = 0 }
}

class Rock(val points: List<Point>)

class Chamber(private val width: Int, private val rocks: RockPile, private val currents: Currents) {
    val rockPoints = HashSet<Point>()
    val fallingRock = HashSet<Point>()

    val highestRow get() = if (rockPoints.isNotEmpty()) rockPoints.maxOf { it.y } else 0
    fun isTopFlat(): Boolean {
        val currentHighest = highestRow
        val testRow = (0 until width).map { Point(it, currentHighest) }
        return rockPoints.containsAll(testRow)
    }

    fun reset() {
        rockPoints.clear()
        fallingRock.clear()
        rocks.reset()
        currents.reset()
    }

    fun dropRock() {
        val rockBottom = highestRow + 4
        val rockLeft = 2
        fallingRock.addAll(rocks.next().points.map { Point(it.x + rockLeft, it.y + rockBottom) })
        do { moveLateral() } while (moveDown())
        rockPoints.addAll(fallingRock)
        fallingRock.clear()
    }

    fun moveLateral() {
        assert (fallingRock.size > 0)
        val direction = currents.next()
        if (direction == AirCurrent.Left) {
            if (fallingRock.minOf { it.x } > 0 && !fallingRock.any { rockPoints.contains(Point(it.x - 1, it.y)) }) {
                val newLoc = fallingRock.map { Point(it.x - 1, it.y) }
                fallingRock.clear()
                fallingRock.addAll(newLoc)
            }
        } else {
            if (fallingRock.maxOf { it.x } < width - 1 && !fallingRock.any { rockPoints.contains(Point(it.x + 1, it.y)) }) {
                val newLoc = fallingRock.map { Point(it.x + 1, it.y) }
                fallingRock.clear()
                fallingRock.addAll(newLoc)
            }
        }
    }

    fun moveDown(): Boolean {
        assert (fallingRock.size > 0)
        if (!fallingRock.any { rockPoints.contains(Point(it.x, it.y - 1)) } &&
            !fallingRock.any { it.y <= 1 }) {
            val newLoc = fallingRock.map { Point(it.x, it.y - 1) }
            fallingRock.clear()
            fallingRock.addAll(newLoc)
            return true
        } else return false
    }

    fun drawLine(height: Int): String {
        if (height > 0) {
            return "|" + (0 until width).map {
                if (rockPoints.contains(Point(it, height))) '#' else
                if (fallingRock.contains(Point(it, height))) '@' else '.'
            }.joinToString("") + "|"
        } else {
            return "+" + (0 until width).map { '-' }.joinToString("") + "+"
        }
    }

    override fun toString(): String {
        var result = "Height: $highestRow\n"
        (highestRow + 7 downTo 0).forEach {
            result += drawLine(it) + "\n"
        }
        return result
    }

}