import java.io.File

fun main(args: Array<String>) {
    println("2022 Advent of Code day 1")

    val elfCalories = File("day1input").readText().trim().split("\n\n").map { calList ->
        calList.split("\n").sumOf { it.toInt() }
    }.sortedDescending()
    println("There are ${elfCalories.size} elves")

    // Part 1
    println("The elf with the most calories has ${elfCalories[0]} calories")

    // Part 2
    println("The three elves with the most calories have ${elfCalories.take(3).sum()} calories")
}
