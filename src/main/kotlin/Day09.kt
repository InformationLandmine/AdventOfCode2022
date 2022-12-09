import java.io.File

fun main(args: Array<String>) {
    println("2022 Advent of Code day 9")

    // Setup - Read the series of motions
    val input = File("day9input").readLines().map { line -> line.split(" ").let { Pair(it.first(), it.last().toInt())}}

    // Part 1 - move the head and tail of the rope, and count all points the tail visited
    val tail = RopeKnot(null)
    var head = RopeKnot(tail)
    // Add 8 more knots for part 2
    // repeat(8) {
    //     head = RopeKnot(head)
    // }
    val visited: MutableSet<Point> = mutableSetOf(tail.location)
    input.forEach { instruction ->
        val operation = when (instruction.first) {
            "L" -> ::moveLeft
            "R" -> ::moveRight
            "U" -> ::moveUp
            "D" -> ::moveDown
            else -> throw Exception("Invalid input direction")
        }
        repeat(instruction.second) {
            operation(head)
            visited.add(tail.location)
        }
    }
    println("The number of positions the tail visits at least once is ${visited.size}")
}

fun moveLeft(knot: RopeKnot) {
    knot.location = Point(knot.location.x - 1, knot.location.y)
    knot.next?.let {
        if (!knot.location.surrounding.contains(it.location)) {
            if (it.location.y > knot.location.y) moveDownLeft(it)
            else if (it.location.y < knot.location.y) moveUpLeft(it)
            else moveLeft(it)
        }
    }
}

fun moveUpLeft(knot: RopeKnot) {
    knot.location = Point(knot.location.x - 1, knot.location.y + 1)
    knot.next?.let {
        if (!knot.location.surrounding.contains(it.location)) {
            if (it.location.y == knot.location.y) moveLeft(it)
            else if (it.location.y == knot.location.y - 1) moveUpLeft(it)
            else if (it.location.y == knot.location.y - 2) {
                if (it.location.x == knot.location.x) moveUp(it)
                else moveUpLeft(it)
            }
            else throw Exception("Error moving up left")
        }
    }
}

fun moveUp(knot: RopeKnot) {
    knot.location = Point(knot.location.x, knot.location.y + 1)
    knot.next?.let {
        if (!knot.location.surrounding.contains(it.location)) {
            if (it.location.x > knot.location.x) moveUpLeft(it)
            else if (it.location.x < knot.location.x) moveUpRight(it)
            else moveUp(it)
        }
    }
}

fun moveUpRight(knot: RopeKnot) {
    knot.location = Point(knot.location.x + 1, knot.location.y + 1)
    knot.next?.let {
        if (!knot.location.surrounding.contains(it.location)) {
            if (it.location.y == knot.location.y) moveRight(it)
            else if (it.location.y == knot.location.y - 1) moveUpRight(it)
            else if (it.location.y == knot.location.y - 2) {
                if (it.location.x == knot.location.x) moveUp(it)
                else moveUpRight(it)
            }
            else throw Exception("Error moving up right")
        }
    }
}

fun moveRight(knot: RopeKnot) {
    knot.location = Point(knot.location.x + 1, knot.location.y)
    knot.next?.let {
        if (!knot.location.surrounding.contains(it.location)) {
            if (it.location.y > knot.location.y) moveDownRight(it)
            else if (it.location.y < knot.location.y) moveUpRight(it)
            else moveRight(it)
        }
    }
}

fun moveDownRight(knot: RopeKnot) {
    knot.location = Point(knot.location.x + 1, knot.location.y - 1)
    knot.next?.let {
        if (!knot.location.surrounding.contains(it.location)) {
            if (it.location.y == knot.location.y) moveRight(it)
            else if (it.location.y == knot.location.y + 1) moveDownRight(it)
            else if (it.location.y == knot.location.y + 2) {
                if (it.location.x == knot.location.x) moveDown(it)
                else moveDownRight(it)
            }
            else throw Exception("Error moving down right")
        }
    }
}

fun moveDown(knot: RopeKnot) {
    knot.location = Point(knot.location.x, knot.location.y - 1)
    knot.next?.let {
        if (!knot.location.surrounding.contains(it.location)) {
            if (it.location.x > knot.location.x) moveDownLeft(it)
            else if (it.location.x < knot.location.x) moveDownRight(it)
            else moveDown(it)
        }
    }
}

fun moveDownLeft(knot: RopeKnot) {
    knot.location = Point(knot.location.x - 1, knot.location.y - 1)
    knot.next?.let {
        if (!knot.location.surrounding.contains(it.location)) {
            if (it.location.y == knot.location.y) moveLeft(it)
            else if (it.location.y == knot.location.y + 1) moveDownLeft(it)
            else if (it.location.y == knot.location.y + 2) {
                if (it.location.x == knot.location.x) moveDown(it)
                else moveDownLeft(it)
            }
            else throw Exception("Error moving down left")
        }
    }
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