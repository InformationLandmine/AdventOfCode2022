import java.io.File

fun main(args: Array<String>) {
    println("2022 Advent of Code day 3")

    // Setup - read the rucksack contents
    val rucksacks = File("day3input").readLines().map { line ->
        Pair(line.take(line.length/2).toSet(), line.takeLast(line.length/2).toSet())
    }
    println("There are ${rucksacks.size} rucksacks to examine")

    // Part 1 - find the common items in each rucksack
    val part1 = rucksacks.map { it.first.intersect(it.second).first() }.sumOf {
        if (it.code - 96 > 0) it.code - 96 else it.code - 38
    }
    println(part1)

    // Part 2 - find common item in each group of three rucksacks
    val part2 = rucksacks.windowed(3, 3).sumOf { sackGroup ->
        sackGroup.map { it.first + it.second }.reduce { acc, next -> acc.intersect(next) }.first().let {
            if (it.code - 96 > 0) it.code - 96 else it.code - 38
        }
    }
    println(part2)
}