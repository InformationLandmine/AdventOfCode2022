import java.io.File

fun main(args: Array<String>) {
    println("2022 Advent of Code day 9")

    // Setup - Read the signal
    val input = File("day9input").readLines().map { line -> line.split(" ").let { Pair(it.first(), it.last().toInt())}}

    // Part 1 - move the head and tail of the rope, and count all points the tail visited
    var headLoc = Point(0, 0)
    var tailLoc = Point(0, 0)
    val visited: MutableSet<Point> = mutableSetOf(tailLoc)
    input.forEach { instruction ->
        val operation = when (instruction.first) {
            "L" -> ::moveLeft
            "R" -> ::moveRight
            "U" -> ::moveUp
            "D" -> ::moveDown
            else -> ::moveLeft
        }
        repeat(instruction.second) {
            operation(headLoc, tailLoc).let {
                headLoc = it.first
                tailLoc = it.second
                visited.add(tailLoc)
            }
        }
    }
    println(visited.size)

    val tail = RopeKnot(null)
    var head = RopeKnot(tail)
    repeat(8) {
        head = RopeKnot(head)
    }
}

fun moveLeft(head: Point, tail: Point): Pair<Point, Point> {
    val newHead = Point(head.x - 1, head.y)
    val newTail = if (newHead.surrounding.contains(tail)) tail else Point(newHead.x + 1, newHead.y)
    return Pair(newHead, newTail)
}

fun moveRight(head: Point, tail: Point): Pair<Point, Point> {
    val newHead = Point(head.x + 1, head.y)
    val newTail = if (newHead.surrounding.contains(tail)) tail else Point(newHead.x - 1, newHead.y)
    return Pair(newHead, newTail)
}

fun moveUp(head: Point, tail: Point): Pair<Point, Point> {
    val newHead = Point(head.x, head.y + 1)
    val newTail = if (newHead.surrounding.contains(tail)) tail else Point(newHead.x, newHead.y - 1)
    return Pair(newHead, newTail)
}

fun moveDown(head: Point, tail: Point): Pair<Point, Point> {
    val newHead = Point(head.x, head.y - 1)
    val newTail = if (newHead.surrounding.contains(tail)) tail else Point(newHead.x, newHead.y + 1)
    return Pair(newHead, newTail)
}

data class Point(val x:Int, val y:Int) {
    val surrounding: Set<Point> get() {
        return setOf(
            Point(x - 1, y + 1), Point(x, y + 1), Point(x + 1, y + 1),
            Point(x - 1, y), Point(x, y), Point(x + 1, y),
            Point(x - 1, y - 1), Point(x, y - 1), Point(x + 1, y - 1),
        )
    }
}

class RopeKnot(val next: RopeKnot?) {
    var location = Point(0, 0)
}