import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main(args: Array<String>) {
    println("2022 Advent of Code day 4")

    // Setup - read the assignment details
    val assignments = File("day4input").readLines().map { line ->
        line.split(",").map { range ->
            range.split("-").let {
                IntRange(it[0].toInt(), it[1].toInt())
            }
        }.let {
            Pair(it[0], it[1])
        }
    }
    println("There are ${assignments.size} assignments to check")

    // Part 1 - find the fully overlapping assignments
    val part1 = assignments.count {
        (it.first.first >= it.second.first && it.first.last <= it.second.last) ||
        (it.second.first >= it.first.first && it.second.last <= it.first.last)
    }
    println("There are $part1 fully overlapping assignments")

    // Part 2 - find the partially overlapping assignments
    val part2 = assignments.count {
        !IntRange(max(it.first.first, it.second.first), min(it.first.last, it.second.last)).isEmpty()
    }
    println("There are $part2 partially overlapping assignments")
}
