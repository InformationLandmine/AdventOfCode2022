import java.io.File

fun main(args: Array<String>) {
    println("2022 Advent of Code day 4")

    // Setup - read the assignment details
    val assignments = File("day4input").readLines().map { line ->
        line.split(",").map { range ->
            range.split("-").let { IntRange(it[0].toInt(), it[1].toInt()).toList() }
        }.let { Pair(it[0], it[1]) }
    }
    println("There are ${assignments.size} assignments to check")

    // Part 1 - find the fully overlapping assignments
    val part1 = assignments.count { it.first.containsAll(it.second) || it.second.containsAll(it.first) }
    println("There are $part1 fully overlapping assignments")

    // Part 2 - find the partially overlapping assignments
    val part2 = assignments.count { it.first.intersect(it.second).isNotEmpty() }
    println("There are $part2 partially overlapping assignments")
}
