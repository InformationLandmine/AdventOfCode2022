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
    val currents = Currents(File("day17input").readText().trim())
    val chamber = Chamber(7, rockPile, currents)

    // Part 1 - Drop 2022 rocks in the chamber and find the height
    val part1times = 2022
    repeat(part1times) {
        chamber.dropRock()
        chamber.truncate()
    }
    println("After $part1times rocks fall, the highest rock in the chamber is at ${chamber.highestRow}")

    // Part 2 - Drop way more rocks in the chamber and find the height
    chamber.reset()
    val stateSet = HashMap<ChamberState, Pair<Long, Int>>()
    val numRocks = 1000000000000L
    var done = false
    val startTime = System.currentTimeMillis()
    do {
        chamber.dropRock()
        val state = chamber.truncate()
        if (stateSet.contains(state)) {
            val (oldCount, oldHeight) = stateSet.getValue(state)
            val deltaH = chamber.highestRow - oldHeight
            val deltaR = chamber.rockCount - oldCount
            val remainder = (numRocks - oldCount) % deltaR
            val (_, remainderHeight) = stateSet.filterValues { it.first == oldCount + remainder }.values.first()
            val totalHeight = ((numRocks - oldCount) / deltaR) * deltaH + remainderHeight
            println("Repeat state after rock ${chamber.rockCount}, height ${chamber.highestRow}, first seen after rock $oldCount with a height of $oldHeight")
            println("Height increases $deltaH every $deltaR rocks after rock $oldCount")
            println("Remainder: $remainder")
            println("Total height after $numRocks rocks fall is $totalHeight")
            done = true
        } else {
            stateSet[state] = Pair(chamber.rockCount, chamber.highestRow)
        }
    } while (!done)
    val totalTime = System.currentTimeMillis() - startTime
    println("Completed in $totalTime ms")
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

data class ChamberState(val rockIndex: Int, val airCurrentIndex: Int, val points: Set<Point>)

class Chamber(private val width: Int, private val rocks: RockPile, private val currents: Currents) {
    private val rockPoints = HashSet<Point>()
    private val fallingRock = HashSet<Point>()
    var rockCount = 0L

    val highestRow get() = if (rockPoints.isNotEmpty()) rockPoints.maxOf { it.y } else 0

    private fun getCurrentFloor() = (0 until width).map { col -> rockPoints.filter { it.x == col }.maxOfOrNull { it.y }}.minOf { it?: 0 }
    fun truncate(): ChamberState {
        val floor = getCurrentFloor()
        rockPoints.removeIf { it.y < floor - 1 }
        return ChamberState(rocks.loc, currents.loc, rockPoints.map { Point(it.x, it.y - floor) }.toSet())
    }

    fun reset() {
        rockPoints.clear()
        fallingRock.clear()
        rocks.reset()
        rockCount = 0
        currents.reset()
    }

    fun dropRock() {
        val rockBottom = highestRow + 4
        val rockLeft = 2
        fallingRock.addAll(rocks.next().points.map { Point(it.x + rockLeft, it.y + rockBottom) })
        do { moveLateral() } while (moveDown())
        rockPoints.addAll(fallingRock)
        fallingRock.clear()
        rockCount++
    }

    private fun moveLateral() {
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

    private fun moveDown(): Boolean {
        assert (fallingRock.size > 0)
        if (!fallingRock.any { rockPoints.contains(Point(it.x, it.y - 1)) } &&
            !fallingRock.any { it.y <= 1 }) {
            val newLoc = fallingRock.map { Point(it.x, it.y - 1) }
            fallingRock.clear()
            fallingRock.addAll(newLoc)
            return true
        } else return false
    }

    private fun drawLine(height: Int): String {
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
