import java.io.File

fun main(args: Array<String>) {
    println("2022 Advent of Code day 6")

    // Setup - Read the signal
    val input = File("day6input").readText()

    // Part 1 - Find the start-of-packet marker
    val markerGroups1 = input.windowed(4, 1).map { it.toSet() }
    val part1 = markerGroups1.indexOf(markerGroups1.first { it.size == 4 }) + 4
    println("The start-of-packet marker was detected after $part1 characters")

    // Part 2 - Find the start-of-message marker
    val markerGroups2 = input.windowed(14, 1).map { it.toSet() }
    val part2 = markerGroups2.indexOf(markerGroups2.first { it.size == 14 }) + 14
    println("The start-of-message marker was detected after $part2 characters")
}
