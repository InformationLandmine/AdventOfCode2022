import java.io.File

fun main(args: Array<String>) {
    println("2022 Advent of Code day 8")

    // Setup - Read the map
    val map = File("day8input").readLines().map { line -> line.map { it.digitToInt() } }

    // Part 1 - get the visible tree count
    var visibleCount = 0
    for (row in 0..map.lastIndex) {
        for (col in 0..map[row].lastIndex) {
            visibleCount += when (isVisible(map, Pair(row, col))) { true -> 1 false -> 0}
        }
    }
    println(visibleCount)

    // Part 2 - get the max scenic score
    val scenicScores = ArrayList<Int>()
    for (row in 0..map.lastIndex) {
        for (col in 0..map[row].lastIndex) {
            scenicScores.add(scenicScore(map, Pair(row, col)))
        }
    }
    println(scenicScores.max())
}

fun isVisible(map: List<List<Int>>, point: Pair<Int, Int>): Boolean {
    val height = map[point.first][point.second]
    // Check up
    val up = !(0 until point.first).any { map[it][point.second] >= height }
    // Check down
    val down = !(point.first + 1..map.lastIndex).any { map[it][point.second] >= height }
    // Check left
    val left = !(0 until point.second).any { map[point.first][it] >= height }
    // Check right
    val right = !(point.second + 1..map[0].lastIndex).any { map[point.first][it] >= height }

    return up || down || left || right
}

fun scenicScore(map: List<List<Int>>, point: Pair<Int, Int>): Int {
    val height = map[point.first][point.second]
    // Check up
    var up = (point.first - 1 downTo 0).firstOrNull { map[it][point.second] >= height }?.let {
        point.first - it
    }
    if (up == null) up = point.first
    // Check down
    var down = (point.first + 1..map.lastIndex).firstOrNull { map[it][point.second] >= height }?.let {
        it - point.first
    }
    if (down == null) down = map.lastIndex - point.first
    // Check left
    var left = (point.second - 1 downTo  0).firstOrNull { map[point.first][it] >= height }?.let {
        point.second - it
    }
    if (left == null) left = point.second
    // Check right
    var right = (point.second + 1..map[0].lastIndex).firstOrNull { map[point.first][it] >= height }?.let {
        it - point.second
    }
    if (right == null) right = map[0].lastIndex - point.second

    return up * down * left * right
}