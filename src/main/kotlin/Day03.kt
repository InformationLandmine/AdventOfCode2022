import java.io.File

fun main(args: Array<String>) {
    println("2022 Advent of Code day 3")

    // Setup - read the rucksack contents
    val rucksacks = ArrayList<Pair<List<Char>, List<Char>>>()
    val input = File("day3input").forEachLine { line ->
        rucksacks.add(Pair(line.take(line.length/2).toList(), line.takeLast(line.length/2).toList()))
    }
    println("There are ${rucksacks.size} rucksacks to examine")

    // Part 1 - find the common items in each rucksack
    val part1 = rucksacks.sumOf {
        val item = it.first.intersect(it.second).first()
        when (item.isUpperCase()) {
            true -> item.code - 38
            false -> item.code - 96
        }
    }
    println(part1)

    // Part 2 - find common item in each group of three rucksacks
    val part2 = rucksacks.windowed(3, 3).sumOf {
        val item = (it[0].first + it[0].second).intersect(it[1].first + it[1].second).intersect(it[2].first + it[2].second).first()
        when (item.isUpperCase()) {
            true -> item.code - 38
            false -> item.code - 96
        }
    }
    println(part2)
}