import java.io.File

fun main(args: Array<String>) {
    println("2022 Advent of Code day 6")

    // Setup - Read the signal
    val input = File("day6input").readText()

    // Part 1 - Find the start-of-packet marker
    val part1 = input.windowed(4, 1).map { it.toSet() }.indexOfFirst { it.size == 4 } + 4
    println("The start-of-packet marker was detected after $part1 characters")

    // Part 2 - Find the start-of-message marker
    val part2 = input.windowed(14, 1).map { it.toSet() }.indexOfFirst { it.size == 14 } + 14
    println("The start-of-message marker was detected after $part2 characters")
}
