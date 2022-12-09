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
            move(head, offset)
            visited.add(tail.location)
        }
    }
    println("The number of positions the tail visits at least once is ${visited.size}")
}

fun move(knot: RopeKnot, offset: Point) {
    knot.location += offset
    knot.next?.let {
        if (!knot.location.surrounding.contains(it.location)) {
            move(it, moveCloser(it, knot.location))
        }
    }
}

fun moveCloser(knot: RopeKnot, loc: Point): Point {
    val dx = if (knot.location.x == loc.x) 0 else -1 * abs(knot.location.x - loc.x) / (knot.location.x - loc.x)
    val dy = if (knot.location.y == loc.y) 0 else -1 * abs(knot.location.y - loc.y) / (knot.location.y - loc.y)
    return Point(dx, dy)
}

data class Point(val x:Int, val y:Int) {
    val surrounding: Set<Point> get() {
        return setOf(
            Point(x - 1, y + 1), Point(x, y + 1), Point(x + 1, y + 1),
            Point(x - 1, y), Point(x, y), Point(x + 1, y),
            Point(x - 1, y - 1), Point(x, y - 1), Point(x + 1, y - 1),
        )
    }
    operator fun plus(other: Point): Point {
        return Point(this.x + other.x, this.y + other.y)
    }
}

class RopeKnot(val next: RopeKnot?) {
    var location = Point(0, 0)
}