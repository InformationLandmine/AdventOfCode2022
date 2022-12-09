import java.io.File
import kotlin.math.abs

fun main(args: Array<String>) {
    println("2022 Advent of Code day 9")

    // Setup - Read the series of motions
    val input = File("day9input").readLines().map { line -> line.split(" ").let { Pair(it.first(), it.last().toInt())}}

    // Part 1 - move the head and tail of the rope, and count all points the tail visited
    val tail = RopeKnot(null)
    var head = RopeKnot(tail)
    // Add 8 more knots for part 2
    repeat(8) {
        head = RopeKnot(head)
    }
    val visited: MutableSet<Point> = mutableSetOf(tail.location)
    input.forEach { instruction ->
        val offset = when (instruction.first) {
            "L" -> Point(-1, 0)
            "R" -> Point(1, 0)
            "U" -> Point(0, 1)
            "D" -> Point(0, -1)
            else -> throw Exception("Invalid input direction")
        }
        repeat(instruction.second) {
            head.move(offset)
            visited.add(tail.location)
        }
    }
    println("The number of positions the tail visits at least once is ${visited.size}")
}

data class Point(val x:Int, val y:Int) {
    fun isAdjacent(other: Point) = abs(x - other.x) <= 1 && abs(y - other.y) <= 1
    fun closerOffset(other: Point): Point {
        val dx = if (x == other.x) 0 else -1 * abs(x - other.x) / (x - other.x)
        val dy = if (y == other.y) 0 else -1 * abs(y - other.y) / (y - other.y)
        return Point(dx, dy)
    }
    operator fun plus(other: Point): Point {
        return Point(this.x + other.x, this.y + other.y)
    }
}

class RopeKnot(val next: RopeKnot?) {
    var location = Point(0, 0)
    fun move(offset: Point) {
        location += offset
        next?.let {
            if (!it.location.isAdjacent(location)) {
                it.move(it.location.closerOffset(location))
            }
        }
    }
}