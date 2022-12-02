import java.io.File

fun main(args: Array<String>) {
    println("2022 Advent of Code day 2")

    // Setup - read the "strategy guide"
    val guide = File("day2input").readLines()
    println("There are ${guide.size} entries in the strategy guide")

    // Part 1 - play the game according to the guide and calculate the score
    val scoreMap1 = mapOf(
        "A X" to 4, "A Y" to 8, "A Z" to 3,
        "B X" to 1, "B Y" to 5, "B Z" to 9,
        "C X" to 7, "C Y" to 2, "C Z" to 6,
    )
    println("Part 1 - after following the guide, the final score is ${guide.sumOf { scoreMap1[it]!! }}")

    // Part 2 - play the game with the new interpretation of the guide and calculate the score
    val scoreMap2 = mapOf(
        "A X" to 3, "A Y" to 4, "A Z" to 8,
        "B X" to 1, "B Y" to 5, "B Z" to 9,
        "C X" to 2, "C Y" to 6, "C Z" to 7,
    )
    println("Part 2 - after following the guide, the final score is ${guide.sumOf { scoreMap2[it]!! }}")
}