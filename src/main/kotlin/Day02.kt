import java.io.File

fun main(args: Array<String>) {
    println("2022 Advent of Code day 2")

    val guide = ArrayList<Pair<String, String>>()

    File("day2input").forEachLine { line ->
        line.split(" ").also {
           guide.add(Pair(it[0], it[1]))
        }
    }
    println("There are ${guide.size} entries in the strategy guide")

    // Part 1 - play the game according to the guide and calculate the score
    var score = 0

    guide.forEach { entry ->
        when (entry.first) {
            "A" -> {
                when (entry.second) {
                    "X" -> score += 4
                    "Y" -> score += 8
                    "Z" -> score += 3
                }
            }
            "B" -> {
                when (entry.second) {
                    "X" -> score += 1
                    "Y" -> score += 5
                    "Z" -> score += 9
                }
            }
            "C" -> {
                when (entry.second) {
                    "X" -> score += 7
                    "Y" -> score += 2
                    "Z" -> score += 6
                }
            }
        }
    }
    println("Part 1 - after following the guide, the final score is $score")

    // Part 2 - play the game with the new interpretation of the guide and calculate the score
    score = 0
    guide.forEach { entry ->
        when (entry.first) {
            "A" -> {
                when (entry.second) {
                    "X" -> score += 3
                    "Y" -> score += 4
                    "Z" -> score += 8
                }
            }
            "B" -> {
                when (entry.second) {
                    "X" -> score += 1
                    "Y" -> score += 5
                    "Z" -> score += 9
                }
            }
            "C" -> {
                when (entry.second) {
                    "X" -> score += 2
                    "Y" -> score += 6
                    "Z" -> score += 7
                }
            }
        }
    }
    println("Part 2 - after following the guide, the final score is $score")}
